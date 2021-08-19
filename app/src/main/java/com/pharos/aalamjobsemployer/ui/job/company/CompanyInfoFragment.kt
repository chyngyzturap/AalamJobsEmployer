package com.pharos.aalamjobsemployer.ui.job.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobsemployer.data.local.db.cv.entities.CompanyInfo
import com.pharos.aalamjobsemployer.data.local.db.cv.models.CompanyModel
import com.pharos.aalamjobsemployer.data.local.db.cv.models.CompanyModelResponse
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.databinding.FragmentCompanyInfoBinding
import com.pharos.aalamjobsemployer.ui.base.BaseFragment
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import com.pharos.aalamjobsemployer.ui.talents.search.sector.SpecDialogDialogFragment
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import com.pharos.aalamjobsemployer.utils.dialogfragments.CityDialog2DialogFragment
import com.pharos.aalamjobsemployer.utils.dialogfragments.CountryDialog2DialogFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CompanyInfoFragment : BaseFragment<JobViewModel, FragmentCompanyInfoBinding, JobRepository>(),
    SearchListener, CompanyListener, Company2Listener {
    private var companyModel: CompanyModel? = null
    private var mId: Int = 0
    private var mName: String = ""
    private var mIdCity: Int = 0
    private var mNameCity: String = ""
    private var mIdSector: Int = 0
    private var mNameSector: String = ""
    private var idCompany: Int = 0
    private lateinit var companyModelResponse: CompanyModelResponse

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCompanyListener(this)
        getMyOrgData()

        binding.tvCompanyCountry.editText?.setOnClickListener {
            val countryDialogFragment = CountryDialog2DialogFragment(this)
            val manager = requireActivity().supportFragmentManager
            countryDialogFragment.show(manager, "countryDialog")
        }

        binding.tvCompanyCity.editText?.setOnClickListener {
            val cityDialogFragment = CityDialog2DialogFragment(this)
            val manager = requireActivity().supportFragmentManager
            cityDialogFragment.show(manager, "cityDialog")
        }

        binding.tvIndustry.editText?.setOnClickListener {
            val sectorDialogFragment = SpecDialogDialogFragment(this)
            val manager = requireActivity().supportFragmentManager
            sectorDialogFragment.show(manager, "sectorDialog2")
        }

        binding.companyBtnNext.setOnClickListener {
            createNewVacancy()
        }

        binding.companyBtnSkip.setOnClickListener {
            goToCurrentSkip()
        }
    }

    private fun goToCurrentSkip() {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()) {
            findNavController().navigate(R.id.nav_register)
        } else {
            findNavController().navigate(R.id.nav_profile)
        }
    }

    private fun createNewVacancy() {
        val companyName = binding.etCompanyName.text.toString().trim()
        val industry = mIdSector
        val industry2 = listOf<Int>(industry)
        val website = binding.etCompanyWebsite.text.toString().trim()
        val summary = binding.etCompanySummary.text.toString().trim()
        val address = binding.etCompanyAddress.text.toString().trim()
        val city = mIdCity
        val country = mId
        val phone = binding.etCompanyPhone.text.toString().trim()
        val email = binding.etCompanyEmail.text.toString().trim()

        if (companyName != "" && industry != 0 && website != "" && summary != "" && address != "" &&
            country != 0 && phone != "" && email != ""
        ) {
            companyModel = CompanyModel(
                summary,
                address,
                country,
                email,
                companyName,
                phone,
                industry2,
                website
            )
            viewModel.createNewCompany(companyModel!!)

            lifecycleScope.launch {
                val created = "created"
                viewModel.companyCreated(created)
            }
        } else {
            if (companyName != "") {
                binding.tvCompanyName.error = null
            } else {
                binding.tvCompanyName.error = R.string.txt_required.toString()
            }
            if (industry != 0) {
                binding.tvIndustry.error = null
            } else {
                binding.tvIndustry.error = resources.getString(R.string.txt_required).toString()
            }
            if (website != "") {
                binding.tvCompanyWebsite.error = null
            } else {
                binding.tvCompanyWebsite.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (summary != "") {
                binding.tvCompanySummary.error = null
            } else {
                binding.tvCompanySummary.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (address != "") {
                binding.tvCompanyAddress.error = null
            } else {

                binding.tvCompanyAddress.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (country != 0) {
                binding.tvCompanyCountry.error = null
            } else {

                binding.tvCompanyCountry.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (phone != "") {
                binding.tvCompanyPhone.error = null
            } else {
                binding.tvCompanyPhone.error =
                    resources.getString(R.string.txt_required).toString()
            }
        }
    }

    private fun getMyOrgData() {
        launch {
            context?.let {
                val companyId = arguments?.getInt("idOfCompany", 0)
                if (companyId != 0) {
                    if (companyId != null) {
                        viewModel.getMyOrganizations(companyId)
                    }
                }
            }
        }
    }

    override fun getViewModel() = JobViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompanyInfoBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): JobRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(JobApi::class.java, token)
        return JobRepository(api)
    }

    override fun getCountryId(idCountry: Int, nameCountry: String) {
        mId = idCountry
        mName = nameCountry
        binding.tvCompanyCountry.editText?.setText(mName)
    }

    override fun getCityId(idCity: Int, nameCity: String) {
        mIdCity = idCity
        mNameCity = nameCity
        binding.tvCompanyCity.editText?.setText(mNameCity)
    }

    override fun getSpecId(idSector: Int, nameSector: String) {
        mIdSector = idSector
        mNameSector = nameSector
        binding.tvIndustry.editText?.setText(mNameSector)
    }

    override fun getCurrencySign(idCurrency: Int, currencySign: String) {
    }

    override fun getEmplTypeId(idEmplType: Int, nameEmplType: String) {
    }

    override fun getPayTypeId(idPayType: Int, namePayType: String) {
    }

    override fun getOrgId(idOrg: Int, nameOrg: String) {
    }

    override fun createCompanySuccess(id: Int) {
        idCompany = id
        if (idCompany != 0) {
            val company = CompanyInfo(idCompany)
            launch {
                context?.let {
                    CvDatabase(it).getCompanyDao()
                        .insertCompanyInfo(company)
                }
            }
        }
        goToCurrent()
    }

    private fun goToCurrent() {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()) {
            findNavController().navigate(R.id.nav_register)
        } else {
            findNavController().navigate(R.id.nav_resume)
        }
    }

    override fun createCompanyFailed(code: Int?) {
    }

    override fun setCompany(company: CompanyModelResponse) {
        this.companyModelResponse = company
        launch {
            context?.let {
                val companyId = arguments?.getInt("idOfCompany", 0)
                if (companyId != 0) {
                    if (companyId != null) {
                        mId = companyModelResponse.country.id
                        binding.tvCompanyCountry.editText?.setText(companyModelResponse.country.name.en)
                        binding.tvCompanyName.editText?.setText(companyModelResponse.name)
                        binding.tvCompanyAddress.editText?.setText(companyModelResponse.address)
                        binding.tvCompanySummary.editText?.setText(companyModelResponse.about)
                        binding.tvCompanyWebsite.editText?.setText(companyModelResponse.website)
                        binding.tvCompanyEmail.editText?.setText(companyModelResponse.email)
                        binding.tvCompanyPhone.editText?.setText(companyModelResponse.phone)
                    }
                }
            }
        }
    }

    override fun getCompanyError(code: Int?) {
    }
}