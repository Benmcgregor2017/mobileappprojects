# Mobileappprojects

Ryan's repository for Mobile App projects! 

# Information
Here you'll find the application for QuikFlix a movie list builder application. It's an application that
is written in Java and was tested on a Pixel 2 Android SDK with an API level of 29.

# What does it do?
The application utilizes Firebase email Authentication and Firebase Real Time database to manipulate information obtained from
the Omdb API call. The user can add/delete favorite movies or view their list of favorite movies. 

# Activities

MainActivity - Log In screen. User starts here.

RegisterActivity - Create an account screen.

HomeActivity - Search for a movie using th Omdb API call or access Favorites list

MovieActivity - View movie data obtained from Omdb api call

FavoriteActivity - View your list of favorite movies.

# Extra Java files

Favorites - Data class file that holds a single favorites record in the Firebase RealTime Database

FirebaseDatabaseHelper - File that aids in the manipulation of the RealTime Database. Has add/delete/read capabilites

RecyclerViewAdapter - Creates a recycleview object and populates the data in each item with the results of the Omdb api call made in
HomeActivity

RecyclerView_fav - RecyclerView used in FavoriteActivity which takes in the Favorites data class and keys from the RealTime database to
populate itself.
