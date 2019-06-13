package com.csbenz.cryptocurrencylive.ui.pairs.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.csbenz.cryptocurrencylive.R
import com.csbenz.cryptocurrencylive.ui.pairs.presenter.PairPresenter
import com.csbenz.cryptocurrencylive.ui.pairs.view.PairItemViewHolder

class PairAdapter(val presenter: PairPresenter, val context: Context) : RecyclerView.Adapter<PairItemViewHolder>() {

    override fun getItemCount(): Int {
        return presenter.getPairsRowCount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairItemViewHolder {
        return PairItemViewHolder(presenter, LayoutInflater.from(context).inflate(R.layout.pair_item, parent, false))
    }

    override fun onBindViewHolder(holder: PairItemViewHolder, position: Int) {
        presenter.onBindPairsRowViewAtPosition(position, holder)
    }
}