package com.ort.moviebrowserapp.MainLandingScreen

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ort.moviebrowserapp.APIConfiguration.RetrofitConfiguration
import com.ort.moviebrowserapp.R
import com.ort.moviebrowserapp.pojo.MovieBrowserResponsePojo
import com.ort.moviebrowserapp.pojo.ResultPojo
import com.ort.moviebrowserapp.splashScreen.SplashScreenActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MovieBrowserViewModel(var activity: Activity,var pageNo:Int, var sortBy:String?):ViewModel(){

    private val movieList: MutableLiveData<MutableList<ResultPojo>> = MutableLiveData()
    private var mutableMovieList:MutableList<ResultPojo> = ArrayList()

     fun getMovieList(): LiveData<MutableList<ResultPojo>> {
        loadMovieList(activity.getString(R.string.api_key),pageNo,sortBy)
        return movieList
    }

     fun loadMovieList(apiKey:String, pageNo: Int, sortBy: String?) {
      try {
          RetrofitConfiguration.apiManagement()!!.getMovieList(apiKey,pageNo,sortBy)!!
              .enqueue(object : Callback<MovieBrowserResponsePojo?> {
                  override fun onFailure(call: Call<MovieBrowserResponsePojo?>?, t: Throwable?) {
                      Log.e("TAG","Inside Failure")
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




}