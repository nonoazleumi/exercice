package com.example.myapplication.screen

import android.icu.text.CaseMap
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.ShowCountryData
import com.example.myapplication.databinding.ShowCountryLayoutBinding
import com.example.myapplication.network.response.model.Country
import com.example.myapplication.screen.base.BaseFragment
import com.example.myapplication.screen.base.NavigationContent
import com.example.myapplication.screen.utils.animationTwoLinesSandwich
import com.example.myapplication.screen.utils.animationTwoLinesSandwichClose
import com.example.myapplication.screen.utils.fadeIn
import com.example.myapplication.screen.utils.fadeOut

class ShowCountryFragment :BaseFragment(), CountryItemAdapter.CountryItemAdapterListener {

    override fun getNavigationContent(): NavigationContent = NavigationContent(title)


    companion object{
        const val SHOW_COUNTRY_DATA = "showCountryData"
        const val TITLE = "title"

        @JvmStatic
        fun newInstance(data:ShowCountryData?, title:String?):ShowCountryFragment{
            return ShowCountryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SHOW_COUNTRY_DATA, data)
                    putString(TITLE, title)
                }
            }
        }
    }

    private var showCountryData:ShowCountryData?=null
    private  var title :String?=null
    private lateinit var activityViewModel: MainViewModel
    private lateinit var binding: ShowCountryLayoutBinding
    private var iconSelected :MutableLiveData<ImageView?>?=MutableLiveData(null)
    private lateinit var lastIconSelected:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            showCountryData = it.getParcelable(SHOW_COUNTRY_DATA)
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
        binding = DataBindingUtil.inflate<ShowCountryLayoutBinding>(inflater, R.layout.show_country_layout, null, false)
        binding.data = showCountryData
        binding.showCountryList.adapter = CountryItemAdapter(showCountryData?.countryList, this)
        binding.showCountryList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.showCountryList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        val sortedList = showCountryData?.countryList?.sortedBy { it.name?.common }
        binding.showCountryList.adapter = CountryItemAdapter(sortedList, this)
        lastIconSelected = binding.iconOrderByAsc
        binding.iconOrderByAsc.setImageResource(R.drawable.asc_active)
        iconSelected?.postValue(binding.iconOrderByAsc)

        binding.iconOrderByAsc.setOnClickListener {
            iconSelected?.postValue(binding.iconOrderByAsc)
            val sortedList = showCountryData?.countryList?.sortedBy { it.name?.common }
            binding.showCountryList.adapter = CountryItemAdapter(sortedList, this)
        }

        binding.iconOrderByDesc.setOnClickListener {
            iconSelected?.postValue(binding.iconOrderByDesc)
            val sortedList = showCountryData?.countryList?.sortedByDescending { it.name?.common }
            binding.showCountryList.adapter = CountryItemAdapter(sortedList, this)
        }

        binding.iconOrderByAreaLittle.setOnClickListener {
            iconSelected?.postValue(binding.iconOrderByAreaLittle)
            val sortedList = showCountryData?.countryList?.sortedBy { it.area }
            binding.showCountryList.adapter = CountryItemAdapter(sortedList, this)
        }

        binding.iconOrderByAreaBig.setOnClickListener {
            iconSelected?.postValue(binding.iconOrderByAreaBig)
            val sortedList = showCountryData?.countryList?.sortedByDescending { it.area }
            binding.showCountryList.adapter = CountryItemAdapter(sortedList, this)
        }




        iconSelected?.observe(this, Observer {
            resetImages()
            when(it?.id){
                R.id.icon_order_by_asc->{
                    it.setImageResource(R.drawable.asc_active)
                }
                R.id.icon_order_by_desc->{
                    it.setImageResource(R.drawable.desc_active)
                }
                R.id.icon_order_by_area_big->{
                    it.setImageResource(R.drawable.area_big_active)
                }
                R.id.icon_order_by_area_little->{
                    it.setImageResource(R.drawable.area_little_active)
                }
            }
            it?.let {
                lastIconSelected = it
            }
        })

        animate()
        return binding.root
    }

    private fun animate(){
        binding.iconOrderByAsc.alpha=0f
        binding.iconOrderByDesc.alpha=0f
        binding.iconOrderByAreaBig.alpha=0f
        binding.iconOrderByAreaLittle.alpha=0f
        binding.showCountryList.alpha=0f
        binding.lineBottom.alpha=0f

        binding.iconOrderByAsc.fadeIn(250)
        binding.iconOrderByDesc.fadeIn(500)
        binding.iconOrderByAreaBig.fadeIn(750)
        binding.iconOrderByAreaLittle.fadeIn(900)
        Handler().postDelayed({
            animationTwoLinesSandwich(binding.lineTop, binding.lineBottom)
            binding.showCountryList.fadeIn(650)
        },1000)
    }

    private fun resetImages(){
        when(lastIconSelected.id){
            R.id.icon_order_by_asc->{
                lastIconSelected.setImageResource(R.drawable.asc_inactive)
            }
            R.id.icon_order_by_desc->{
                lastIconSelected.setImageResource(R.drawable.desc_inactive)
            }
            R.id.icon_order_by_area_big->{
                lastIconSelected.setImageResource(R.drawable.area_big_inactive)
            }
            R.id.icon_order_by_area_little->{
                lastIconSelected.setImageResource(R.drawable.area_little_inactive)
            }
        }
    }

    override fun onCountrySelected(country: Country?) {

        country?.borders?.let {
            if(it.size>0){
                animateExit(country)
            } else showPopUp()
        }?:showPopUp()
    }

    private fun animateExit(country:Country){
        binding.iconOrderByAsc.fadeOut(250)
        binding.iconOrderByDesc.fadeOut(500)
        binding.iconOrderByAreaBig.fadeOut(750)
        binding.iconOrderByAreaLittle.fadeOut(900)
        Handler().postDelayed({
            binding.showCountryList.fadeOut(350)
            animationTwoLinesSandwichClose(binding.lineTop, binding.lineBottom, binding.lineTop.y)
            Handler().postDelayed({
                activityViewModel.onCountrySelected(country)
            },650)
        },1000)

    }
    private fun showPopUp() {
        Toast.makeText(context, resources.getString(R.string.there_is_no_border), Toast.LENGTH_SHORT ).show()
    }
}
