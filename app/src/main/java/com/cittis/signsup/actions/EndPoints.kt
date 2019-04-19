package com.cittis.signsup.actions


object EndPoints {

    // Users
    var FireBaseID = ""
    var FireBasePath = "locations"

    // Server
    // Server Main 172.20.1.14 172.20.1.19 192.168.43.137 172.20.1.13
    private const val SERVER_MAIN = "ivs.cittis.com.co"
    private const val PATH_SOURCE = "https://$SERVER_MAIN/isv/source/"
    private const val PATH_MAIN = "${PATH_SOURCE}controller/isv/"

    // Login - https://ivs.cittis.com.co/isv/source/plugins/login/signin.php
    const val URL_CHECK_SIGIN = "${PATH_SOURCE}plugins/login/signin.php"

}