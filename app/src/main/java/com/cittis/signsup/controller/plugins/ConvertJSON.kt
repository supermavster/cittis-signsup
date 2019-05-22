package com.cittis.signsup.controller.plugins

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener


class ConvertJSON(json: String) : JSONObject(json) {
    val data = this.optJSONArray("data")
        ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } } // returns an array of JSONObject

}

internal object JsonUtil2 {

    /**
     * Returns a list of String objects from a JSONArray. This
     * does not do any kind of recursive unpacking of the array.
     * Thus, if the array includes other JSON arrays or JSON objects
     * their string representation will be a single item in the
     * returned list.
     *
     * @param jArray The JSONArray to convert.
     * @return A List of the String representation of each item in
     * the JSON array.
     * @throws JSONException if an element of jArray cannot be
     * converted to a String.
     */
    @Throws(JSONException::class)
    fun getStringListFromJsonArray(jArray: JSONArray): List<String> {
        val returnList = ArrayList<String>()
        for (i in 0 until jArray.length()) {
            val `val` = jArray.getString(i)
            returnList.add(`val`)
        }
        return returnList
    }

    /**
     * Returns a Java Object list of a JSONArray with each item in
     * the array converted using convertJsonItem().
     *
     * @param jArray The JSONArray to convert.
     * @return A List of Strings and more Object lists.
     * @throws JSONException if an element in jArray cannot be
     * converted properly.
     */
    @Throws(JSONException::class)
    fun getListFromJsonArray(jArray: JSONArray): List<Any> {
        val returnList = ArrayList<Any>()
        for (i in 0 until jArray.length()) {
            returnList.add(convertJsonItem(jArray.get(i)))
        }
        return returnList
    }

    /**
     * Returns a list containing one two item list per key in jObject.
     * Each two item list has the key String as its first element and
     * the result of calling convertJsonItem() on its value as the
     * second element. The sub-lists in the returned list will appear
     * in alphabetical order by key.
     *
     * @param jObject The JSONObject to convert.
     * @return A list of two item lists: [String key, Object value].
     * @throws JSONException if an element in jObject cannot be
     * converted properly.
     */
    @Throws(JSONException::class)
    fun getListFromJsonObject(jObject: JSONObject): List<Any> {
        val returnList = ArrayList<Any>()
        val keys = jObject.keys()

        val keysList = ArrayList<String>()
        while (keys.hasNext()) {
            keysList.add(keys.next())
        }
        keysList.sort()

        for (key in keysList) {
            val nestedList = ArrayList<Any>()
            nestedList.add(key)
            nestedList.add(convertJsonItem(jObject.get(key)))
            returnList.add(nestedList)
        }

        return returnList
    }

    /**
     * Returns a Java object representation of objects that are
     * encountered inside of JSON created using the org.json package.
     * JSON arrays and objects are transformed into their list
     * representations using getListFromJsonArray and
     * getListFromJsonObject respectively.
     *
     * Java Boolean values and the Strings "true" and "false" (case
     * insensitive) are inserted as Booleans. Java Numbers are
     * inserted without modification and all other values are inserted
     * as their toString(). value.
     *
     * @param o An item in a JSON array or JSON object to convert.
     * @return A Java Object representing o or the String "null"
     * if o is null.
     * @throws JSONException if o fails to parse.
     */
    @Throws(JSONException::class)
    fun convertJsonItem(o: Any?): Any {
        if (o == null) {
            return "null"
        }

        if (o is JSONObject) {
            return getListFromJsonObject((o as JSONObject?)!!)
        }

        if (o is JSONArray) {
            return getListFromJsonArray((o as JSONArray?)!!)
        }

        if (o == java.lang.Boolean.FALSE || o is String && o.equals("false", ignoreCase = true)) {
            return false
        }

        return if (o == java.lang.Boolean.TRUE || o is String && o.equals("true", ignoreCase = true)) {
            true
        } else o as? Number ?: o.toString()

    }

    @Throws(JSONException::class)
    fun getJsonRepresentation(value: Any?): String {
        if (value == null || value == null) {
            return "null"
        }
        /*
       * This has been commented out to prevent the need for the Kawa library. Do NOT use Fstring
       * or YailList in any of your data, otherwise this conversion won't work.
       *
      if (value instanceof FString) {
        return JSONObject.quote(value.toString());
      }
      if (value instanceof YailList) {
        return ((YailList) value).toJSONString();
      }
      */

        if (value is Number) {
            return JSONObject.numberToString(value as Number?)
        }
        if (value is Boolean) {
            return value.toString()
        }
        if (value.javaClass.isArray) {
            val sb = StringBuilder()
            sb.append("[")
            var separator = ""
            for (o in (value as Array<Any>?)!!) {
                sb.append(separator).append(getJsonRepresentation(o))
                separator = ","
            }
            sb.append("]")
            return sb.toString()
        }
        return (value as? String)?.toString() ?: JSONObject.quote(value.toString())
    }

    @Throws(JSONException::class)
    fun getObjectFromJson(jsonString: String): Any? {
        val value = JSONTokener(jsonString).nextValue()
        // Note that the JSONTokener may return a value equals() to null.
        if (value == null || value == null) {
            return null
        } else if (value is String ||
            value is Number ||
            value is Boolean
        ) {
            return value
        } else if (value is JSONArray) {
            return getListFromJsonArray(value)
        } else if (value is JSONObject) {
            return getListFromJsonObject(value)
        }
        throw JSONException("Invalid JSON string.")
    }
}
/**
 * Prevent instantiation.
 */
