package com.example.myapplication.network.controller

import com.example.myapplication.network.AppService
import com.example.myapplication.network.response.model.Country

class GetCountryDetailsController(var countrySelected:String?):BaseController<List<Country>>(){
    override fun getModuleName(): String="name"

    override fun start() {
        val countryDetailsService = buildRetrofit()?.create(AppService::class.java)
        countryDetailsService?.getCountryDetails(countrySelected)?.enqueue(this)
    }

}