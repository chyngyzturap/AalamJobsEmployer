package com.pharos.aalamjobsemployer.ui.talents

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.text.isDigitsOnly
import com.bumptech.glide.Glide
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.model.Talents
import com.pharos.aalamjobsemployer.data.network.TalentsApi
import com.pharos.aalamjobsemployer.data.repository.TalentsRepository
import com.pharos.aalamjobsemployer.databinding.ActivityTalentsDetailBinding
import com.pharos.aalamjobsemployer.ui.base.BaseActivity
import com.pharos.aalamjobsemployer.ui.talents.adapter.TalentsAdapter
import com.pharos.aalamjobsemployer.ui.talents.model.ResumeId
import com.pharos.aalamjobsemployer.ui.talents.utils.TalentListener
import com.pharos.aalamjobsemployer.ui.main.MainActivity
import com.pharos.aalamjobsemployer.ui.vacancies.CvWebPreviewActivity
import com.pharos.aalamjobsemployer.utils.startNewActivity
import com.pharos.aalamjobsemployer.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.lang.Exception


class TalentDetailActivity :
    BaseActivity<TalentsViewModel, ActivityTalentsDetailBinding, TalentsRepository>(),
    TalentListener, TalentsAdapter.TalentClickListener {
    private lateinit var job: Talents
    private var adapter: TalentsAdapter? = null
    private var jobId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTalentsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressbar.visible(true)
        viewModel.setJobListener(this)
        getThisTalent()

        binding.ivBackpressed.setOnClickListener {
            onBackPressed()
        }

        binding.talentDescriptionContainer.setOnClickListener {
            val intent = Intent(this, CvWebPreviewActivity::class.java)
            intent.putExtra("cvId", jobId)
            startActivity(intent)
        }
    }

    private fun getThisTalent() {
        jobId = intent.getIntExtra("jobId", 0)
        if (jobId != 0) {
            viewModel.getTalentData(jobId)
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
                viewModel.getTalentData(jobIdStr.toInt())
            } else {
                Toast.makeText(this, getString(R.string.job_not_found), Toast.LENGTH_SHORT).show()
                startNewActivity(MainActivity::class.java)
            }
        }
    }

    private fun sendEmail(recipient: String, subject: String, message: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        try {
            startActivity(Intent.createChooser(intent, "Choose Email Client..."))
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setJob(talents: Talents) {
        binding.progressbar.visible(false)
        this.job = talents
        val linkUri = "http://165.22.88.94:9000/api/resumes/${talents.id}/webview/"
        binding.talentFullName.text = talents.firstname + " " + talents.lastname
        binding.talentPosition.text = talents.position
        binding.talentLocation.text = talents.current_city.toString() + ", " +
                talents.current_country
        binding.talentDate.text = talents.created_at.split("T")[0]

        if (talents.photo != "")
            Glide.with(binding.root).load(talents.photo)
                .into(binding.ivPhotoTalent)

        binding.btnSendCv.setOnClickListener {
            sendEmail(
                talents.email,
                talents.firstname + " " + talents.lastname + ", " + talents.position,
                ""
            )
        }

        binding.ivShare.setOnClickListener {
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(getString(R.string.share_via))
                .setText(linkUri)
                .startChooser()
        }

        if (talents.favorite) {
            binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_filled)
        } else {
            binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
        }

        binding.ivTalentFavorite.setOnClickListener {
            val jobIdInt = ResumeId(job.id)
            if (!talents.favorite) {
                job.favorite
                binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_filled)
                viewModel.setFavorite(jobIdInt)
            } else {
                !talents.favorite
                binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
                viewModel.deleteFromFav(job.id)
            }

            if (!talents.favorite) {
                job.favorite
                binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_filled)
                viewModel.setFavorite(jobIdInt)
            } else {
                !talents.favorite
                binding.ivTalentFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
                viewModel.deleteFromFav(job.id)
            }
        }
    }

    override fun getJobError(code: Int?) {
        binding.progressbar.visible(false)
        Toast.makeText(this, "Error $code ", Toast.LENGTH_SHORT).show()
    }

    override fun onTalentClick(jobId: Int) {
    }

    override fun addToFavorites(position: Int) {
        val jobIdFav = ResumeId(job.id)
        viewModel.setFavorite(jobIdFav)
    }

    override fun deleteFromFavorites(position: Int) {
    }

    override fun getViewModel() = TalentsViewModel::class.java

    override fun getActivityBinding(inflater: LayoutInflater) =
        ActivityTalentsDetailBinding.inflate(layoutInflater)

    override fun getActivityRepository(): TalentsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(TalentsApi::class.java, token)
        return TalentsRepository(api)
    }
}