package com.cittis.signsup.model

import android.os.Parcel
import android.os.Parcelable

data class CittisSignsUp(var count: Int, var typeRoad: String?, var typeSignal: String?) : Parcelable {

    // Variables
    var imagesByCode: ImagenSignalCode? = null

    // Horizontal
    var horizontalSignal: HorizontalSignals? = null
    // Vertical
    var verticalSignal: VerticalSignals? = null


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Municipalities> = object : Parcelable.Creator<Municipalities> {
            override fun newArray(size: Int): Array<Municipalities?> = arrayOfNulls(size)
            override fun createFromParcel(source: Parcel): Municipalities = Municipalities(source)
        }

    }

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString(),
        source.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            dest.writeInt(count)
            dest.writeString(typeRoad)
            dest.writeString(typeSignal)
        }
    }

    override fun describeContents(): Int = 0

    override fun toString(): String {

        var typeSignalMain = when (typeSignal) {
            "Vertical" -> this.verticalSignal
            "Horizontal" -> this.horizontalSignal
            else -> null
        }
        //return "\"$count\":{\"typeRoad\":\"$typeRoad\",\"typeSignal\":\"$typeSignal\",$imagesByCode,$locationSignal,$cittusSignal,$typeSignalMain}";

        return "\"$count\":{\"typeRoad\":\"$typeRoad\",\"typeSignal\":\"$typeSignal\"}"
    }


}