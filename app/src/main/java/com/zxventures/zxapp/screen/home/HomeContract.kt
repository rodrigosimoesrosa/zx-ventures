package com.zxventures.zxapp.screen.home

import PocSearchMethodQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.zxventures.zxapp.R
import com.zxventures.zxapp.ZxVenturesApp
import com.zxventures.zxapp.api.ZxVentureAPI
import com.zxventures.zxapp.base.mvp.BaseMVPPresenter
import com.zxventures.zxapp.base.mvp.BaseMVPPresenterImpl
import com.zxventures.zxapp.base.mvp.BaseMVPView
import com.zxventures.zxapp.extensions.toISO8601
import com.zxventures.zxapp.model.PointOfContactData
import java.util.*

/**
 * Created by rodrigosimoesrosa
 */
object HomeContract {

    val instance = ZxVenturesApp.instance
    val NEAREST: String = instance.getString(R.string.nearest)

    private val EMPTY_DATA_POC = instance.getString(R.string.home_empty_data)

    interface HomeView : BaseMVPView {
        fun showPointsOfContact(points: MutableList<PointOfContactData.PointOfContact>)
        fun showError(e: Exception)
        fun showEmptyState(msg: String)
    }

    interface HomePresenter : BaseMVPPresenter<HomeView> {
        fun loadPocsByAddress(lat: String, lng: String)
        fun cancel()
    }

    class HomePresenterImpl : BaseMVPPresenterImpl<HomeView>(),
            HomePresenter {

        private var client: ApolloQueryCall<PocSearchMethodQuery.Data>? = null

        override fun cancel() {
            client?.cancel()
        }

        override fun loadPocsByAddress(lat: String, lng: String) {
            val apolloClient: ApolloClient = ApolloClient.builder()
                    .serverUrl(ZxVentureAPI.API)
                    .build()

            client = apolloClient.query(PocSearchMethodQuery.builder()
                    .lat(lat)
                    .lng(lng)
                    .algorithm(NEAREST)
                    .now(Date().toISO8601())
                    .build())

            client?.enqueue(object : ApolloCall.Callback<PocSearchMethodQuery.Data>() {

                override fun onResponse(response: Response<PocSearchMethodQuery.Data>) {
                    val points:MutableList<PointOfContactData.PointOfContact> = mutableListOf()
                    response.data()?.pocSearch()?.map {
                        val p = PointOfContactData.PointOfContact.build(it)
                        points.add(p)
                    }
                    if(points.isEmpty()){
                        view?.let { view -> call(view, EMPTY_DATA_POC, view::showEmptyState)}
                    }else{
                        view?.let { view -> call(view, points, view::showPointsOfContact) }
                    }
                }

                override fun onFailure(e: ApolloException) {
                    view?.let { view -> call(view, e, view::showError) }
                }
            })
        }
    }
}