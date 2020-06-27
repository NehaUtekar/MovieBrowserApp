package com.ort.moviebrowserapp.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ort.moviebrowserapp.APIConfiguration.ApiResponseHandler
import com.ort.moviebrowserapp.MainLandingScreen.MovieBrowserActivity
import com.ort.moviebrowserapp.R
import com.ort.moviebrowserapp.pojo.MovieBrowserResponsePojo


class SplashScreenActivity : AppCompatActivity(), ApiResponseHandler<MovieBrowserResponsePojo> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash_screen)

        Handler().postDelayed({
           startActivity(Intent(this,MovieBrowserActivity::class.java))
            finish()
        }, 1000)
    }

    override fun noInternet() {}

    override fun success(response: MovieBrowserResponsePojo?) {}

    override fun fail(error:Throwable?) {
     /*   hideLoading()
        Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show()
        llRetry.visibility = View.VISIBLE*/
    }

    override fun showLoading() {}

    override fun hideLoading() {
       // llProgresBar.visibility = View.GONE
    }




}
