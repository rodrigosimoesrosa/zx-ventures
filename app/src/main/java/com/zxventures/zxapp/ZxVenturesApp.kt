package com.zxventures.zxapp

import android.app.Application
import com.zxventures.zxapp.extensions.Delegate

/**
 * Created by rodrigosimoesrosa
 */
class ZxVenturesApp : Application() {

    companion object {
        var instance: ZxVenturesApp by Delegate.notNullValue()
        val name = "Rodrigo"
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}