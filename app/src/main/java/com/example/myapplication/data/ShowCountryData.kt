package com.example.myapplication.data

import android.os.Parcel
import android.os.Parcelable
import com.example.myapplication.network.response.model.Country

data class ShowCountryData(var subtitle:String?, var explain:String?, var countryList: List<Country>?,
                           var orderEnTxtAZ:String?, var orderEnTxtZA:String?, var orderAreaTxt1:String?,
                           var orderAreaTxt9:String?) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Country),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(subtitle)
        parcel.writeString(explain)
        parcel.writeTypedList(countryList)
        parcel.writeString(orderEnTxtAZ)
        parcel.writeString(orderEnTxtZA)
        parcel.writeString(orderAreaTxt1)
        parcel.writeString(orderAreaTxt9)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShowCountryData> {
        override fun createFromParcel(parcel: Parcel): ShowCountryData {
            return ShowCountryData(parcel)
        }

        override fun newArray(size: Int): Array<ShowCountryData?> {
            return arrayOfNulls(size)
        }
    }

}