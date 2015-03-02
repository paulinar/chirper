# Chirper
A Twitter client for Android.

Time spent for Part A: ~15 hours
Time spent for Part B: ~10 hours

## Completed user stories:

# Part A

 * [x] User can sign in to Twitter using OAuth login
 * [x] User can view the tweets from their home timeline
 	* [x] User should be displayed the username, name, and body for each tweet
 	* [x] User should be displayed the relative timestamp for each tweet "8m", "7h"
 	* [x] User can view more tweets as they scroll with infinite pagination
 	* [x] *Optional:* Links in tweets are clickable and will launch the web browser (see autolink)
 * [x] User can compose a new tweet
 	* [x] User can click a “Compose” icon in the Action Bar on the top right
 	* [x] User can then enter a new tweet and post this to twitter
 	* [x] User is taken back to home timeline with new tweet visible in timeline
 	* [x] *Optional:* User can see a counter with total number of characters left for tweet
 * [x] *Advanced:* User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
 * [x] *Advanced:* User can open the twitter app offline and see last loaded tweets
  	* [x] Tweets are persisted into sqlite and can be displayed from the local DB
 * [x] *Advanced:* Improve the user interface and theme the app to feel "twitter branded"

# Part B

 * [x] User can switch between Timeline and Mention views using tabs.
  	* [x] User can view their home timeline tweets.
  	* [x] User can view the recent mentions of their username.
 * [x] User can navigate to view their own profile
  	* [x] User can see picture, tagline, # of followers, # of following, and tweets on their profile.
 * [x] User can click on the profile image in any tweet to see another user's profile.
  	* [x] User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
  	* [x] Profile view should include that user's timeline
 * [x] User can infinitely paginate any of these timelines (home, mentions, user) by scrolling to the bottom
 * [x] *Advanced:* Robust error handling, check if internet is available, handle error cases, network failures
 * [] *Advanced:* User can take favorite (and unfavorite) or reweet actions on a tweet
 * [x] *Advanced:* Improve the user interface and theme the app to feel twitter branded

Also implemented ViewHolder pattern for fast lookups.

## Walkthrough of all user stories

![Video Walkthrough](finalwalkthrough.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).
Icons credited to [NounProject](http://thenounproject.com).

## Libraries

This app uses the following third-party libraries:

* [scribe-java](https://github.com/fernandezpablo85/scribe-java) - Simple OAuth library for handling the authentication flow.
* [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
* [codepath-oauth](https://github.com/thecodepath/android-oauth-handler) - Custom-built library for managing OAuth authentication and signing of requests
* [Picasso](https://github.com/square/picasso) - Used for async image loading and caching them in memory and on disk.
* [ActiveAndroid](https://github.com/pardom/ActiveAndroid) - Simple ORM for persisting a local SQLite database on the Android device