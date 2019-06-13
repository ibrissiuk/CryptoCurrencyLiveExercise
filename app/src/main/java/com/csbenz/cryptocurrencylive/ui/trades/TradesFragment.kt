package com.csbenz.cryptocurrencylive.ui.trades

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csbenz.cryptocurrencylive.Constants
import com.csbenz.cryptocurrencylive.R
import com.csbenz.cryptocurrencylive.network.ConnectivityReceiver
import com.csbenz.cryptocurrencylive.utils.Utils
import kotlinx.android.synthetic.main.fragment_trades.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONTokener

class TradesFragment : Fragment(), ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var mContext: Context
    private lateinit var websocket: WebSocket
    private lateinit var tradesAdapter: TradesAdapter


    private lateinit var pairName: String

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        pairName = arguments!!.getString(Constants.PAIR_NAME_BUNDLE_ID)

        return inflater.inflate(R.layout.fragment_trades, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tradesAdapter = TradesAdapter(mContext)
        rv_trades.layoutManager = LinearLayoutManager(activity)
        rv_trades.adapter = tradesAdapter

    }

    override fun onResume() {
        super.onResume()

        ConnectivityReceiver.tradesConnectivityReceiverListener = this
    }

    override fun onStop() {
        super.onStop()

        stopWebsocket()
    }

    private fun startWebsocket() {
        val client = OkHttpClient()
        val request = Request.Builder().url(Constants.BITFINEX_WEBSOCKET_URL).build()
        val listener = TradesWebSocketListener(pairName)

        websocket = client.newWebSocket(request, listener)

        client.dispatcher().executorService().shutdown()
    }

    private fun stopWebsocket() {
        websocket.close(1000, "closing")
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            startWebsocket()
        } else {
            stopWebsocket()
        }
    }

    fun parseNewUpdate(jsonArray: JSONArray) {

        when (JSONTokener(jsonArray[1].toString()).nextValue()) {
            is JSONArray -> { // is the initial snapshot

                val tradesJSON: JSONArray = jsonArray[1] as JSONArray
                val trades: ArrayList<Triple<Long, Double, Double>> = ArrayList()

                for (i in 0 until tradesJSON.length()) {
                    val e: JSONArray = tradesJSON[i] as JSONArray
                    trades.add(Triple(e.getLong(1), e.getDouble(3), e.getDouble(2)))
                }

                trades.sortBy { e -> e.first }
                tradesAdapter.addTrades(trades.reversed())

            }
            is String -> { // is an update

                if (jsonArray[1].toString() == "hb") {
                    // ignore
                } else if (jsonArray[1].toString() == "te") {
                    val trade: JSONArray = jsonArray[2] as JSONArray
                    tradesAdapter.addTrade(Triple(trade.getLong(1), trade.getDouble(3), trade.getDouble(2)))
                }
            }
        }
    }


    private inner class TradesWebSocketListener(val pairName: String) : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send(Utils.createTradesRequest(pairName))
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            when (JSONTokener(text).nextValue()) {
                is JSONArray -> {

                    parseNewUpdate(JSONArray(text))
                }
                else -> {
                    // something else, ignore
                }
            }
        }


        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            super.onClosing(webSocket, code, reason)
            webSocket?.close(1000, "closing")
        }

    }
}