package com.example.myapplication.network.controller


import android.content.ContentValues.TAG
import android.util.Log
import androidx.annotation.CallSuper
import com.example.myapplication.network.response.IResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseController<T> :Callback<T>,
    NetworkController {

    protected var mListener: IResponse<T>? = null
    protected var mCall: Call<T>? = null

    protected fun buildRetrofit(): Retrofit? {
        return sRetrofit
    }


    fun setListener(listener: IResponse<T>): BaseController<T> {
        mListener = listener
        return this
    }

    abstract fun getModuleName(): String

    override fun cancel() {
        mListener = null
        mCall?.cancel()
        mCall = null
    }

    @CallSuper
    override fun onResponse(call: Call<T>, response: Response<T>) {
        Log.i(TAG, response.toString())

        if (response.isSuccessful) {
            mListener?.onSuccess(response.body())
        } else {
            onFailure(call, Throwable("Request Failed ${response.body()}"), response)
        }
        mListener = null
        mCall = null
    }

    @CallSuper
    override fun onFailure(call: Call<T>, t: Throwable) {
        Log.i(TAG, call.toString())
        Log.i(TAG, t.toString())

        onFailure(call, t, null)
    }

    private fun onFailure(call: Call<T>, t: Throwable, response: Response<T>?) {
        mListener?.onError( t.message, t, response?.body())
        mListener = null
        mCall = null
    }

    companion object {
        val BASE_URL = "https://restcountries.eu/rest/v2/"

        private var sRetrofit: Retrofit? = null

        init {
            val builder = OkHttpClient.Builder()
            builder.readTimeout(60, TimeUnit.SECONDS)
            builder.writeTimeout(60, TimeUnit.SECONDS)
            builder.connectTimeout(60, TimeUnit.SECONDS)
            val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                Log.i(TAG, message)
            })

            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
            val client = builder.build()

                sRetrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().excludeFieldsWithoutExposeAnnotation().create()))
                    .client(client)
                    .build()

            Log.i(TAG, client.toString())

        }

    }




}