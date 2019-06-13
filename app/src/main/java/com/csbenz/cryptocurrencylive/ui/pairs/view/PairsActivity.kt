package com.csbenz.cryptocurrencylive.ui.pairs.view

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.csbenz.cryptocurrencylive.Constants
import com.csbenz.cryptocurrencylive.R
import com.csbenz.cryptocurrencylive.ui.details.DetailsActivity
import com.csbenz.cryptocurrencylive.ui.pairs.adapter.PairAdapter
import com.csbenz.cryptocurrencylive.ui.pairs.data.PairData
import com.csbenz.cryptocurrencylive.ui.pairs.presenter.PairPresenter
import kotlinx.android.synthetic.main.activity_pairs.*

class PairsActivity : AppCompatActivity(), PairsView {

    private lateinit var presenter: PairPresenter

    private lateinit var pairAdapter: PairAdapter
    lateinit var noNetworkSnackbar: Snackbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pairs)

        presenter = PairPresenter(this, ArrayList())

        prepareAdapter()
        presenter.fetchPairs(PairData(this))
    }

    private fun prepareAdapter() {
        pairAdapter = PairAdapter(presenter, this)

        rv_pairs.layoutManager = LinearLayoutManager(this)
        rv_pairs.adapter = pairAdapter
    }

    override fun showPairs(pairs: List<String>) {
        presenter.pairs = pairs
        pairAdapter.notifyDataSetChanged()
    }

    override fun displayNoNetworkSnackbar(errorText: String) {
        noNetworkSnackbar = Snackbar.make(pairs_root_layout, errorText, Snackbar.LENGTH_INDEFINITE)
        noNetworkSnackbar.setAction(getString(R.string.retry_network), {
            noNetworkSnackbar.dismiss()
            presenter.fetchPairs(PairData(this))
        })
        noNetworkSnackbar.show()
    }

    override fun openDetailsActivity(pairName: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(Constants.PAIR_NAME_BUNDLE_ID, pairName)
        startActivity(intent)
    }

}
