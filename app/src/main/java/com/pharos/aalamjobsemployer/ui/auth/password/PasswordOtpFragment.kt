package com.pharos.aalamjobsemployer.ui.auth.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.network.AuthApi
import com.pharos.aalamjobsemployer.data.repository.AuthRepository
import com.pharos.aalamjobsemployer.databinding.FragmentPasswordOtpBinding
import com.pharos.aalamjobsemployer.ui.auth.AuthViewModel
import com.pharos.aalamjobsemployer.ui.base.BaseFragment
import com.pharos.aalamjobsemployer.utils.visible
import kotlinx.android.synthetic.main.fragment_otp.*

class PasswordOtpFragment : BaseFragment<AuthViewModel, FragmentPasswordOtpBinding, AuthRepository>() {

    lateinit var auth: FirebaseAuth
    private var inputText: String? = ""
    private var phone: String? = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressbar.visible(false)

        inputText = arguments?.getString("storedVerificationId")
        phone = arguments?.getString("phone")

        binding.buttonValidate.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(
                inputText!!,
                binding.pinviewOtp.text.toString()
            )
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
               val bundle = Bundle()
              bundle.putString("phone", phone)
                    findNavController().navigate(R.id.action_passwordOtpFragment_to_newPasswordFragment, bundle)
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(), getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentPasswordOtpBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository (
        remoteDataSource.buildApiWithoutToken(AuthApi::class.java), userPreferences
    )
}