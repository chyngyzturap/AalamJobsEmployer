package com.pharos.aalamjobsemployer.ui.talents

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import com.pharos.aalamjobs.data.model.TokenAccess
import com.pharos.aalamjobsemployer.data.model.Talents
import com.pharos.aalamjobsemployer.data.network.TalentsApi
import com.pharos.aalamjobsemployer.data.repository.TalentsRepository
import com.pharos.aalamjobsemployer.data.responses.TalentsResponse
import com.pharos.aalamjobsemployer.databinding.FragmentTalentsBinding
import com.pharos.aalamjobsemployer.ui.base.BaseFragment
import com.pharos.aalamjobsemployer.ui.favorites.model.FavoriteTalents
import com.pharos.aalamjobsemployer.ui.talents.adapter.TalentsAdapter
import com.pharos.aalamjobsemployer.ui.talents.model.ResumeId
import com.pharos.aalamjobsemployer.ui.talents.utils.FavoriteListener
import com.pharos.aalamjobsemployer.ui.talents.utils.TalentsListener
import com.pharos.aalamjobsemployer.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class TalentsFragment : BaseFragment<TalentsViewModel, FragmentTalentsBinding, TalentsRepository>(),
    TalentsListener,
    TalentsAdapter.TalentClickListener, FavoriteListener {
    private var talentsAdapter: TalentsAdapter? = null
    private val jobs = mutableListOf<Talents>()
    private var page: Int = 1
    private var search: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyToken()
        binding.progressbar.visible(true)
        viewModel.setJobsListener(this)
        binding.rvJobs.setHasFixedSize(true)
        filterList()

        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight) {
                binding.progressbar.visible(true)
                page++
                verifyToken()
                viewModel.getTalentsList(page, search)
                binding.progressbar.visible(false)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            page = 1
            jobs.clear()
            verifyToken()
            viewModel.getTalentsList(page, search)
        }

        binding.ivSearchDetail.setOnClickListener {
            val intent = Intent(requireContext(), TalentSearchDetailActivity::class.java)
            startActivity(intent)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getJobsList()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }

    private fun filterList() {
        val currencyIntent = requireActivity().intent.getIntExtra("currencyId", 0)
        val currencyArray = "[$currencyIntent]"

        val emplIntent = requireActivity().intent.getIntExtra("emplId", 0)
        val emplArray = "[$emplIntent]"

        val salaryIntent = requireActivity().intent.getIntExtra("salary", 0)
        val salaryArray = "[$salaryIntent]"

        if (currencyIntent == 0 && emplIntent == 0 && salaryIntent == 0) {
            viewModel.getTalentsList(page, search)
        }

        var options: HashMap<String, String> = HashMap<String, String>()

        if (salaryIntent != 0) {
            options.put("salary", "$salaryIntent")
            viewModel.getTalentsFilteredList(options)
        }
        if (emplIntent != 0) {
            options.put("employment", "$emplIntent")
            viewModel.getTalentsFilteredList(options)
        }
        if (currencyIntent != 0) {
            options.put("currency", currencyArray)
            viewModel.getTalentsFilteredList(options)
        }
    }

    private fun verifyToken() {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val access = TokenAccess(token)
        viewModel.verify(access)
    }

    private fun getJobsList() {
        viewModel.getTalentsList(page, binding.etSearch.text.toString().trim())
        talentsAdapter = TalentsAdapter(this)
        binding.rvJobs.setHasFixedSize(true)
        binding.rvJobs.adapter = talentsAdapter
        binding.swipeRefresh.isRefreshing = false
        talentsAdapter?.submitList(jobs)
    }

    override fun getViewModel() = TalentsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTalentsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): TalentsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(TalentsApi::class.java, token)
        return TalentsRepository(api)
    }

    override fun onTalentClick(talentId: Int) {
        val intent = Intent(requireContext(), TalentDetailActivity::class.java)
        intent.putExtra("jobId", talentId)
        startActivity(intent)
    }

    override fun addToFavorites(position: Int) {
        val jobIdFav = ResumeId(position)
        viewModel.setFavorite(jobIdFav)
    }

    override fun deleteFromFavorites(position: Int) {
        viewModel.deleteFromFav(position)
    }

    override fun setTalent(talents: TalentsResponse) {
        binding.swipeRefresh.isRefreshing = false
        binding.progressbar.visible(false)
        if (page == 1)
            jobs.clear()
        jobs.addAll(talents.results)
        talentsAdapter = TalentsAdapter(this)
        binding.rvJobs.adapter = talentsAdapter
        talentsAdapter?.submitList(jobs)
    }

    override fun getTalentError(code: Int?) {
        binding.progressbar.visible(false)
        binding.swipeRefresh.isRefreshing = false
        if (code != 404) {
            Toast.makeText(requireContext(), "Error $code ", Toast.LENGTH_SHORT).show()
        }
    }

    override fun postFavJobSuccess() {
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
    }

    override fun addToFavFailed(code: Int?) {
        Toast.makeText(requireContext(), "error + $code", Toast.LENGTH_SHORT).show()
    }

    override fun deleteFromFav() {
    }

    override fun setFavoriteJob(jobs: FavoriteTalents) {
    }

    override fun getFavJobError(code: Int?) {
    }
}