package com.example.myapplication.network.response.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountryName() : Parcelable {

    @SerializedName("common")
    @Expose
    var common: String? = null

    @SerializedName("official")
    @Expose
    var official: String? = null

    constructor(parcel: Parcel) : this() {
        common = parcel.readString()
        official = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(common)
        parcel.writeString(official)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CountryName> {
        override fun createFromParcel(parcel: Parcel): CountryName {
            return CountryName(parcel)
        }

        override fun newArray(size: Int): Array<CountryName?> {
            return arrayOfNulls(size)
        }
    }

}

