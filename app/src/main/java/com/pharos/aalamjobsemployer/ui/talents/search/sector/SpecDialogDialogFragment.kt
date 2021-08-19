package com.pharos.aalamjobsemployer.ui.talents.search.sector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobsemployer.data.network.TalentsApi
import com.pharos.aalamjobsemployer.data.repository.TalentsRepository
import com.pharos.aalamjobsemployer.data.responses.dialog.SectorResponse
import com.pharos.aalamjobsemployer.data.responses.dialog.SpecResponseItem
import com.pharos.aalamjobsemployer.databinding.LayoutCountryBinding
import com.pharos.aalamjobsemployer.ui.base.BaseDialogFragment
import com.pharos.aalamjobsemployer.ui.talents.TalentsViewModel
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import com.pharos.aalamjobsemployer.ui.talents.utils.SpecListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SpecDialogDialogFragment(private val listener: SearchListener) :
    BaseDialogFragment<TalentsViewModel, LayoutCountryBinding, TalentsRepository>(),
    SpecListener, SpecAdapter.SectorClickListener {

    private var sectorList = mutableListOf<SpecResponseItem>()
    private lateinit var specAdapter: SpecAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setSectorListener(this)
        viewModel.getSectorsList()
    }

    override fun getViewModel() = TalentsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = LayoutCountryBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): TalentsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(TalentsApi::class.java, token)
        return TalentsRepository(api)
    }

    override fun setSpec(jobs: SectorResponse) {
        sectorList.addAll(jobs)
        specAdapter = SpecAdapter(this)
        binding.rvCountry.adapter = specAdapter
        specAdapter.submitList(sectorList)
    }

    override fun getSpecError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onSectorClick(position: Int) {
        listener.getSpecId(sectorList[position].id, sectorList[position].name)
        dismiss()
    }
}