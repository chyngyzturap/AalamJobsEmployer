package com.pharos.aalamjobsemployer.ui.talents.search.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobsemployer.data.network.TalentsApi
import com.pharos.aalamjobsemployer.data.repository.TalentsRepository
import com.pharos.aalamjobsemployer.data.responses.dialog.CityResponse
import com.pharos.aalamjobsemployer.data.responses.dialog.CityResponseItem
import com.pharos.aalamjobsemployer.data.responses.dialog.CountryResponseItem
import com.pharos.aalamjobsemployer.databinding.LayoutCityBinding
import com.pharos.aalamjobsemployer.ui.base.BaseDialogFragment
import com.pharos.aalamjobsemployer.ui.talents.TalentsViewModel
import com.pharos.aalamjobsemployer.ui.talents.utils.CityListener
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CityDialogDialogFragment(private val listener: SearchListener) :
    BaseDialogFragment<TalentsViewModel, LayoutCityBinding, TalentsRepository>(),
    CityListener,
    CityAdapter.CityClickListener {

    private var cityList = mutableListOf<CityResponseItem>()
    private lateinit var cityAdapter: CityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCityListener(this)
        viewModel.getCitiesList()
    }

    override fun getViewModel() = TalentsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = LayoutCityBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): TalentsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(TalentsApi::class.java, token)
        return TalentsRepository(api)
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