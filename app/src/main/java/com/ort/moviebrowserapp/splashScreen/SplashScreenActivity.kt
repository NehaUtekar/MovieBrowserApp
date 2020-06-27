package com.ort.moviebrowserapp.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ort.moviebrowserapp.APIConfiguration.ApiResponseHandler
import com.ort.moviebrowserapp.MainLandingScreen.MovieBrowserActivity
import com.ort.moviebrowserapp.R
import com.ort.moviebrowserapp.pojo.MovieBrowserResponsePojo


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash_screen)

        Handler().postDelayed({
           startActivity(Intent(this,MovieBrowserActivity::class.java))
            finish()
        }, 1000)
    }




}
