package com.zxventures.zxapp.presenter.home.contract

import com.zxventures.zxapp.base.mvp.BaseMVPPresenter
import com.zxventures.zxapp.base.mvp.BaseMVPView
import com.zxventures.zxapp.model.PointOfContactData

/**
 * Created by rodrigosimoesrosa
 */
object HomeContract {

    interface HomeView : BaseMVPView {
        fun showPointsOfContact(points: MutableList<PointOfContactData.PointOfContact>)
        fun showError(e: Exception)
        fun showEmptyState(msg: String)
    }

    interface HomePresenter : BaseMVPPresenter<HomeView> {
        fun loadPocsByAddress(lat: String, lng: String)
        fun cancel()
    }

}