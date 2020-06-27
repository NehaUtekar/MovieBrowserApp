package com.ort.moviebrowserapp.APIConfiguration

import com.ort.moviebrowserapp.pojo.MovieBrowserResponsePojo
import com.ort.moviebrowserapp.pojo.MovieDetailsPojo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface APIManagement {

    //call to get default movie list
    @GET("discover/movie")
    fun getMovieList(@Query("api_key") api_key: String?,
                     @Query("page") pageNo:Int,
                     @Query("sort_by") sortBy:String?): Call<MovieBrowserResponsePojo?>?

    //to get search result
    @GET("search/movie")
    fun getSearchResult(@Query("api_key") api_key: String?,
                    @Query("query") searchQuery:String?): Call<MovieBrowserResponsePojo?>?

    //to get movie details
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id")movieId:Long,
                        @Query("api_key") api_key: String?): Call<MovieDetailsPojo?>?


}