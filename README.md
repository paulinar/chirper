# Chirper
A Twitter client for Android.

Time spent: ~15 hours spent in total

## Completed user stories:

 * [x] User can sign in to Twitter using OAuth login
 * [x] User can view the tweets from their home timeline
 * [x] User should be displayed the username, name, and body for each tweet
 * [x] User should be displayed the relative timestamp for each tweet "8m", "7h"
 * [x] User can view more tweets as they scroll with infinite pagination
 * [x] User can compose a new tweet by clicking a “Compose” icon in the Action Bar on the top right
 * [x] User can then enter a new tweet and post this to Twitter
 * [x] User is taken back to home timeline with new tweet visible in timeline
 * [x] *Optional:* Links in tweets are clickable and will launch the web browser (see autolink)
 * [x] *Optional:* User can see a counter with total number of characters left for tweet
 * [x] *Optional:* User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
 * [x] *Optional:* Improve the user interface and theme the app to feel "twitter branded"

Also implemented ViewHolder pattern for fast lookups.

## Walkthrough of all user stories

![Video Walkthrough](finalwalkthrough.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Libraries

This app uses the following third-party libraries:

* [scribe-java](https://github.com/fernandezpablo85/scribe-java) - Simple OAuth library for handling the authentication flow.
* [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
* [codepath-oauth](https://github.com/thecodepath/android-oauth-handler) - Custom-built library for managing OAuth authentication and signing of requests
* [Picasso](https://github.com/square/picasso) - Used for async image loading and caching them in memory and on disk.
* [ActiveAndroid](https://github.com/pardom/ActiveAndroid) - Simple ORM for persisting a local SQLite database on the Android device