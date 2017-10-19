package com.zxventures.zxapp.base.mvp

/**
 * Created by rodrigosimoesrosa
 */
interface BaseMVPPresenter<in V : BaseMVPView> {
    fun attachView(view: V)
    fun detachView()
}