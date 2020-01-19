package com.example.myapplication.network.controller

import com.example.myapplication.network.AppService
import com.example.myapplication.network.response.model.Country

class GetAllCountryController:BaseController<List<Country>>(){
    override fun getModuleName(): String="all"

    override fun start() {
        val countryService = buildRetrofit()?.create(AppService::class.java)
        countryService?.getAllCountry()?.enqueue(this)
    }

}