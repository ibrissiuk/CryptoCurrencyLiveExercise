package com.csbenz.cryptocurrencylive

import com.csbenz.cryptocurrencylive.ui.pairs.data.PairData
import com.csbenz.cryptocurrencylive.ui.pairs.presenter.PairPresenter
import com.csbenz.cryptocurrencylive.ui.pairs.view.PairRowView
import com.csbenz.cryptocurrencylive.ui.pairs.view.PairsView
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class PairTest {

    lateinit var dataLoaderListener: PairData.DataLoaderListener
    lateinit var pairViewMock: PairsView
    lateinit var pairDataMock: PairData
    lateinit var pairModel: ArrayList<String>
    lateinit var presenter: PairPresenter
    lateinit var pairRowView: PairRowView

    @Before
    fun setUp() {
        dataLoaderListener = mock()
        pairViewMock = mock()
        pairDataMock = mock()
        pairModel = arrayListOf("a", "b", "c")
        presenter = PairPresenter(pairViewMock, pairModel)

        dataLoaderListener = object : PairData.DataLoaderListener {
            override fun loadingPairsSuccess(pairs: ArrayList<String>) {
                pairViewMock.showPairs(pairModel)
            }

            override fun loadingPairsFailure(errorText: String) {
                pairViewMock.displayNoNetworkSnackbar("")
            }
        }
    }

    @Test
    fun itemClickTest() {
        presenter.onItemClick(1)

        Mockito.verify(pairViewMock).openDetailsActivity("b")
    }

    @Test
    fun getPairsRowCountTest() {
        val size: Int = presenter.getPairsRowCount()
        assert(size == 3)
    }

    @Test
    fun onBindPairsRowViewAtPositionTest() {
        pairRowView = mock()
        presenter.onBindPairsRowViewAtPosition(1, pairRowView)

        verify(pairRowView).setTitle("b")
    }

    @Test
    fun pairFetchTest() {
        doAnswer { dataLoaderListener.loadingPairsSuccess(pairModel) }.`when`(pairDataMock).fetchPairs(dataLoaderListener)
        presenter.fetchPairs(pairDataMock)

        verify(pairViewMock).showPairs(pairModel)
    }

}