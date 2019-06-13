package com.csbenz.cryptocurrencylive.ui.pairs.view

import android.support.v7.widget.RecyclerView
import android.view.View
import com.csbenz.cryptocurrencylive.ui.pairs.presenter.PairPresenter
import kotlinx.android.synthetic.main.pair_item.view.*

class PairItemViewHolder(private val presenter: PairPresenter, val view: View) : RecyclerView.ViewHolder(view), PairRowView, View.OnClickListener {

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        presenter.onItemClick(adapterPosition)
    }

    override fun setTitle(title: String) = with(view) {
        tv_pair_name.text = title
    }
}