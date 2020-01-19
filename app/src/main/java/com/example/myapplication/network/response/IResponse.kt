package com.example.myapplication.network.response

interface IResponse<T> {

    fun onSuccess(result: T?)

    fun onError( message: String?, t: Throwable, response: T?)

}

