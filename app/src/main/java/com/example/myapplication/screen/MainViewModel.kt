package com.example.myapplication.screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.CountryDetails
import com.example.myapplication.data.ShowCountryData
import com.example.myapplication.network.controller.GetAllCountryController
import com.example.myapplication.network.controller.GetCountryDetailsController
import com.example.myapplication.network.controller.NetworkController
import com.example.myapplication.network.response.IResponse
import com.example.myapplication.network.response.model.Country

class MainViewModel :ViewModel(){

    var stepInProcess = MutableLiveData<FlowStep>()
    var screenState = MutableLiveData<ScreenState>()
    val requests: ArrayList<NetworkController> = ArrayList()
    var resourcesString:HashMap<String, String>? = HashMap<String, String>()
    var allCountryList :List<Country>?=null

    fun startFlow(allResourcesString:HashMap<String, String>?) {
        resourcesString = allResourcesString
        sendGetAllCountryRequest()
    }

    private fun sendGetAllCountryRequest(){
        screenState.postValue(ScreenState.Waiting)
        val getAllCountryController = GetAllCountryController()
        getAllCountryController.setListener(object :IResponse<List<Country>>{
            override fun onSuccess(result: List<Country>?) {
                allCountryList = result
                screenState.postValue(ScreenState.IDLE)
                stepInProcess.postValue(FlowStep.StepShowCountry(ShowCountryData(resourcesString?.get("countrySubtitle"),
                    resourcesString?.get("countryExplain"), result,
                    resourcesString?.get("orderByEnTxtAZ"), resourcesString?.get("orderByEnTxtZA"),
                    resourcesString?.get("orderByAreaTxt1"), resourcesString?.get("orderByAreaTxt9")),
                    resourcesString?.get("titleShowCountry")))
            }

            override fun onError(message: String?, t: Throwable, response: List<Country>?) {
                screenState.postValue(ScreenState.IDLE)
            }

        })
        sendRequest(getAllCountryController)

    }

    fun onCountrySelected(country: Country){
        screenState.postValue(ScreenState.Waiting)
        val getCountryDetailsController = GetCountryDetailsController(country.name)
        getCountryDetailsController.setListener(object:IResponse<List<Country>>{
            override fun onSuccess(result: List<Country>?) {
                if(result!=null && result?.isNotEmpty() && result?.size>0){
                    val currentCountryDetails = result?.get(0)
                    searchBorderCountryByCode(currentCountryDetails)

                }
            }

            override fun onError(message: String?, t: Throwable, result: List<Country>?) {
                screenState.postValue(ScreenState.IDLE)
            }

        })
           country.borders?.let {
               if(it.size>0){
                   sendRequest(getCountryDetailsController)
               }
        }
    }

    fun searchBorderCountryByCode(countrySelected:Country){
        val allBorderCountries = ArrayList<Country>()
        countrySelected.borders?.forEach { borderCountry->
            val borderCountryList = allCountryList?.filter { it.alpha3Code==borderCountry }
            borderCountryList?.let {
                if(it.size>0){
                    allBorderCountries.add(it.get(0))
                }
            }
        }
        screenState.postValue(ScreenState.IDLE)
        stepInProcess.postValue(FlowStep.StepShowCountryDetails(
            CountryDetails(countrySelected.name, allBorderCountries, countrySelected.nativeName),
            resourcesString?.get("titleDetailsCountry")))
    }

    fun sendRequest(request: NetworkController) {
        request.start()
        requests.add(request)
    }

    override fun onCleared() {
        super.onCleared()
        requests.forEach { request ->
            request.cancel()
        }
        requests.clear()
    }

    sealed class ScreenState(){
        object Waiting:ScreenState()
        object IDLE:ScreenState()
    }

    sealed class FlowStep{
        class StepShowCountry(var showCountryData: ShowCountryData, var titleTxt:String?):FlowStep()
        class StepShowCountryDetails(var countryDetails: CountryDetails, var titleTxt:String?):FlowStep()
    }
}