package com.pharos.aalamjobsemployer.ui.applied

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pharos.aalamjobsemployer.R

class AppliedFragment : Fragment() {

    companion object {
        fun newInstance() = AppliedFragment()
    }

    private lateinit var viewModel: AppliedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AppliedViewModel::class.java)
        // TODO: Use the ViewModel
        cleanSearchIntents()
    }

    private fun cleanSearchIntents(){
        val countryIntent = requireActivity().intent.getIntExtra("salary", 0)
        val cityIntent = requireActivity().intent.getIntExtra("currencyId", 0)
        val sectorIntent = requireActivity().intent.getIntExtra("emplId", 0)
        if (countryIntent != 0 || cityIntent != 0 || sectorIntent != 0) {
            requireActivity().intent.removeExtra("salary")
            requireActivity().intent.removeExtra("currencyId")
            requireActivity().intent.removeExtra("emplId")
        }
    }

}