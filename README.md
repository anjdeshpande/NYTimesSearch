# New York Times Article Search

This is an Android demo application for searching New York Times News articles which allows a user to find old articles. 
See the http://developer.nytimes.com/ for a API overview.

Time spent: 8 hours spent in total

Completed user stories:

 * [x] Required: User can enter a search query that will display a grid of news articles using the thumbnail and headline from the New York Times Search API.
 * [x] Required: User can click on "settings" which allows selection of advanced search options to filter results.
 * [x] Required: User can configure advanced search filters such as
 			Begin Date (using a date picker)
			News desk values (Arts, Fashion & Style, Sports)
			Sort order (oldest or newest)
 * [x] Required: Subsequent searches will have any filters applied to the search results.
 * [x] Required: User can tap on any article in results to view the contents in an embedded browser.
 * [x] Required: User can scroll down "infinitely" to continue loading more news articles. The maximum number of articles is limited by the API search.
 
 * [x] Advanced: Robust error handling, check if internet is available, handle error cases, network failures.
 * [x] Advanced: Use the ActionBar SearchView or custom layout as the query box instead of an EditText.
 * [x] Advanced: User can share a link to their friends or email it to themselves.
 * [x] Advanced: Improve the user interface and experiment with image assets and/or styling and coloring
 * [x] Bonus: Use Parcelable instead of Serializable using the popular Parceler library. 
 * [x] Bonus: Replace Picasso with Glide for more efficient image rendering.
 
 Walkthrough of all user stories:

![Video Walkthrough] (nytimessearch.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).
