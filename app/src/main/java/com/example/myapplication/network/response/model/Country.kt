package com.example.myapplication.network.response.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Country() : Parcelable {

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("nativeName")
    @Expose
    var nativeName: String? = null

    @SerializedName("area")
    @Expose
    var area: String? = null

    @SerializedName("borders")
    @Expose
    var borders: List<String>? = null

    @SerializedName("alpha3Code")
    @Expose
    var alpha3Code: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        nativeName = parcel.readString()
        area = parcel.readString()
        borders = parcel.createStringArrayList()
        alpha3Code = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(nativeName)
        parcel.writeString(area)
        parcel.writeStringList(borders)
        parcel.writeString(alpha3Code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Country> {
        override fun createFromParcel(parcel: Parcel): Country {
            return Country(parcel)
        }

        override fun newArray(size: Int): Array<Country?> {
            return arrayOfNulls(size)
        }
    }

}