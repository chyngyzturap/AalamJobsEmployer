package com.pharos.aalamjobsemployer.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobsemployer.data.network.TalentsApi
import com.pharos.aalamjobsemployer.data.repository.TalentsRepository
import com.pharos.aalamjobsemployer.databinding.FragmentFavoritesBinding
import com.pharos.aalamjobsemployer.ui.base.BaseFragment
import com.pharos.aalamjobsemployer.ui.favorites.adapter.FavoriteAdapter
import com.pharos.aalamjobsemployer.ui.favorites.model.FavoriteTalents
import com.pharos.aalamjobsemployer.ui.favorites.model.FavoriteTalentsItem
import com.pharos.aalamjobsemployer.ui.talents.TalentDetailActivity
import com.pharos.aalamjobsemployer.ui.talents.TalentsViewModel
import com.pharos.aalamjobsemployer.ui.talents.model.ResumeId
import com.pharos.aalamjobsemployer.ui.talents.utils.FavoriteListener
import com.pharos.aalamjobsemployer.utils.dialogfragments.SignUpDialogFragment
import com.pharos.aalamjobsemployer.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class FavoritesFragment : BaseFragment<TalentsViewModel, FragmentFavoritesBinding, TalentsRepository>(),
    FavoriteAdapter.TalentClickListener, FavoriteListener {
    private var favAdapter: FavoriteAdapter? = null
    private val jobsFavList = mutableListOf<FavoriteTalentsItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressbar.visible(true)
        viewModel.setFavJobsListener(this)
        viewModel.getFavJobsList()
        binding.rvJobs.setHasFixedSize(true)
        cleanSearchIntents()
    }

    private fun cleanSearchIntents(){
        val countryIntent = requireActivity().intent.getIntExtra("salary", 0)
        val cityIntent = requireActivity().intent.getIntExtra("currencyId", 0)
        val sectorIntent = requireActivity().intent.getIntExtra("emplId", 0)
        if (countryIntent != 0 || cityIntent != 0 || sectorIntent != 0) {
            requireActivity().intent.removeExtra("salary")
            requireActivity().intent.removeExtra("currencyId")
            requireActivity().intent.removeExtra("emplId")
        }
    }

    override fun getViewModel()= TalentsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): TalentsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(TalentsApi::class.java, token)
        val api = remoteDataSource.buildApi(TalentsApi::class.java, token)
        if (token.isNullOrEmpty()){
            return TalentsRepository(apiNoToken)
        } else {
            return TalentsRepository(api)
        }
    }

    override fun setFavoriteJob(jobs: FavoriteTalents) {
        val token = runBlocking { userPreferences.tokenAccess.first() }

        if (token.isNullOrEmpty()){
            val signUpDialogFragment = SignUpDialogFragment()
            val manager = requireActivity().supportFragmentManager
            signUpDialogFragment.show(manager, "signUpDialog")
        } else {
            if (jobs.isNotEmpty()) {
                binding.tvFavorites.visible(false)
                binding.loggedContainer.visible(true)
                binding.progressbar.visible(false)
                jobsFavList.clear()
                jobsFavList.addAll(jobs)
                favAdapter = FavoriteAdapter(this)
                binding.rvJobs.adapter = favAdapter
                favAdapter?.submitList(jobsFavList)
            }
        }
    }

    override fun getFavJobError(code: Int?) {
    }

    override fun onTalentClick(jobId: Int) {
        val intent = Intent(requireContext(), TalentDetailActivity::class.java)
        intent.putExtra("jobId", jobId)
        startActivity(intent)
    }

    override fun addToFavorites(position: Int) {
        val jobIdFav = ResumeId(position)
        viewModel.setFavorite(jobIdFav)
    }

    override fun deleteFromFavorites(position: Int) {
        viewModel.deleteFromFav(position)
    }

    override fun postFavJobSuccess() {
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
    }

    override fun addToFavFailed(code: Int?) {
        Toast.makeText(requireContext(), "error + $code", Toast.LENGTH_SHORT).show()
    }

    override fun deleteFromFav() {
    }
}