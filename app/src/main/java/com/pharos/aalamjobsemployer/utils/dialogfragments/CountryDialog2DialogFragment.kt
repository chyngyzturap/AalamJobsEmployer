package com.pharos.aalamjobsemployer.utils.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.data.responses.dialog.CityResponseItem
import com.pharos.aalamjobsemployer.data.responses.dialog.CountryResponse
import com.pharos.aalamjobsemployer.data.responses.dialog.CountryResponseItem
import com.pharos.aalamjobsemployer.databinding.LayoutCountryBinding
import com.pharos.aalamjobsemployer.ui.base.BaseDialogFragment
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import com.pharos.aalamjobsemployer.ui.talents.search.location.CountryAdapter
import com.pharos.aalamjobsemployer.ui.talents.utils.CountryListener
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CountryDialog2DialogFragment(private val listener: SearchListener) :
    BaseDialogFragment<JobViewModel, LayoutCountryBinding, JobRepository>(),
    CountryListener,
    CountryAdapter.CountryClickListener {

    private var countryList = mutableListOf<CountryResponseItem>()
    private lateinit var countryAdapter: CountryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCountryListener(this)
        viewModel.getCountryList()
    }

    override fun setCountry(jobs: CountryResponse) {

        countryList.addAll(jobs)
        countryAdapter = CountryAdapter(this)
        binding.rvCountry.adapter = countryAdapter
        countryAdapter.submitList(countryList)
    }

    override fun getCountryError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun getViewModel() = JobViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = LayoutCountryBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): JobRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(JobApi::class.java, token)
        return JobRepository(api)
    }

    override fun onCountryClick(position: Int) {
        listener.getCountryId(countryList[position].id, countryList[position].name.en)
        dismiss()
    }
}