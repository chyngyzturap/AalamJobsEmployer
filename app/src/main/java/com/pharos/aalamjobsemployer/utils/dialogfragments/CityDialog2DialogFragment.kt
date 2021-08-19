package com.pharos.aalamjobsemployer.utils.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.data.responses.dialog.CityResponse
import com.pharos.aalamjobsemployer.data.responses.dialog.CityResponseItem
import com.pharos.aalamjobsemployer.databinding.LayoutCityBinding
import com.pharos.aalamjobsemployer.ui.base.BaseDialogFragment
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import com.pharos.aalamjobsemployer.ui.talents.search.location.CityAdapter
import com.pharos.aalamjobsemployer.ui.talents.utils.CityListener
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CityDialog2DialogFragment(private val listener: SearchListener) :
    BaseDialogFragment<JobViewModel, LayoutCityBinding, JobRepository>(),
    CityListener,
    CityAdapter.CityClickListener {

    private var cityList = mutableListOf<CityResponseItem>()
    private lateinit var cityAdapter: CityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCityListener(this)
        viewModel.getCitiesList()
    }

    override fun getViewModel() = JobViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = LayoutCityBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): JobRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(JobApi::class.java, token)
        return JobRepository(api)
    }

    override fun setCities(jobs: CityResponse) {
        cityList.addAll(jobs)
        cityAdapter = CityAdapter(this)
        binding.rvCountry.adapter = cityAdapter
        cityAdapter.submitList(cityList)
    }

    override fun getCityError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()

    }

    override fun onCityClick(position: Int) {
        listener.getCityId(cityList[position].id, cityList[position].name.en)
        dismiss()
    }
}