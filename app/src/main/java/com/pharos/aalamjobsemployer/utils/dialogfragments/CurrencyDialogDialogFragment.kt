package com.pharos.aalamjobsemployer.utils.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.data.responses.dialog.CityResponseItem
import com.pharos.aalamjobsemployer.data.responses.dialog.CurrencyResponse
import com.pharos.aalamjobsemployer.data.responses.dialog.CurrencyResponseItem
import com.pharos.aalamjobsemployer.databinding.LayoutCountryBinding
import com.pharos.aalamjobsemployer.ui.base.BaseDialogFragment
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import com.pharos.aalamjobsemployer.ui.talents.utils.CurrencyListener
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CurrencyDialogDialogFragment(private val listener: SearchListener):BaseDialogFragment<JobViewModel, LayoutCountryBinding, JobRepository>(),
    CurrencyListener, CurrencyAdapter.CurrencyClickListener {

    private var cityList = mutableListOf<CityResponseItem>()
    private var currencyList = mutableListOf<CurrencyResponseItem>()
    private lateinit var currencyAdapter: CurrencyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.setCurrencyListener(this)
        viewModel.getCurrencyList()

    }


    override fun getViewModel()= JobViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= LayoutCountryBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): JobRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(JobApi::class.java, token)
        val api = remoteDataSource.buildApi(JobApi::class.java, token)

        if (token.isNullOrEmpty()){
            return JobRepository(apiNoToken )
        } else {
            return JobRepository(api)
        }
    }

    override fun setCurrency(currency: CurrencyResponse) {
        currencyList.addAll(currency)
        currencyAdapter = CurrencyAdapter(this)
        binding.rvCountry.adapter = currencyAdapter
        currencyAdapter.submitList(currencyList)
    }

    override fun getCurrencyError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()

    }

    override fun onCurrencyCLick(position: Int) {
        listener.getCurrencySign(currencyList[position].id, currencyList[position].sign)
        dismiss()
    }
}