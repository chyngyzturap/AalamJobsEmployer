package com.pharos.aalamjobsemployer.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pharos.aalamjobsemployer.R
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        iv_backpressed.setOnClickListener {
            onBackPressed()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentAuth) as NavHostFragment
        val navController: NavController = navHostFragment.navController
    }
}