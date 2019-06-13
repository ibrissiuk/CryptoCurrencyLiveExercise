package com.csbenz.cryptocurrencylive.ui.pairs.data

import android.content.Context
import com.csbenz.cryptocurrencylive.Constants
import com.csbenz.cryptocurrencylive.R
import com.csbenz.cryptocurrencylive.network.NetworkUtils
import com.csbenz.cryptocurrencylive.utils.Utils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.json.JSONArray
import java.io.FileNotFoundException
import java.net.URL

open class PairData(private val context: Context) {

    open fun fetchPairs(dataLoaderListener: DataLoaderListener) {
        if (NetworkUtils.isNetworkAvailable(context)) {

            doAsync {
                try {
                    val response = URL(Constants.BITFINEX_PAIR_LIST_URL).readText()
                    context.runOnUiThread {
                        val pairList = Utils.jsonArrayToList(JSONArray(response))

                        dataLoaderListener.loadingPairsSuccess(pairList)
                    }
                } catch (e: FileNotFoundException) {
                    dataLoaderListener.loadingPairsFailure(context.getString(R.string.server_unavailable))
                }

            }
        } else {
            dataLoaderListener.loadingPairsFailure(context.getString(R.string.retry_network))
        }
    }

    interface DataLoaderListener {
        fun loadingPairsSuccess(pairs: ArrayList<String>)
        fun loadingPairsFailure(errorText: String)
    }
}