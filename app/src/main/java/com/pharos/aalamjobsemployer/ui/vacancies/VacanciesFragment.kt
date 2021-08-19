package com.pharos.aalamjobsemployer.ui.vacancies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponse
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponseItem
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.databinding.FragmentVacancyBinding
import com.pharos.aalamjobsemployer.ui.base.BaseFragment
import com.pharos.aalamjobsemployer.ui.job.JobActivity
import com.pharos.aalamjobsemployer.ui.job.JobListener
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import com.pharos.aalamjobsemployer.ui.vacancies.adapter.VacancyAdapter
import com.pharos.aalamjobsemployer.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class VacanciesFragment : BaseFragment<JobViewModel, FragmentVacancyBinding, JobRepository>(),
    JobListener,
    VacancyAdapter.CvClickListener {

    private var vacancyAdapter: VacancyAdapter? = null
    private val resumes = mutableListOf<JobModelResponseItem>()
    private var page: Int = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cleanSearchIntents()
        binding.btnPostJob.setOnClickListener {
            showCurrent()
        }
        binding.progressbar.visible(true)
        viewModel.setJobListener(this)
        viewModel.getResumesList(page)
        binding.rvJobs.setHasFixedSize(true)

        binding.swipeRefresh.setOnRefreshListener {
            page = 1
            resumes.clear()
            viewModel.getResumesList(page)
        }
    }

    private fun showCurrent() {
        val created = runBlocking { userPreferences.created.first() }
        if (created != "") {
            val intent = Intent(requireContext(), JobActivity::class.java)
            startActivity(intent)
        } else {
            findNavController().navigate(R.id.companyInfoFragment)
            Toast.makeText(
                requireContext(),
                getString(R.string.add_your_company),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun cleanSearchIntents() {
        val countryIntent = requireActivity().intent.getIntExtra("countryId", 0)
        val cityIntent = requireActivity().intent.getIntExtra("cityId", 0)
        val sectorIntent = requireActivity().intent.getIntExtra("sectorId", 0)
        if (countryIntent != 0 || cityIntent != 0 || sectorIntent != 0) {
            requireActivity().intent.removeExtra("countryId")
            requireActivity().intent.removeExtra("cityId")
            requireActivity().intent.removeExtra("sectorId")
        }
    }

    override fun getViewModel() = JobViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentVacancyBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): JobRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(JobApi::class.java, token)
        return JobRepository(api)
    }

    override fun createCvSuccess() {
    }

    override fun createCvFailed(code: Int?) {
    }

    override fun setResume(job: JobModelResponse) {
        if (job.count != 0) {
            binding.swipeRefresh.isRefreshing = false
            binding.tvVacancies.visible(false)
            binding.loggedContainer.visible(true)
            binding.progressbar.visible(false)
            if (page == 1)
                resumes.clear()
            resumes.addAll(job.results)
            vacancyAdapter = VacancyAdapter(this)
            binding.rvJobs.adapter = vacancyAdapter
            vacancyAdapter?.submitList(resumes)
        }
    }

    override fun getCvError(code: Int?) {
        binding.progressbar.visible(false)
        binding.swipeRefresh.isRefreshing = false
        if (code != null) {
            Toast.makeText(requireContext(), "Error $code ", Toast.LENGTH_SHORT).show()
        }
    }

    override fun setJobDetail(job: JobModelResponseItem) {
    }

    override fun getJobError(code: Int?) {
    }

    override fun onCvClick(cvId: Int) {
        val intent = Intent(requireContext(), JobsDetailActivity::class.java)
        intent.putExtra("jobId", cvId)
        startActivity(intent)
    }
}