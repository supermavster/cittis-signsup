package com.cittis.signsup.model

import android.os.Parcel
import android.os.Parcelable

data class DataUser(
    var firebase_email: String?,
    var firebase_id: String?,
    var firebase_auth: Int?
) : Parcelable {

    // 0 -> Email
    // 1 -> Id
    // 2 -> Check Auth
    // 3 -> PATH

    var firebase_path = "locations"


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DataUser> = object : Parcelable.Creator<DataUser> {
            override fun newArray(size: Int): Array<DataUser?> = arrayOfNulls(size)
            override fun createFromParcel(source: Parcel): DataUser = DataUser(source)
        }

    }

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readInt()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            dest.writeString(firebase_email)
            dest.writeString(firebase_id)
            firebase_auth?.let { it1 -> dest.writeInt(it1) }
        }
    }

    override fun describeContents(): Int = 0
    override fun toString(): String {
        return "\"DataUser\":{\"firebase_email\":\"$firebase_email\", \"firebase_id\":\"$firebase_id\", \"firebase_auth\":$firebase_auth, \"firebase_path\":\"$firebase_path\"}"
    }


    fun getData(): String {
        return "{" + toString() + "}"
    }
}