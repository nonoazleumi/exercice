package com.example.myapplication.screen

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.CountryDetails
import com.example.myapplication.data.ShowCountryData
import com.example.myapplication.databinding.CountryDetailsLayoutBinding
import com.example.myapplication.databinding.ShowCountryLayoutBinding
import com.example.myapplication.network.response.model.Country
import com.example.myapplication.screen.base.BaseFragment
import com.example.myapplication.screen.base.NavigationContent
import com.example.myapplication.screen.utils.animationTwoLinesSandwich
import com.example.myapplication.screen.utils.fadeIn

class ShowCountryDetailsFragment :BaseFragment() {

    override fun getNavigationContent(): NavigationContent = NavigationContent(title)


    companion object{
        const val DETAILS_COUNTRY = "detailsCountry"
        const val TITLE = "title"

        @JvmStatic
        fun newInstance(data:CountryDetails?, title:String?): ShowCountryDetailsFragment {
            return ShowCountryDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DETAILS_COUNTRY, data)
                    putString(TITLE, title)
                }
            }
        }
    }

    private var countryDetails:CountryDetails?=null
    private  var title :String?=null
    private lateinit var activityViewModel: MainViewModel
    private lateinit var binding: CountryDetailsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countryDetails = it.getParcelable(DETAILS_COUNTRY)
            title = it.getString(TITLE)
        }
        activity?.let {
            activityViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<CountryDetailsLayoutBinding>(inflater, R.layout.country_details_layout, null, false)
        binding.data = countryDetails
        binding.showCountryBorder.adapter = CountryItemAdapter(countryDetails?.borderlist, null)
        binding.showCountryBorder.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.showCountryBorder.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        animate()
        return binding.root
    }

    private fun animate(){
        binding.countryName.alpha=0f
        binding.countryNameNative.alpha=0f
        binding.showCountryBorder.alpha=0f
        binding.lineBottom.alpha=0f

        binding.countryName.fadeIn()
        binding.countryNameNative.fadeIn(600)
        Handler().postDelayed({
            animationTwoLinesSandwich(binding.lineTop, binding.lineBottom)
            binding.showCountryBorder.fadeIn(650)
        },600)

    }


}