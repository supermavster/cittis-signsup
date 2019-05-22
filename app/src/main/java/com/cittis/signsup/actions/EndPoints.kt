package com.cittis.signsup.actions


object EndPoints {

    @kotlin.jvm.JvmField
    var FireBasePath: String = "locations"
    // Users
    var FireBaseID = ""

    /** Server **/
    // Server Main 172.20.1.14 172.20.1.19 192.168.43.137 172.20.1.13
    private const val SERVER_MAIN = "localhost"//""ivs.cittis.com.co"
    private const val PATH_SOURCE = "http://$SERVER_MAIN/"
    private const val PATH_API = "${PATH_SOURCE}app?"
    private const val PATH_MAIN = "${PATH_SOURCE}controller/isv/"

    /** Check Login */
    const val URL_CHECK_SIGNIN = "${PATH_SOURCE}plugins/login/signin.php"

    /** Get Components */
    // Get All Municipalities
    const val URL_GET_MUNICIPALITIES = "${PATH_API}municipalities=all"

    // Get All Departments
    const val URL_GET_DEPARTMENTS = "${PATH_API}departments=all"

    // Get Specific Departments
    private const val URL_GET_SPECIFIC_DEPARTMENT = "${PATH_API}departments=" //boyaca

    fun URL_GET_SPECIFIC_DEPARTMENT(department: String): String {
        return URL_GET_SPECIFIC_DEPARTMENT + department
    }

    // Get All Municipalities BY Department
    private const val URL_GET_MUNICIPALITIES_BY_DEPARTMENTS = "${PATH_API}municipios="//boyaca

    fun URL_GET_MUNICIPALITIES_BY_DEPARTMENTS(department: String): String {
        return URL_GET_MUNICIPALITIES_BY_DEPARTMENTS + department
    }

    // Get Departments By ID Firebase
    private const val URL_GET_DEPARTMENTS_BY_IDFIREBASE = "${PATH_API}departments="//boyaca

    fun URL_GET_DEPARTMENTS_BY_IDFIREBASE(department: String, idFirebase: String): String {
        return "$URL_GET_DEPARTMENTS_BY_IDFIREBASE$department&idFirebase=$idFirebase"//LJCpaE6xqmTOmtsKXJp6hnnya2d2
    }

    // Get Municipalities By ID Firebase
    private const val URL_GET_MUNICIPALITIES_BY_IDFIREBASE = "${PATH_API}municipios="//boyaca

    fun URL_GET_MUNICIPALITIES_BY_IDFIREBASE(municipios: String, idFirebase: String): String {
        return "$URL_GET_MUNICIPALITIES_BY_IDFIREBASE$municipios&idFirebase=$idFirebase"//LJCpaE6xqmTOmtsKXJp6hnnya2d2
    }

    // Count Inventory By idSignal and idProject
    private const val URL_GET_COUNT_INVENTORY = "${PATH_API}count=inventario"//&idSignal=1&idProject=1

    fun URL_GET_COUNT_INVENTORY(idSignal: String, idProject: String): String {
        return "$URL_GET_COUNT_INVENTORY&idSignal=$idSignal&idProject=$idProject"//1
    }

}