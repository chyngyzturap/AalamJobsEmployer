package com.pharos.aalamjobsemployer.utils.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.data.responses.dialog.PaymentTypeResponse
import com.pharos.aalamjobsemployer.data.responses.dialog.PaymentTypeResponseItem
import com.pharos.aalamjobsemployer.databinding.LayoutCountryBinding
import com.pharos.aalamjobsemployer.ui.base.BaseDialogFragment
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import com.pharos.aalamjobsemployer.ui.talents.utils.PayTypeListener
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class PaymentTypeDialogDialogFragment(private val listener: SearchListener) :
    BaseDialogFragment<JobViewModel, LayoutCountryBinding, JobRepository>(),
    PayTypeListener, PaymentTypeAdapter.PayTypeClickListener {

    private var payTypeList = mutableListOf<PaymentTypeResponseItem>()
    private lateinit var payTypeAdapter: PaymentTypeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setPayTypesListener(this)
        viewModel.getPaymentTypes()
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

    override fun setPayTypes(payTypes: PaymentTypeResponse) {
        payTypeList.addAll(payTypes)
        payTypeAdapter = PaymentTypeAdapter(this)
        binding.rvCountry.adapter = payTypeAdapter
        payTypeAdapter.submitList(payTypeList)
    }

    override fun getPayTypesError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onPayTypeClick(position: Int) {
        listener.getPayTypeId(payTypeList[position].id, payTypeList[position].name.en)
        dismiss()
    }
}