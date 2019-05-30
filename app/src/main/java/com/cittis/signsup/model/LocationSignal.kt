package com.cittis.signsup.model

class LocationSignal {

    var firstAddressSignal: String? = null
    var secondAddressSignal: String? = null
    var thirdAddressSignal: String? = null


    override fun toString(): String {
        return "\"LocationSignal\":{\"firstAddressSignal\":\"$firstAddressSignal\",\"secondAddressSignal\":\"$secondAddressSignal\",\"thirdAddressSignal\":\"$thirdAddressSignal\"}"
    }


}
