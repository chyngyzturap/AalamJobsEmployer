package com.pharos.aalamjobsemployer.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.network.AuthApi
import com.pharos.aalamjobsemployer.data.repository.AuthRepository
import com.pharos.aalamjobsemployer.data.responses.UserResponse
import com.pharos.aalamjobsemployer.databinding.FragmentProfileBinding
import com.pharos.aalamjobsemployer.ui.auth.AuthViewModel
import com.pharos.aalamjobsemployer.ui.auth.utils.UserListener
import com.pharos.aalamjobsemployer.ui.base.BaseFragment
import com.pharos.aalamjobsemployer.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileFragment : BaseFragment<AuthViewModel, FragmentProfileBinding, AuthRepository>(),
    UserListener {
    private var companyName = ""
    private var userResponse: UserResponse? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cleanSearchIntents()
        requireActivity().nav_bottom.visible(true)
        viewModel.setUserListener(this)
        viewModel.getProfileInfo()

        binding.btnAboutCompany.setOnClickListener {
            val bundle = Bundle()
            userResponse?.id?.let { it1 ->
                bundle.putInt("idOfCompany", it1)
                findNavController().navigate(R.id.companyInfoFragment, bundle)
            }
        }

        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_settingsFragment)
        }

        binding.ivEdit.setOnClickListener {
            recreate(requireActivity())
            findNavController().navigate(R.id.action_nav_profile_to_editProfileFragment)
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): AuthRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(AuthApi::class.java, token)
        return AuthRepository(api, userPreferences)
    }

    override fun setUserData(userResponse: UserResponse) {
        initUserData(userResponse)
    }

    private fun cleanSearchIntents() {
        val countryIntent = requireActivity().intent.getIntExtra("salary", 0)
        val cityIntent = requireActivity().intent.getIntExtra("currencyId", 0)
        val sectorIntent = requireActivity().intent.getIntExtra("emplId", 0)
        if (countryIntent != 0 || cityIntent != 0 || sectorIntent != 0) {
            requireActivity().intent.removeExtra("salary")
            requireActivity().intent.removeExtra("currencyId")
            requireActivity().intent.removeExtra("emplId")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initUserData(userResponse: UserResponse) {
        if (userResponse.city != null && userResponse.country != null
            && userResponse.city != "" && userResponse.country != ""
        ) {
            binding.tvLocation.text =
                userResponse.city.toString() + ", " + userResponse.country.toString()
        } else {
            binding.tvLocation.visible(false)
        }
        if (userResponse.organization != null) {
            if (userResponse.organization.name != "") {
                companyName = " - " + userResponse.organization.name
            }
            if (userResponse.organization.name != null) {
                binding.profileTvPosition.text = userResponse.position + companyName
            }
        } else {
            if (userResponse.position != "" && userResponse.position != null) {
                binding.profileTvPosition.text = userResponse.position
            } else {
                binding.profileTitleContainer.visible(false)
            }
        }
        if (userResponse.username != "") {
            binding.profileTvPhone.text = userResponse.username
        } else {
            binding.profilePhoneContainer.visible(false)
        }
        if (userResponse.email != "") {
            binding.profileTvEmail.text = userResponse.email
        } else {
            binding.profileEmailContainer.visible(false)
        }
        if (userResponse.fullname != "") {
            binding.tvFullName.text = userResponse.fullname
        } else {
            binding.tvFullName.visible(false)
        }
        if (userResponse.address != "" && userResponse.address != null) {
            binding.profileTvAddress.text = userResponse.address
        } else {
            binding.profileAddressContainer.visible(false)
        }

        lifecycleScope.launch {
            viewModel.saveUser(
                userResponse.email, userResponse.id, userResponse.username,
                userResponse.photo, userResponse.city, userResponse.country, userResponse.position,
                userResponse.fullname
            )
        }

        if (userResponse.photo != "")
            Glide.with(binding.root).load(userResponse.photo)
                .into(binding.ivProfilePhoto)
    }

    override fun dataError(code: Int?) {
    }

}