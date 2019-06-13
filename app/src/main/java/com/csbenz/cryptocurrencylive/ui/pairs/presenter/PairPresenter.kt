package com.csbenz.cryptocurrencylive.ui.pairs.presenter

import com.csbenz.cryptocurrencylive.ui.pairs.view.PairRowView
import com.csbenz.cryptocurrencylive.ui.pairs.view.PairsView
import com.csbenz.cryptocurrencylive.ui.pairs.data.PairData

class PairPresenter(val view: PairsView, var pairs: List<String>) {


    fun onBindPairsRowViewAtPosition(pos: Int, pairsRowView: PairRowView) {
        pairsRowView.setTitle(pairs[pos])
    }

    fun getPairsRowCount(): Int {
        return pairs.size
    }

    fun onItemClick(pos: Int) {
        view.openDetailsActivity(pairs[pos])
    }

    fun fetchPairs(pairData: PairData) {
        pairData.fetchPairs(object : PairData.DataLoaderListener {
            override fun loadingPairsSuccess(pairs: ArrayList<String>) {
                view.showPairs(pairs)
            }

            override fun loadingPairsFailure(errorText: String) {
                view.displayNoNetworkSnackbar(errorText)
            }
        })
    }


}