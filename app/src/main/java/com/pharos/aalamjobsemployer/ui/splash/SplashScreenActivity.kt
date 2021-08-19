package com.pharos.aalamjobsemployer.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.utils.startNewActivity
import kotlinx.android.synthetic.main.activity_splashscreen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        iv_splash_logo.alpha = 0f
        iv_splash_logo.animate().setDuration(1500).alpha(1f).withEndAction{
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            startNewActivity(SplashActivity::class.java)
            finish()
            overridePendingTransition(0, 0)
        }
    }
}