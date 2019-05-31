package com.cittis.signsup.model

class CittisSignal {

    var altitude: Float = 0.0f
    var latitude: Float = 0.0f
    var longitude: Float = 0.0f

    // Fotos
    var photoFront: String = ""
    var photoBack: String = ""
    var photoPlaque: String = ""

    var location: String = ""

    override fun toString(): String {
        return "\"CittusSignal\":{\"altitude\":$altitude, \"latitude\":$latitude, \"longitude\":$longitude, \"photoFront\":\"$photoFront\", \"photoBack\":\"$photoBack\", \"photoPlaque\":\"$photoPlaque\", \"location\":\"$location\"}"
    }

}