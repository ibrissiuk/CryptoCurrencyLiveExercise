package com.csbenz.cryptocurrencylive.ui.trades


import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csbenz.cryptocurrencylive.R
import kotlinx.android.synthetic.main.trades_item.view.*
import org.jetbrains.anko.runOnUiThread
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue

class TradesAdapter(private val context: Context, private val trades: ArrayList<Triple<Long, Double, Double>> = ArrayList()) : RecyclerView.Adapter<TradesViewHolder>() {

    override fun getItemCount(): Int {
        return trades.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradesViewHolder {
        return TradesViewHolder(LayoutInflater.from(context).inflate(R.layout.trades_item, parent, false))
    }

    override fun onBindViewHolder(holder: TradesViewHolder, position: Int) {
        holder.bindItems(trades[position])
    }

    fun addTrades(newTrades: List<Triple<Long, Double, Double>>) {
        context.runOnUiThread {
            trades.clear()
            trades.addAll(newTrades)
            cutTrades()
            notifyDataSetChanged()
        }
    }

    fun addTrade(trade: Triple<Long, Double, Double>) {
        context.runOnUiThread {
            trades.add(0, trade)
            cutTrades()
            notifyDataSetChanged()
        }
    }

    // Make sure the number of trades to display isn't more than n
    private fun cutTrades(n: Int = 40) {
        if (trades.size > n) {
            val tmpTrades = trades.take(n)
            trades.clear()
            trades.addAll(tmpTrades)
        }
    }
}

class TradesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindItems(trade: Triple<Long, Double, Double>) = with(view) {
        tv_trades_time.text = getNiceTime(trade.first)
        tv_trades_price.text = trade.second.toString()
        tv_trades_amount.text = trade.third.absoluteValue.toString()

        tv_trades_price.setTextColor(if (trade.third > 0) Color.GREEN else Color.RED)

    }

    private fun getNiceTime(timestamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            e.toString()
        }
        // TODO not able to format exception handling
    }
}