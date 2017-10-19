package com.zxventures.zxapp.screen.splash

import android.os.Bundle
import com.zxventures.zxapp.R
import com.zxventures.zxapp.base.BaseActivity
import com.zxventures.zxapp.screen.home.HomeActivity
import java.util.*

/**
 * Created by rodrigosimoesrosa
 */
class SplashActivity : BaseActivity() {

    private val DELAY = 2000L

    override fun getLayout(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                startActivity(HomeActivity::class.java)
                finish()
            }
        },DELAY)
    }
}
