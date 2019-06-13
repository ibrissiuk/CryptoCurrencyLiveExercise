package com.csbenz.cryptocurrencylive.ui.details


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.csbenz.cryptocurrencylive.Constants
import com.csbenz.cryptocurrencylive.ui.orderbook.OrderBookFragment
import com.csbenz.cryptocurrencylive.ui.trades.TradesFragment


class DetailsPagerAdapter(fm: FragmentManager, val pairName: String) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        args.putString(Constants.PAIR_NAME_BUNDLE_ID, pairName)

        return when (position) {
            0 -> {
                val f = OrderBookFragment()
                f.arguments = args
                f
            }
            1 -> {
                val f = TradesFragment()
                f.arguments = args
                f
            }
            else -> {
                throw IllegalStateException("Should only have two tabs")
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Book"
            1 -> "Trades"
            else -> {
                throw IllegalStateException("Should have only two tabs")
            }
        }
    }
}