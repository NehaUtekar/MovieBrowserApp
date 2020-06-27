package com.ort.moviebrowserapp.MainLandingScreen

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
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


class MovieBrowserActivity : AppCompatActivity(), ApiResponseHandler<MovieBrowserResponsePojo> {

    var gridLayoutManager: GridLayoutManager? = null
    var movieList: List<ResultPojo>? = null
    var model: MovieBrowserViewModel? = null
    var viewModelFactory: MyViewModelFactory? = null
    var isDataLoading: Boolean = false
    var pageNo = 1
    var lastIndexBeforePagination = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //set up recyclerview
        gridLayoutManager = GridLayoutManager(this, 2)
        rvMoviesPoster.layoutManager = gridLayoutManager
        movieList = ArrayList()

        var movieAdapter = MovieBrowserAdapter(this, movieList!!)
        rvMoviesPoster.adapter = movieAdapter
        rvMoviesPoster.addOnScrollListener(recyclerViewOnScrollListener!!)


        //configure  view model
        viewModelFactory = MyViewModelFactory(this, pageNo, null,null)
        model = ViewModelProvider(this, viewModelFactory!!).get(
            MovieBrowserViewModel(this, pageNo, null,null)::class.java
        )
        //start loading
        showLoading()
        model!!.getMovieList().observe(this, Observer<MutableList<ResultPojo>> {
            hideLoading()
            if (model!!.searchQuery!=null && it.isEmpty()) {
                rvMoviesPoster.visibility = View.GONE
                tvNoMoviesFound.visibility = View.VISIBLE
            }
            else if(it.isNotEmpty())
            {
                rvMoviesPoster.visibility = View.VISIBLE
                tvNoMoviesFound.visibility = View.GONE
                movieList = it
                movieAdapter = MovieBrowserAdapter(this, movieList!!)
                rvMoviesPoster.adapter = movieAdapter
                rvMoviesPoster.scrollToPosition(lastIndexBeforePagination)
            }
        })
    }

    override fun noInternet() {}

    override fun success(response: MovieBrowserResponsePojo?) {}

    override fun fail(error: Throwable?) {
        hideLoading()
        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        rvMoviesPoster.visibility = View.GONE
        llRetry.visibility = View.VISIBLE
    }

    override fun showLoading() {
        isDataLoading = true
        if (llRetry.visibility == View.VISIBLE) llRetry.visibility = View.GONE
        llProgresBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        isDataLoading = false
        llProgresBar.visibility = View.GONE
    }


    //to imflate sort menu option
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.sort_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.action_search).actionView as SearchView).apply {

            setOnCloseListener (object :SearchView.OnCloseListener{
                override fun onClose(): Boolean {
                    model!!.mutableMovieList.clear()
                    model!!.searchQuery = null
                    model!!.getMovieList()
                    return  true
                }
            })
            setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    //Toast.makeText(this@MovieBrowserActivity,"Query Submitted",Toast.LENGTH_SHORT).show()
                    model!!.searchResultList.clear()
                    model!!.searchQuery = p0!!
                    model!!.getSearchResult()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    if((p0!!.isEmpty() && model!!.searchResultList.isNotEmpty())||(p0.isEmpty()&&tvNoMoviesFound.visibility==View.VISIBLE))
                    {
                        model!!.mutableMovieList.clear()
                        model!!.searchQuery = null
                        model!!.getMovieList()
                    }
                  return  true
                }

            })
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        pageNo =1
        model!!.pageNo = pageNo
        if (model != null) {
            return when (item.itemId) {
                R.id.sort_popularity_aesc -> {
                    model!!.sortBy = "popularity.asc"
                    model!!.getMovieList()
                    true
                }
                R.id.sort_popularity_desc -> {
                    model!!.sortBy = "popularity.desc"
                    model!!.getMovieList()
                    true
                }
                R.id.sort_rating_aesc -> {
                    model!!.sortBy = "vote_average.asc"
                    model!!.getMovieList()
                    true
                }
                R.id.sort_rating_desc -> {
                    model!!.sortBy = "vote_average.desc"
                    model!!.getMovieList()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

        }
        return super.onOptionsItemSelected(item)
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
                val firstVisibleItemPosition: Int =
                    gridLayoutManager!!.findFirstVisibleItemPosition()
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && !isDataLoading) {
                    lastIndexBeforePagination = totalItemCount
                    showLoading()
                    pageNo++
                    //to load next data
                   // viewModelFactory!!.pageNo = pageNo
                    model!!.pageNo = pageNo
                    model!!.getMovieList()

                }
            }
        }

    //created view model factory for passing custome parameters to ViewModel
    class MyViewModelFactory(private val activity: Activity, var pageNo: Int,
                             var sortBy: String?,var searchQuery:String?) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MovieBrowserViewModel(activity, pageNo, sortBy,searchQuery) as T
        }
    }


}
