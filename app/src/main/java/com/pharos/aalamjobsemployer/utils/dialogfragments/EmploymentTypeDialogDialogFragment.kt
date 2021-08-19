package com.pharos.aalamjobsemployer.utils.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.data.responses.dialog.EmploymentTypeResponse
import com.pharos.aalamjobsemployer.data.responses.dialog.EmploymentTypeResponseItem
import com.pharos.aalamjobsemployer.databinding.LayoutCountryBinding
import com.pharos.aalamjobsemployer.ui.base.BaseDialogFragment
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import com.pharos.aalamjobsemployer.ui.talents.utils.EmplTypeListener
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class EmploymentTypeDialogDialogFragment(private val listener: SearchListener) :
    BaseDialogFragment<JobViewModel, LayoutCountryBinding, JobRepository>(),
    EmplTypeListener, EmploymentTypeAdapter.EmplTypeClickListener {

    private var emplTypeList = mutableListOf<EmploymentTypeResponseItem>()
    private lateinit var emplTypeAdapter: EmploymentTypeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setEmplTypesListener(this)
        viewModel.getEmploymentTypes()
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

    override fun setEmplTypes(emplTypes: EmploymentTypeResponse) {
        emplTypeList.addAll(emplTypes)
        emplTypeAdapter = EmploymentTypeAdapter(this)
        binding.rvCountry.adapter = emplTypeAdapter
        emplTypeAdapter.submitList(emplTypeList)
    }

    override fun getEmplTypesError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onEmplTypeClick(position: Int) {
        listener.getEmplTypeId(emplTypeList[position].id, emplTypeList[position].name.en)
        dismiss()
    }
}