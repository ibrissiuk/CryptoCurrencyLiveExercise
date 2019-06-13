package com.csbenz.cryptocurrencylive.utils

import org.json.JSONArray
import org.json.JSONObject

object Utils {

    val SUMMARY_LOW__KEY = "low"
    val SUMMARY_HIGH_KEY = "high"
    val SUMMARY_VOLUME_LEY = "volume"
    val SUMMARY_LAST_PRICE_KEY = "last_price"

    fun unJsonSummary(summary: String): String {
        val jsonObject = JSONObject(summary)

        val lastPrice = jsonObject.get(SUMMARY_LAST_PRICE_KEY)
        val high = jsonObject.get(SUMMARY_HIGH_KEY)
        val low = jsonObject.get(SUMMARY_LOW__KEY)
        val volume = jsonObject.get(SUMMARY_VOLUME_LEY)

        return "$lastPrice\nVOL: $volume\nLOW: $low   HIGH: $high"
    }

    fun jsonArrayToList(jsonArray: JSONArray): ArrayList<String> {
        val l: ArrayList<String> = ArrayList()

        for (i in 0 until jsonArray.length()) {
            val e: String = jsonArray[i].toString()
            l.add(e)
        }

        return l
    }

    fun createBookRequest(pairName: String): String {
        val request = "{\n" +
                "   \"event\":\"subscribe\",\n" +
                "   \"channel\":\"book\",\n" +
                "   \"pair\":\"" + pairName + "\",\n" +
                "   \"prec\":\"P0\",\n" +
                "   \n" +
                "   \"freq\":\"F0\",\n" +
                "   \"length\":\"25\"\n" +
                "}"

        return request
    }

    fun createTradesRequest(pairName: String): String {
        val request = "{\n" +
                "  \"event\": \"subscribe\",\n" +
                "  \"channel\": \"trades\",\n" +
                "  \"pair\": \"" + pairName + "\"\n" +
                "}"

        return request
    }

}