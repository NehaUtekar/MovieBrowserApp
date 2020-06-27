package com.ort.moviebrowserapp.MainLandingScreen

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ort.moviebrowserapp.APIConfiguration.RetrofitConfiguration
import com.ort.moviebrowserapp.R
import com.ort.moviebrowserapp.pojo.MovieBrowserResponsePojo
import com.ort.moviebrowserapp.pojo.MovieDetailsPojo
import com.ort.moviebrowserapp.pojo.ResultPojo
import com.ort.moviebrowserapp.splashScreen.SplashScreenActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MovieBrowserViewModel(var activity: Activity,var pageNo:Int, var sortBy:String?,var searchQuery:String?):ViewModel(){

    private val movieList: MutableLiveData<MutableList<ResultPojo>> = MutableLiveData()
     var mutableMovieList:MutableList<ResultPojo> = ArrayList()
     var searchResultList:MutableList<ResultPojo> = ArrayList()

    //to fetch movie details
     var movieId:Long = 0
     var mutableMovieDetails: MutableLiveData<MovieDetailsPojo>? = MutableLiveData()

     fun getMovieList(): LiveData<MutableList<ResultPojo>> {
        loadMovieList(activity.getString(R.string.api_key))
        return movieList
    }

    fun getMovieDetails(): MutableLiveData<MovieDetailsPojo>? {
        loadMovieDetails(activity.getString(R.string.api_key))
        return mutableMovieDetails
    }

    fun getSearchResult(): LiveData<MutableList<ResultPojo>> {
        loadSearchResult(activity.getString(R.string.api_key))
        return movieList
    }

     fun loadMovieList(apiKey:String) {
      try {
          RetrofitConfiguration.apiManagement()!!.getMovieList(apiKey,pageNo,sortBy)!!
              .enqueue(object : Callback<MovieBrowserResponsePojo?> {
                  override fun onFailure(call: Call<MovieBrowserResponsePojo?>?, t: Throwable?) {
                      if(activity is MovieBrowserActivity) (activity as MovieBrowserActivity).fail(t)
                  }

                  override fun onResponse(
                      call: Call<MovieBrowserResponsePojo?>?,
                      response: Response<MovieBrowserResponsePojo?>?) {
                      if(response!!.isSuccessful)
                      {
                          if((mutableMovieList.isNullOrEmpty()||!sortBy.isNullOrEmpty())&& pageNo==1)
                          {
                              mutableMovieList = response.body()!!.results!!.toMutableList()
                          }
                          else mutableMovieList.addAll(movieList.value!!.size,response.body()!!.results!!)
                          movieList.postValue(mutableMovieList)
                      }
                  }

              })
      }
      catch (e:Exception){
         e.printStackTrace()
      }
    }

    //to get search result
    fun loadSearchResult(apiKey: String)
    {
        RetrofitConfiguration.apiManagement()!!.getSearchResult(apiKey,searchQuery)!!
            .enqueue(object : Callback<MovieBrowserResponsePojo?> {
                override fun onFailure(call: Call<MovieBrowserResponsePojo?>?, t: Throwable?) {
                    if(activity is MovieBrowserActivity) (activity as MovieBrowserActivity).fail(t)
                }
                override fun onResponse(
                    call: Call<MovieBrowserResponsePojo?>?,
                    response: Response<MovieBrowserResponsePojo?>?) {
                    if(response!!.isSuccessful)
                    {
                        searchResultList = response.body()!!.results!!.toMutableList()
                        movieList.postValue(searchResultList)
                    }
                }

            })
    }


    //to load movie details
    fun loadMovieDetails(apiKey: String)
    {
        RetrofitConfiguration.apiManagement()!!.getMovieDetails(movieId,apiKey)!!
            .enqueue(object : Callback<MovieDetailsPojo?> {
                override fun onFailure(call: Call<MovieDetailsPojo?>?, t: Throwable?) {
                    Log.d("TAG","OnFailure")
                }
                override fun onResponse(
                    call: Call<MovieDetailsPojo?>?,
                    response: Response<MovieDetailsPojo?>?) {
                    if(response!!.isSuccessful)
                    {
                        mutableMovieDetails!!.postValue(response.body())
                    }
                }
            })
    }
}