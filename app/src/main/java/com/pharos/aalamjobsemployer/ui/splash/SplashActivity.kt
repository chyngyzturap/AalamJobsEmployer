package com.pharos.aalamjobsemployer.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.local.prefs.UserPreferences
import com.pharos.aalamjobsemployer.data.network.AuthApi
import com.pharos.aalamjobsemployer.data.repository.AuthRepository
import com.pharos.aalamjobsemployer.databinding.ActivitySplashBinding
import com.pharos.aalamjobsemployer.ui.auth.AuthActivity
import com.pharos.aalamjobsemployer.ui.auth.AuthViewModel
import com.pharos.aalamjobsemployer.ui.base.BaseActivity
import com.pharos.aalamjobsemployer.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class SplashActivity : BaseActivity<AuthViewModel, ActivitySplashBinding, AuthRepository>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showCurrent()

        btn_splash_log.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        lang_change.setOnClickListener {
            showChangeLang()
        }
    }

    private fun showCurrent() {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()) {
            setContentView(R.layout.activity_splash)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showChangeLang() {
        val langList = arrayOf("English", "Кыргызча", "Русский", "Türkçe")
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle(getString(R.string.choose_lang))
        mBuilder.setSingleChoiceItems(langList, -1) { dialog, which ->
            when (which) {
                0 -> {
                    lifecycleScope.launch {
                        setLocale("en")
                    }
                    recreate()
                }
                1 -> {
                    lifecycleScope.launch {
                        setLocale("ky")
                    }
                    recreate()
                }
                2 -> {
                    lifecycleScope.launch {
                        setLocale("ru")
                    }
                    recreate()
                }
                3 -> {
                    lifecycleScope.launch {
                        setLocale("tr")
                    }
                    recreate()
                }
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    @Suppress("DEPRECATION")
    private suspend fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = this.resources.configuration
        config.setLocale(locale)
        this.createConfigurationContext(config)
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
        viewModel.saveLang(lang)
        Log.d("ololo", lang)
        recreate()
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getActivityBinding(inflater: LayoutInflater) =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun getActivityRepository(): AuthRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(AuthApi::class.java, token)
        val api = remoteDataSource.buildApi(AuthApi::class.java, token)

        if (token.isNullOrEmpty()) {
            return AuthRepository(apiNoToken, userPreferences)
        } else {
            return AuthRepository(api, userPreferences)
        }
    }
}




