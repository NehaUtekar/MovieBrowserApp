package com.ort.moviebrowserapp.MainLandingScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ort.moviebrowserapp.R
import com.ort.moviebrowserapp.pojo.ResultPojo

class MovieBrowserAdapter(var context:Context, var movieList:List<ResultPojo>):
    RecyclerView.Adapter<MovieBrowserAdapter.MovieBrowserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieBrowserViewHolder {
    val adapterView = LayoutInflater.from(context).inflate(R.layout.movie_browser_adapter_layout, parent,false)
        return   MovieBrowserViewHolder(adapterView)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieBrowserViewHolder, position: Int) {
        holder.tvMovieName.text = movieList[position].originalTitle
        holder.tvRating.text = (movieList[position].voteAverage*10).toString()+"%"
        holder.tvReleaseDate.text = movieList[position].releaseDate

        //create url for movie poster
        val url = "https://image.tmdb.org/t/p/w220_and_h330_face"+movieList[position].posterPath
        Glide.with(context)
            .load(url)
            .into(holder.ivMoviePoster);
    }



    //view holder class
    class MovieBrowserViewHolder(item: View):RecyclerView.ViewHolder(item)
    {
        val ivMoviePoster = item.findViewById<AppCompatImageView>(R.id.ivMoviePoster)
        val tvRating = item.findViewById<AppCompatTextView>(R.id.tvMovieRatings)
        val tvMovieName = item.findViewById<AppCompatTextView>(R.id.tvMovieName)
        val tvReleaseDate = item.findViewById<AppCompatTextView>(R.id.tvMovieReleaseDate)

    }
}