package com.cittis.signsup.model

import android.os.Parcel
import android.os.Parcelable

data class GeolocationCardinalImages(
    var nameCardinal: String,
    var photoPath: String
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GeolocationCardinalImages> =
            object : Parcelable.Creator<GeolocationCardinalImages> {
                override fun newArray(size: Int): Array<GeolocationCardinalImages?> = arrayOfNulls(size)
                override fun createFromParcel(source: Parcel): GeolocationCardinalImages =
                    GeolocationCardinalImages(source)
            }

    }

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            dest.writeString(nameCardinal)
            dest.writeString("$photoPath,")
        }
    }

    override fun describeContents(): Int = 0

    override fun toString(): String {
        return "\"$nameCardinal\":\"$photoPath\""
    }

}