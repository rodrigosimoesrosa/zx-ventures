package com.zxventures.zxapp.base.mvp

import android.os.Bundle
import com.zxventures.zxapp.base.BaseFragment

/**
 * Created by rodrigosimoesrosa
 */
abstract class BaseMVPFragment<in V : BaseMVPView, P : BaseMVPPresenter<V>>
    : BaseFragment(), BaseMVPView {

    abstract var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}