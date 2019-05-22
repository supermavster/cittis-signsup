package com.cittis.signsup.model


import android.os.Parcel
import android.os.Parcelable

data class CittisListSignal(
    var idProject: Int,
    var dataUser: DataUser?,
    var municipality: Municipalities?
    //var signal: ArrayList<CittusISV>?,
    //var geolocationCardinalImages: ArrayList<GeolocationCardinalImages>?
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CittisListSignal> = object : Parcelable.Creator<CittisListSignal> {
            override fun newArray(size: Int): Array<CittisListSignal?> = arrayOfNulls(size)
            override fun createFromParcel(source: Parcel): CittisListSignal = CittisListSignal(source)
        }

    }

    constructor(dataUser: Parcel) : this(
        dataUser.readInt(),
        dataUser.readParcelable<DataUser>(DataUser::class.java.classLoader),
        dataUser.readParcelable<Municipalities>(DataUser::class.java.classLoader)
        //source.readParcelable(DataUser::class.java.classLoader)
    )

    /*
    constructor(source: Parcel) : this(

        //source.readParcelable(DataUser::class.java.classLoader)
        //source.readParcelable(DataUser::class.java.classLoader)
        /*  source.readParcelable(Municipalities::class.java.classLoader),
        arrayListOf<CittusISV>().apply {
            source.readArrayList(CittusISV::class.java.classLoader)
        },
        arrayListOf<GeolocationCardinalImages>().apply {
            source.readArrayList(GeolocationCardinalImages::class.java.classLoader)
        }*/
    )*/

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            dest.writeInt(idProject)
            dest.writeParcelable(dataUser, flags)
            // Data Municipality
            dest.writeParcelable(municipality, flags)
            /*/ Cittus ISV - Signal
            var size: Int? = signal?.size
            if (size == null) {
                size = 0
            }
            for (i in 0 until size) {
                dest.writeParcelableArray(arrayOf(signal?.get(i)), flags)
            }
            // Cardinal Images
            size = geolocationCardinalImages?.size
            if (size == null) {
                size = 0
            }
            for (i in 0 until size) {
                dest.writeParcelableArray(arrayOf(geolocationCardinalImages?.get(i)), flags)
            }*/
        }
    }

    override fun describeContents(): Int = 0

    override fun toString(): String {
        //return "[{\"CittusListSignal\":{\"login\":\"$login\",$municipality,\"CittusISV\":{$signal},\"GeolocationCardinalImages\":{$geolocationCardinalImages}}}]"
        return "[{\"CittusListSignal\":{'idProject':$idProject,$dataUser,$municipality}}]"
    }
}
