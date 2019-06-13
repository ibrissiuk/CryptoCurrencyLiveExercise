package com.csbenz.cryptocurrencylive.ui.details

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.csbenz.cryptocurrencylive.Constants
import com.csbenz.cryptocurrencylive.R
import com.csbenz.cryptocurrencylive.network.ConnectivityReceiver
import com.csbenz.cryptocurrencylive.network.NetworkUtils
import com.csbenz.cryptocurrencylive.utils.Utils
import kotlinx.android.synthetic.main.activity_details.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL


class DetailsActivity : AppCompatActivity() {


    private lateinit var pairName: String
    private lateinit var noNetworkSnackbar: Snackbar
    private lateinit var connectivityReceiver: ConnectivityReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)


        pairName = intent.getStringExtra(Constants.PAIR_NAME_BUNDLE_ID)
        (this as AppCompatActivity).supportActionBar?.title = pairName

        val fragmentAdapter = DetailsPagerAdapter(supportFragmentManager, pairName)
        vp_details.adapter = fragmentAdapter

        tabs_details.setupWithViewPager(vp_details)
    }

    override fun onStart() {
        super.onStart()

        connectivityReceiver = ConnectivityReceiver()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        fetchSummary()
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(connectivityReceiver)
    }

    private fun fetchSummary() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            doAsync {
                val result = URL(createSummaryUrl(pairName)).readText()
                uiThread {
                    displaySummary(result)
                }
            }
        } else {
            noNetworkSnackbar = Snackbar.make(details_root_layout, getString(R.string.network_unavailable), Snackbar.LENGTH_INDEFINITE)
            noNetworkSnackbar.setAction(getString(R.string.retry_network), {
                noNetworkSnackbar.dismiss()
                fetchSummary()
            })
            noNetworkSnackbar.show()

        }
    }

    private fun displaySummary(summary: String) {
        tv_summary.text = Utils.unJsonSummary(summary)
    }


    private fun createSummaryUrl(pairName: String): String {
        return Constants.BITFINEX_PAIR_SUMMARY_URL_PREFIX + pairName
    }

}