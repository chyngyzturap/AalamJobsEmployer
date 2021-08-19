package com.pharos.aalamjobsemployer.ui.job.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobsemployer.data.local.db.cv.entities.CompanyInfo
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.databinding.LayoutAddCompanyBinding
import com.pharos.aalamjobsemployer.ui.base.BaseDialogFragment
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddCompanyDialogDialogFragment :
    BaseDialogFragment<JobViewModel, LayoutAddCompanyBinding, JobRepository>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddCompany.setOnClickListener {
            findNavController().navigate(R.id.companyInfoFragment)
            dismiss()
        }

        binding.btnAlreadyHave.setOnClickListener {
            val company = CompanyInfo(12)

            launch {
                context?.let {
                    CvDatabase(it).getCompanyDao()
                        .insertCompanyInfo(company)
                    dismiss()
                }
            }
        }
    }

    override fun getViewModel() = JobViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = LayoutAddCompanyBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): JobRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(JobApi::class.java, token)
        val api = remoteDataSource.buildApi(JobApi::class.java, token)
        if (token.isNullOrEmpty()) {
            return JobRepository(apiNoToken)
        } else {
            return JobRepository(api)
        }
    }
}