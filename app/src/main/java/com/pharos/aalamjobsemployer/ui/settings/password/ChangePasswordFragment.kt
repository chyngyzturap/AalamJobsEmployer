package com.pharos.aalamjobsemployer.ui.settings.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.model.ChangePasswordModel
import com.pharos.aalamjobsemployer.data.network.AuthApi
import com.pharos.aalamjobsemployer.data.repository.AuthRepository
import com.pharos.aalamjobsemployer.databinding.FragmentChangePasswordBinding
import com.pharos.aalamjobsemployer.ui.auth.AuthViewModel
import com.pharos.aalamjobsemployer.ui.base.BaseFragment
import com.pharos.aalamjobsemployer.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ChangePasswordFragment : BaseFragment<AuthViewModel, FragmentChangePasswordBinding, AuthRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)

        binding.btnDone.setOnClickListener {
            val currentPwd = binding.etCurrentPassword.text.toString().trim()
            val newPwd = binding.etNewPassword.text.toString().trim()
            val confirmPwd = binding.etConfirmNewPassword.text.toString().trim()
            val changePasswordModel = ChangePasswordModel(currentPwd, newPwd)
            if (newPwd == confirmPwd){
                viewModel.changePassword(changePasswordModel)
                binding.progressbar.visible(true)
                Toast.makeText(requireContext(), getString(R.string.pwd_changed_succ), Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.settingsFragment)
            } else {
                Toast.makeText(requireContext(), getString(R.string.pwd_mismatch), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentChangePasswordBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() : AuthRepository {
        val token = runBlocking {userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(AuthApi::class.java, token)
        return AuthRepository(api, userPreferences)
    }

}