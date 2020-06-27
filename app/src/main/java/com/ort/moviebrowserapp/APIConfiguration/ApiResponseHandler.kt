package com.ort.moviebrowserapp.APIConfiguration

interface ApiResponseHandler<T> {

    fun noInternet()

    fun success(response: T?)

    fun fail(error:Throwable?)

    fun showLoading()

    fun hideLoading()
}