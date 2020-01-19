package com.example.myapplication.data

import android.os.Parcel
import android.os.Parcelable
import com.example.myapplication.network.response.model.Country

data class CountryDetails (var countryName:String?, var borderlist:List<Country>?, var countryNativeName:String?) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(Country),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(countryName)
        parcel.writeTypedList(borderlist)
        parcel.writeString(countryNativeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CountryDetails> {
        override fun createFromParcel(parcel: Parcel): CountryDetails {
            return CountryDetails(parcel)
        }

        override fun newArray(size: Int): Array<CountryDetails?> {
            return arrayOfNulls(size)
        }
    }

}