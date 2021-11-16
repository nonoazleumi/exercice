package com.example.myapplication.network.response.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Country() : Parcelable {

    @SerializedName("name")
    @Expose
    var name: CountryName? = null

    @SerializedName("area")
    @Expose
    var area: String? = null

    @SerializedName("borders")
    @Expose
    var borders: List<String>? = null

    @SerializedName("cca3")
    @Expose
    var cca3: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readParcelable(CountryName::class.java.classLoader)
        area = parcel.readString()
        borders = parcel.createStringArrayList()
        cca3 = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(name, flags)
        parcel.writeString(area)
        parcel.writeStringList(borders)
        parcel.writeString(cca3)
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