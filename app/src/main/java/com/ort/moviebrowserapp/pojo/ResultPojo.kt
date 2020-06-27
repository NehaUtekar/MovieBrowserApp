package com.ort.moviebrowserapp.pojo

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ResultPojo(
    @SerializedName("popularity")
    @Expose
     var popularity: Double = 0.0,
    @SerializedName("vote_count")
    @Expose
     var voteCount: Long = 0,
    @SerializedName("video")
    @Expose
     var video: Boolean = false,
    @SerializedName("poster_path")
    @Expose
     var posterPath: String? = null,
    @SerializedName("id")
    @Expose
     var id: Long = 0,
    @SerializedName("adult")
    @Expose
     var adult: Boolean = false,
    @SerializedName("backdrop_path")
    @Expose
     var backdropPath: String? = null,
    @SerializedName("original_language")
    @Expose
     var originalLanguage: String? = null,
    @SerializedName("original_title")
    @Expose
     var originalTitle: String? = null,
    @SerializedName("genre_ids")
    @Expose
     var genreIds: List<Long?>? = null,
    @SerializedName("title")
    @Expose
     var title: String? = null,
    @SerializedName("vote_average")
    @Expose
     var voteAverage: Double = 0.0,
    @SerializedName("overview")
    @Expose
     var overview: String? = null,
    @SerializedName("release_date")
    @Expose
     var releaseDate: String? = null
)