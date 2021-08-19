package com.pharos.aalamjobsemployer.utils.dialogfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pharos.aalamjobsemployer.databinding.LayoutSignUpBinding
import com.pharos.aalamjobsemployer.ui.auth.AuthActivity

class SignUpDialogFragment: DialogFragment(){

    private var _binding: LayoutSignUpBinding? = null
    private val binding: LayoutSignUpBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGoSignUp  .setOnClickListener {
            val intent = Intent (requireContext(), AuthActivity::class.java)
            startActivity(intent)
            dismiss()
        }
    }
}