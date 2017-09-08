package com.zxventures.zxapp.mockup

import br.com.mirabilis.util.io.GsonUtil
import com.zxventures.zxapp.R
import com.zxventures.zxapp.ZxVenturesApp
import com.zxventures.zxapp.base.mvp.BaseMVPPresenterImpl
import com.zxventures.zxapp.model.PointOfContactData
import com.zxventures.zxapp.screen.home.HomeContract
import java.io.Serializable

/**
 * Created by rodrigosimoesrosa
 */
class HomePresenterImplMockup : BaseMVPPresenterImpl<HomeContract.HomeView>(),
        HomeContract.HomePresenter {

    val instance = ZxVenturesApp.instance

    override fun loadPocsByAddress(lat: String, lng: String) {
        view?.let { view -> call(view, GsonUtil.toSerialize(
                instance.resources.openRawResource(R.raw.home),
                PointOfContactMockup::class.java).pocSearch,
                view::showPointsOfContact)}
    }

    override fun cancel() {}

    data class PointOfContactMockup(val pocSearch: ArrayList<PointOfContactData.PointOfContact>) : Serializable
}