package com.pharos.aalamjobsemployer.utils.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.pharos.aalamjobsemployer.data.network.AuthApi
import com.pharos.aalamjobsemployer.data.repository.AuthRepository
import com.pharos.aalamjobsemployer.databinding.LayoutLogOutBinding
import com.pharos.aalamjobsemployer.ui.auth.AuthViewModel
import com.pharos.aalamjobsemployer.ui.base.BaseDialogFragment
import com.pharos.aalamjobsemployer.ui.splash.SplashActivity
import com.pharos.aalamjobsemployer.utils.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LogOutDialogDialogFragment :
    BaseDialogFragment<AuthViewModel, LayoutLogOutBinding, AuthRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                userPreferences.clear()
            }
            requireActivity().startNewActivity(SplashActivity::class.java)
        }

        binding.btnCancel.setOnClickListener {
            requireActivity().onBackPressed()
            dismiss()
        }
    }

    override fun getViewModel() = AuthViewModel::class.java
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = LayoutLogOutBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): AuthRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(AuthApi::class.java, token)
        return AuthRepository(api, userPreferences)
    }
}