package com.pharos.aalamjobsemployer.utils.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.data.responses.dialog.SectorResponse
import com.pharos.aalamjobsemployer.data.responses.dialog.SpecResponseItem
import com.pharos.aalamjobsemployer.databinding.LayoutCountryBinding
import com.pharos.aalamjobsemployer.ui.base.BaseDialogFragment
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import com.pharos.aalamjobsemployer.ui.talents.search.sector.SpecAdapter
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import com.pharos.aalamjobsemployer.ui.talents.utils.SpecListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SpecDialog2DialogFragment(private val listener: SearchListener) :
    BaseDialogFragment<JobViewModel, LayoutCountryBinding, JobRepository>(),
    SpecListener, SpecAdapter.SectorClickListener {

    private var specList = mutableListOf<SpecResponseItem>()
    private lateinit var specAdapter: SpecAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setSpecListener(this)
        viewModel.getSpecList()
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

    override fun setSpec(jobs: SectorResponse) {
        specList.addAll(jobs)
        specAdapter = SpecAdapter(this)
        binding.rvCountry.adapter = specAdapter
        specAdapter.submitList(specList)
    }

    override fun getSpecError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()

    }

    override fun onSectorClick(position: Int) {
        listener.getSpecId(specList[position].id, specList[position].name)
        dismiss()
    }
}