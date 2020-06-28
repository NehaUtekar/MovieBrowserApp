package com.ort.moviebrowserapp.MovieDetailsScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ort.moviebrowserapp.APIConfiguration.ApiResponseHandler
import com.ort.moviebrowserapp.MainLandingScreen.MovieBrowserActivity
import com.ort.moviebrowserapp.MainLandingScreen.MovieBrowserViewModel
import com.ort.moviebrowserapp.R
import com.ort.moviebrowserapp.pojo.MovieDetailsPojo
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.movie_browser_adapter_layout.*

class MovieDetailsActivity : AppCompatActivity(),ApiResponseHandler<MovieDetailsPojo> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        //set action bar
        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)}

        val movieId = intent.getLongExtra("MOVIE_ID",0)

        //create view model instance
       val  viewModelFactory = MovieBrowserActivity.MyViewModelFactory(this, 0, null, null)
       val  model = ViewModelProvider(this, viewModelFactory).get(
           MovieBrowserViewModel(this, 0, null,null)::class.java)

        model.movieId =movieId
        showLoading()
        model.getMovieDetails()!!.observe(this, Observer<MovieDetailsPojo> {
            //update UI
            hideLoading()
            //create url for movie poster
            val url = "https://image.tmdb.org/t/p/w220_and_h330_face"+it.posterPath
            Glide.with(this).load(url).into(ivMoviePosterImg)
            tvMovieTitle.text = it.title
            tvReleaseDate.text = it.releaseDate+"  ("+it.originalLanguage.toUpperCase()+")"
            val timeInHour:String = (it.runtime?.div(60)).toString()
            tvMovieRunTime.text = timeInHour+"h"+" "+ (it.runtime?.rem(60)).toString()+"m"
            tvRatingsData.text = (it.voteAverage?.times(10)).toString()+"%"
            tvMovieTagLine.text = it.tagline
            tvOverViewData.text = it.overview

        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home)
        {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun noInternet() {}

    override fun success(response: MovieDetailsPojo?) {}

    override fun fail(error: Throwable?) {
        hideLoading()
        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        llMainLayout.visibility = View.GONE
        llProgresBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        llMainLayout.visibility = View.VISIBLE
        llProgresBar.visibility = View.GONE
    }
}
