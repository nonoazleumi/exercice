package com.example.myapplication.network.controller

import com.example.myapplication.network.AppService
import com.example.myapplication.network.response.model.Country

class GetCountryByCodeController(var code:String?):BaseController<Country>(){
    override fun getModuleName(): String="alpha"

    override fun start() {
        val countryDetailsService = buildRetrofit()?.create(AppService::class.java)
        countryDetailsService?.getCountryByCode(code)?.enqueue(this)
    }

}