package com.ort.moviebrowserapp.MainLandingScreen

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.moviebrowserapp.APIConfiguration.ApiResponseHandler
import com.ort.moviebrowserapp.R
import com.ort.moviebrowserapp.pojo.MovieBrowserResponsePojo
import com.ort.moviebrowserapp.pojo.ResultPojo
import kotlinx.android.synthetic.main.activity_main.*


class MovieBrowserActivity : AppCompatActivity(),ApiResponseHandler<MovieBrowserResponsePojo> {

    var gridLayoutManager:GridLayoutManager? =null
    var movieList:List<ResultPojo>? = null
    var  model: MovieBrowserViewModel?= null
    var  viewModelFactory: MyViewModelFactory?= null
    var  isDataLoading:Boolean = false
    var pageNo =1
    var lastIndexBeforePagination = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //set up recyclerview
        gridLayoutManager = GridLayoutManager(this,2)
        rvMoviesPoster.layoutManager = gridLayoutManager
        movieList = ArrayList()

        var movieAdapter = MovieBrowserAdapter(this,movieList!!)
        rvMoviesPoster.adapter = movieAdapter
        rvMoviesPoster.addOnScrollListener(recyclerViewOnScrollListener!!)


        //configure  view model
        viewModelFactory = MyViewModelFactory(this,pageNo)
        model= ViewModelProvider(this,viewModelFactory!!).get(MovieBrowserViewModel(this,pageNo)::class.java)
        //start loading
        showLoading()
        model!!.getMovieList().observe(this, Observer<MutableList<ResultPojo>>{
            hideLoading()
            if(it.isNotEmpty())
            {
                movieList = it
                movieAdapter = MovieBrowserAdapter(this,movieList!!)
                rvMoviesPoster.adapter = movieAdapter
                rvMoviesPoster.scrollToPosition(lastIndexBeforePagination)
            }
        })
    }

    override fun noInternet() {}

    override fun success(response: MovieBrowserResponsePojo?) {}

    override fun fail(error:Throwable?) {
        hideLoading()
        Toast.makeText(this,"Something Went Wrong", Toast.LENGTH_SHORT).show()
        rvMoviesPoster.visibility = View.GONE
        llRetry.visibility = View.VISIBLE
    }

    override fun showLoading() {
        isDataLoading = true
        if(llRetry.visibility == View.VISIBLE) llRetry.visibility = View.GONE
        llProgresBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        isDataLoading = false
        llProgresBar.visibility = View.GONE
    }



    //RecyclerViewListener
    private var recyclerViewOnScrollListener: RecyclerView.OnScrollListener? =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = gridLayoutManager!!.getChildCount()
                val totalItemCount: Int = gridLayoutManager!!.getItemCount()
                val firstVisibleItemPosition: Int = gridLayoutManager!!.findFirstVisibleItemPosition()
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && !isDataLoading)
                {
                    lastIndexBeforePagination =totalItemCount
                    showLoading()
                    pageNo++
                    //to load next data
                    viewModelFactory!!.pageNo = pageNo
                    model!!.pageNo = pageNo
                    model!!.getMovieList()

                }
            }
        }

    //created view model factory for passing custome parameters to ViewModel
    class MyViewModelFactory(private val activity: Activity,var pageNo:Int): ViewModelProvider.NewInstanceFactory() {
        override fun <T: ViewModel> create(modelClass:Class<T>): T {
            return MovieBrowserViewModel(activity,pageNo) as T
        }
    }


}
