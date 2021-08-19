package com.pharos.aalamjobsemployer.ui.vacancies

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModelResponseItem
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.databinding.ActivityJobsDetailBinding
import com.pharos.aalamjobsemployer.ui.base.BaseActivity
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import com.pharos.aalamjobsemployer.ui.main.MainActivity
import com.pharos.aalamjobsemployer.ui.vacancies.adapter.VacancyAdapter
import com.pharos.aalamjobsemployer.utils.startNewActivity
import com.pharos.aalamjobsemployer.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class JobsDetailActivity : BaseActivity<JobViewModel, ActivityJobsDetailBinding, JobRepository>(),
    JobDetailListener, VacancyAdapter.CvClickListener {
    private lateinit var job: JobModelResponseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressbar.visible(true)
        viewModel.setJobDetailListener(this)

        getMyVacancy()

        binding.ivBackpressed.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getMyVacancy() {
        val jobId = intent.getIntExtra("jobId", 0)
        if (jobId != 0) {
            viewModel.getJobData(jobId)
        } else {
            val uri = intent.data.toString()
            var last = uri.lastIndex
            var id = ""
            while (uri[last] != '/') {
                id += uri[last]
                last--
            }
            val jobIdStr = id.reversed()
            if (jobIdStr.isDigitsOnly()) {
                viewModel.getJobData(jobIdStr.toInt())
            } else {
                Toast.makeText(this, "Job not found", Toast.LENGTH_SHORT).show()
                startNewActivity(MainActivity::class.java)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setJobDetail(jobs: JobModelResponseItem) {
        binding.progressbar.visible(false)
        this.job = jobs

        val respList = jobs.responsibilities
        var respText = ""
        val reqList = jobs.requirements
        var reqText = ""
        for (text in respList) {
            respText += "\n • $text"
        }
        for (text in reqList) {
            reqText += "\n • $text"
        }

        binding.tvResponsibilities.text = respText
        binding.tvRequirements.text = reqText
        binding.jobTitle.text = jobs.title
        binding.jobNameCompany.text = jobs.organization.name
        binding.jobNameLocation.text = jobs.city.name.en.toString() + ", " +
                jobs.city.country.name.en
        binding.jobsDate.text = jobs.published_date.split("T")[0]
        binding.tvPosition.text = jobs.position
        binding.tvSchedule.text = jobs.schedule
        binding.tvSalary.text =
            jobs.salary.min.toString() + "-" + jobs.salary.max + jobs.currency.sign
        binding.tvDescription.text = jobs.description
    }

    override fun getJobError(code: Int?) {
        binding.progressbar.visible(false)
        Toast.makeText(this, "Error $code ", Toast.LENGTH_SHORT).show()
    }

    override fun getViewModel() = JobViewModel::class.java

    override fun getActivityBinding(inflater: LayoutInflater) =
        ActivityJobsDetailBinding.inflate(layoutInflater)

    override fun getActivityRepository(): JobRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(JobApi::class.java, token)
        return JobRepository(api)
    }

    override fun onCvClick(cvId: Int) {
    }
}