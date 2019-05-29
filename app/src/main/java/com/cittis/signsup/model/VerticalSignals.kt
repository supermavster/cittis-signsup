package com.cittis.signsup.model

class VerticalSignals {

    var verticalNameSignal: String = ""
    var sizeSignal: String = ""
    var stateSingal: Float = 0.0f
    var postTypeSignal: String = ""
    var statePost: Float = 0.0f

    override fun toString(): String {
        return "\"VerticalSignal\":{\"verticalNameSignal\":\"$verticalNameSignal\", \"sizeSignal\":\"$sizeSignal\", \"stateSingal\":$stateSingal, \"postTypeSignal\":\"$postTypeSignal\", \"statePost\":$statePost}"
    }


}