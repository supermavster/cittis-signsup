package com.cittis.signsup.model

import android.os.Parcel
import android.os.Parcelable

data class Municipalities(
    var nameMunicipal: String?,
    var nameDepartment: String?
) : Parcelable {

    // 0 -> Id Inventario
    // 1 -> Id Lista Senal
    // 2 -> Id Max Signal
    // 3 -> Municipio
    // 4 -> Departamento


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Municipalities> = object : Parcelable.Creator<Municipalities> {
            override fun newArray(size: Int): Array<Municipalities?> = arrayOfNulls(size)
            override fun createFromParcel(source: Parcel): Municipalities = Municipalities(source)
        }

    }

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            dest.writeString(nameMunicipal)
            dest.writeString(nameDepartment)
        }
    }

    override fun describeContents(): Int = 0

    override fun toString(): String {
        return "\"Municipalities\":{\"nameMunicipal\":\"$nameMunicipal\",\"nameDepartment\":\"$nameDepartment\"}"
    }
}

