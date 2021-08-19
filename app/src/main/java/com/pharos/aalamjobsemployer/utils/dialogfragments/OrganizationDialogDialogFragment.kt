package com.pharos.aalamjobsemployer.utils.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobsemployer.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobsemployer.data.local.db.cv.entities.CompanyInfo
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.data.responses.dialog.OrganizationResponse
import com.pharos.aalamjobsemployer.data.responses.dialog.OrganizationResponseItem
import com.pharos.aalamjobsemployer.databinding.LayoutCountryBinding
import com.pharos.aalamjobsemployer.ui.base.BaseDialogFragment
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import com.pharos.aalamjobsemployer.ui.talents.utils.OrganizationListener
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class OrganizationDialogDialogFragment(private val listener: SearchListener) :
    BaseDialogFragment<JobViewModel, LayoutCountryBinding, JobRepository>(),
    OrganizationListener, OrganizationAdapter.OrganizationClickListener {

    private var orgList = mutableListOf<OrganizationResponseItem>()
    private lateinit var orgAdapter: OrganizationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setOrgListener(this)
        viewModel.getOrganizations()
        forNotShowDialog()
    }

    private fun forNotShowDialog() {
        launch {
            context?.let {
                val company = CompanyInfo(21)
                CvDatabase(it).getCompanyDao()
                    .insertCompanyInfo(company)
            }
        }
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

    override fun setOrganization(org: OrganizationResponse) {
        orgList.addAll(org)
        orgAdapter = OrganizationAdapter(this)
        binding.rvCountry.adapter = orgAdapter
        orgAdapter.submitList(orgList)
    }

    override fun getOrgError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()

    }

    override fun onOrganizationClick(position: Int) {
        listener.getOrgId(orgList[position].id, orgList[position].name)
        dismiss()
    }
}