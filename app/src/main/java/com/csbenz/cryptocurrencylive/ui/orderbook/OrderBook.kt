package com.csbenz.cryptocurrencylive.ui.orderbook

import android.util.Pair
import java.util.*

class OrderBook(val book: TreeMap<Double, Pair<Double, Int>> = TreeMap()) {
    // Key is price, value is pair of amout-count

    fun processNewOrder(price: Double, count: Int, amount: Double) {
        if (count == 0) {
            removeOrder(price)
        } else {
            addOrder(price, count, amount)
        }
    }

    fun addOrder(price: Double, count: Int, amount: Double) {
        book[price] = Pair(amount, count)
    }

    fun removeOrder(price: Double) {
        book.remove(price)
    }

    fun getBidOrders(): List<Triple<Double, Double, Int>> {
        return toList().filter { o -> o.second > 0 }
    }

    fun getAskOrders(): List<Triple<Double, Double, Int>> {
        return toList().filter { o -> o.second < 0 }
    }

    private fun toList(): ArrayList<Triple<Double, Double, Int>> {
        val l: ArrayList<Triple<Double, Double, Int>> = ArrayList()

        for ((price, vs) in book) {
            l.add(Triple(price, vs.first, vs.second))
        }

        return l
    }
}