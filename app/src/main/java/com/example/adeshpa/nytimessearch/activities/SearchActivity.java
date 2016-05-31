package com.example.adeshpa.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.adeshpa.nytimessearch.R;
import com.example.adeshpa.nytimessearch.adapters.ArticlesArrayAdapter;
import com.example.adeshpa.nytimessearch.listeners.EndlessScrollListener;
import com.example.adeshpa.nytimessearch.models.AppStatus;
import com.example.adeshpa.nytimessearch.models.Article;
import com.example.adeshpa.nytimessearch.models.FilterDialogFragment;
import com.example.adeshpa.nytimessearch.models.Filter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    private Filter filter;
    private String filterQuery;

    ArrayList<Article> articles;
    ArticlesArrayAdapter adapter;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();

        GridView lvItems = (GridView) findViewById(R.id.gvResults);
        // Attach the listener to the AdapterView onCreate
        lvItems.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    private void customLoadMoreDataFromApi(int page) {
        onArticleSearch (filterQuery, page);
    }

    public void setupViews() {
        gvResults = (GridView) findViewById(R.id.gvResults);

        articles = new ArrayList<Article>();
        adapter = new ArticlesArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        filter = new Filter();

        // Setup grid view click listener
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create intent to display article
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

                // get the article
                Article article = articles.get(position);

                // pass into intent
                intent.putExtra ("article", article);

                // launch the activity
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                // Clear grid view data
                adapter.clear();

                searchView.clearFocus();
                filterQuery = query;
                onArticleSearch (query, 0);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(item.getItemId()) {
            case R.id.miOptions:
                Intent intent = new Intent(this, FilterActivity.class);
                intent.putExtra("filter", filter);
                this.startActivityForResult(intent, REQUEST_CODE);
                //showEditDialog();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance("Some Title");
        filterDialogFragment.show(fm, "fragment_filter");
    }

    public void onArticleSearch(String query, int pageNumber) {

        if (AppStatus.getInstance(this).isOnline()) {

            AsyncHttpClient client = new AsyncHttpClient();
            String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";


            RequestParams params = new RequestParams();
            params.put("api-key", "5821cc890a5b40a785836794e8cd76ab");
            params.put("page", pageNumber);
            params.put("q", query);
            addFilterParamsToQuery(params);

            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray articleJsonArray = null;

                    try {
                        articleJsonArray = response.getJSONObject("response").getJSONArray("docs");
                        adapter.addAll(Article.fromJsonArray(articleJsonArray));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Toast.makeText(this,"You are not online!!!!",Toast.LENGTH_SHORT).show();
        }
    }

    private void addFilterParamsToQuery(RequestParams params) {
        if (!TextUtils.isEmpty(filter.getBeginDate())) {
            params.put("begin_date", filter.getBeginDate());
        }
        if (!TextUtils.isEmpty(filter.getSort())) {
            params.put("sort", filter.getSort());
        }
        String fl = TextUtils.join(",", filter.getFl());
        if (!TextUtils.isEmpty(fl)) {
            params.put("fq", fl);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            filter = data.getExtras().getParcelable("filter");
        }
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    }
}
