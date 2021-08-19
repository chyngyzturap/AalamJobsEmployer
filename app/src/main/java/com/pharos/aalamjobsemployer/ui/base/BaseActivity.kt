package com.pharos.aalamjobsemployer.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.pharos.aalamjobsemployer.data.local.prefs.UserPreferences
import com.pharos.aalamjobsemployer.data.network.AuthApi
import com.pharos.aalamjobsemployer.data.network.RemoteDataSource
import com.pharos.aalamjobsemployer.data.repository.BaseRepository
import com.pharos.aalamjobsemployer.ui.auth.AuthActivity
import com.pharos.aalamjobsemployer.utils.startNewActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<VM : ViewModel, B : ViewBinding, R : BaseRepository> :
    AppCompatActivity(),
    CoroutineScope {
    private lateinit var job: Job
    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    protected val remoteDataSource = RemoteDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        userPreferences = UserPreferences(this)
        binding = getActivityBinding(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory(getActivityRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
        lifecycleScope.launch { userPreferences.tokenAccess.first() }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getActivityBinding(inflater: LayoutInflater): B

    abstract fun getActivityRepository(): R


}