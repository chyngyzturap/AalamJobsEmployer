package com.pharos.aalamjobsemployer.ui.job

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.ui.main.MainActivity
import com.pharos.aalamjobsemployer.utils.startNewActivity
import kotlinx.android.synthetic.main.activity_job.*

class JobActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentCV) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        iv_backpressed.setOnClickListener {
            onBackPressed()
        }
        auth_toolbar_title.setOnClickListener{
           startNewActivity(MainActivity::class.java)
        }
    }
}