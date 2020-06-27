package com.ort.moviebrowserapp.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MovieBrowserResponsePojo(
    @SerializedName("page")
    @Expose
    var page: Long = 0,
    @SerializedName("total_results")
    @Expose
     var totalResults: Long = 0,
    @SerializedName("total_pages")
    @Expose
     var totalPages: Long = 0,
    @SerializedName("results")
    @Expose
     var results: List<ResultPojo>? = null
)
