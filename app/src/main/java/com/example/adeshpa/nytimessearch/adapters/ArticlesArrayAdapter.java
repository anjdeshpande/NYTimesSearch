package com.example.adeshpa.nytimessearch.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adeshpa.nytimessearch.R;
import com.example.adeshpa.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by adeshpa on 5/28/16.
 */
public class ArticlesArrayAdapter extends ArrayAdapter<Article> {
    public ArticlesArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get data item for currect position
        Article article = getItem(position);

        // check if existing view being reused
        // not using recycled view then inflate the layout
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }

        //find image view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);

        // clear out recycled image
        imageView.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadline());

        // Populate the image and download in background
        String thumbnail = article.getThumbnail();
        if (!TextUtils.isEmpty(thumbnail)) {
            //Picasso.with(getContext()).load(thumbnail).into(imageView);
            Glide.with(getContext()).load(thumbnail).into(imageView);
        } else {
            Glide.with(getContext())
                    .load(R.drawable.ic_default_news)
                    .into(imageView);
        }

        return convertView;
    }
}
