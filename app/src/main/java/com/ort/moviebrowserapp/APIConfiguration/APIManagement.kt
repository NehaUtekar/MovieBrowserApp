package com.ort.moviebrowserapp.APIConfiguration

import com.ort.moviebrowserapp.pojo.MovieBrowserResponsePojo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface APIManagement {

    //call to get default movie list
    @GET("discover/movie")
    fun getMovieList(@Query("api_key") api_key: String?,
                     @Query("page") pageNo:Int,
                     @Query("sort_by") sortBy:String?): Call<MovieBrowserResponsePojo?>?
}