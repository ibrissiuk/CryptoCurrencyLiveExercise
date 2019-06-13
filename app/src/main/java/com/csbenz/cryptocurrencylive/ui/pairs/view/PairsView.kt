package com.csbenz.cryptocurrencylive.ui.pairs.view

interface PairsView {

    fun openDetailsActivity(pairName: String)

    fun displayNoNetworkSnackbar(errorText: String)

    fun showPairs(pairs: List<String>)
}