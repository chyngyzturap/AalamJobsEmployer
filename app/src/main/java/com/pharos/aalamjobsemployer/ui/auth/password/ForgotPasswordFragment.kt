package com.pharos.aalamjobsemployer.ui.auth.password

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.network.AuthApi
import com.pharos.aalamjobsemployer.data.repository.AuthRepository
import com.pharos.aalamjobsemployer.data.responses.LoginResponse
import com.pharos.aalamjobsemployer.databinding.FragmentForgotPasswordBinding
import com.pharos.aalamjobsemployer.ui.auth.AuthViewModel
import com.pharos.aalamjobsemployer.ui.auth.utils.LoginListener
import com.pharos.aalamjobsemployer.ui.base.BaseFragment
import com.pharos.aalamjobsemployer.utils.visible
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import okhttp3.ResponseBody
import java.util.concurrent.TimeUnit

class ForgotPasswordFragment :
    BaseFragment<AuthViewModel, FragmentForgotPasswordBinding, AuthRepository>(),
    LoginListener {
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressbar.visible(false)

        viewModel.setLoginListener(this)
        auth = FirebaseAuth.getInstance()

        binding.btnContinue.setOnClickListener {
            initUI()
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentForgotPasswordBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(
        remoteDataSource.buildApiWithoutToken(AuthApi::class.java), userPreferences
    )

    override fun isUserExists(available: Boolean) {
    }

    override fun signInFail(errorCode: ResponseBody?, code: Int?) {
    }

    override fun userDataSavedLogin() {
    }

    override fun loginSuccess(loginResponse: LoginResponse) {
    }

    override fun loginFail(code: Int?) {
    }

    private fun signInWithPhone(authCredential: PhoneAuthCredential) {
        val username = "+" + binding.etPhoneCode.selectedCountryCode.toString()
            .trim() + binding.etPhoneNumber.text.toString().trim()
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful)
                Log.d("RegisterRegularFragment", "signInwithPhone: success ${it.result}")
            else
                Log.d("RegisterRegularFragment", "signInwithPhone: failed ${it.exception}")
        }
    }

    private fun initCallbackClient() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhone(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressbar.visible(false)
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                Log.d("TAG", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                val phone = "+" + binding.etPhoneCode.selectedCountryCode.toString()
                    .trim() + binding.etPhoneNumber.text.toString().trim()
                val bundle = Bundle()
                bundle.putString("storedVerificationId", storedVerificationId)
                bundle.putString("phone", phone)
                findNavController().navigate(
                    R.id.action_forgotPasswordFragment_to_passwordOtpFragment,
                    bundle
                )
            }
        }
    }

    private fun getOtp() {
        val phone = "+" + binding.etPhoneCode.selectedCountryCode.toString()
            .trim() + binding.etPhoneNumber.text.toString().trim()
        if (phone.isNotEmpty()) {
            progressbar.visible(true)
            sendVerificationCode(phone)
        } else {
            progressbar.visible(false)
            Toast.makeText(
                requireContext(),
                getString(R.string.enter_mobile_number),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun initUI() {
        initCallbackClient()
        getOtp()
    }
}