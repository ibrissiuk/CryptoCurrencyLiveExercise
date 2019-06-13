package com.csbenz.cryptocurrencylive.ui.orderbook


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csbenz.cryptocurrencylive.R
import kotlinx.android.synthetic.main.bid_order_item.view.*
import org.jetbrains.anko.runOnUiThread
import kotlin.math.absoluteValue

class OrderAdapter(private val context: Context, private val isBid: Boolean, private val orders: ArrayList<Triple<Double, Double, Int>> = ArrayList()) : RecyclerView.Adapter<OrderViewHolder>() {

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(LayoutInflater.from(context).inflate(if (isBid) R.layout.bid_order_item else R.layout.ask_order_item, parent, false))
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bindItems(orders[position])
    }

    fun setData(newOrders: List<Triple<Double, Double, Int>>) {
        context.runOnUiThread {
            orders.clear()
            orders.addAll(newOrders)
            notifyDataSetChanged()
        }
    }
}

class OrderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindItems(order: Triple<Double, Double, Int>) = with(view) {
        tv_order_price.text = order.first.toString()
        tv_order_amount.text = order.second.absoluteValue.toString()
        // tv_order_count.text = order.third.toString()
    }
}