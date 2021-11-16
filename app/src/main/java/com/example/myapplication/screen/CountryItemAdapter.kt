package com.example.myapplication.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.network.response.model.Country

class CountryItemAdapter(private val countryList:List<Country>?, private val listener:CountryItemAdapterListener?): RecyclerView.Adapter<CountryItemAdapter.CountryItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryItemViewHolder = CountryItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.country_item_layout, parent, false))

    override fun getItemCount(): Int = countryList?.size?:0

    override fun onBindViewHolder(holder: CountryItemViewHolder, position: Int) {
        val countriesNameData = countryList?.get(position)
        holder.apply {
            countryNameView.text = countriesNameData?.name?.common
            countryNameNativeView.text = countriesNameData?.name?.official
            countryCell?.setOnClickListener {
                listener?.onCountrySelected(countriesNameData)
            }
        }
    }


    class CountryItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val countryNameView = itemView.findViewById<TextView>(R.id.country_item_name)
        val countryCell = itemView.findViewById<ConstraintLayout>(R.id.country_item_constraint)
        val countryNameNativeView = itemView.findViewById<TextView>(R.id.country_item_name_native)
    }

    interface CountryItemAdapterListener{
        fun onCountrySelected(country:Country?)
    }
}