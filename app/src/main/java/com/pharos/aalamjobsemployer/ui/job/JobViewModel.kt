package com.pharos.aalamjobsemployer.ui.job

import androidx.lifecycle.viewModelScope
import com.pharos.aalamjobsemployer.data.local.db.cv.models.CompanyModel
import com.pharos.aalamjobsemployer.data.local.db.cv.models.JobModel
import com.pharos.aalamjobsemployer.data.network.Resource
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.ui.base.BaseViewModel
import com.pharos.aalamjobsemployer.ui.job.company.Company2Listener
import com.pharos.aalamjobsemployer.ui.job.company.CompanyListener
import com.pharos.aalamjobsemployer.ui.talents.utils.*
import com.pharos.aalamjobsemployer.ui.vacancies.JobDetailListener
import com.pharos.aalamjobsemployer.ui.talents.utils.CurrencyListener
import kotlinx.coroutines.launch


class JobViewModel(
    private val repository: JobRepository
) : BaseViewModel(repository) {
    private var currencyListener: CurrencyListener? = null
    private var jobDetailListener: JobDetailListener? = null
    private var countryListener: CountryListener? = null
    private var cityListener: CityListener? = null
    private var specListener: SpecListener? = null
    private var emplTypesListener: EmplTypeListener? = null
    private var payTypesListener: PayTypeListener? = null
    private var orgListener: OrganizationListener? = null
    private var compListener: Company2Listener? = null
    private var userDataListener: UserDataListener? = null
    private var listener: JobListener? = null
    private var companyListener: CompanyListener? = null

    fun createNewJob(jobModel: JobModel) = viewModelScope.launch {
        when (val response = repository.createJob(jobModel)) {
            is Resource.Success -> {
                listener?.createCvSuccess()
            }
            is Resource.Failure -> {
                listener?.createCvFailed(response.errorCode)
            }
        }
    }

    fun createNewCompany(companyModel: CompanyModel) = viewModelScope.launch {
        when (val response = repository.createNewCompany(companyModel)) {
            is Resource.Success -> {
                companyListener?.createCompanySuccess(response.value.id)
            }
            is Resource.Failure -> {
                companyListener?.createCompanyFailed(response.errorCode)
            }
        }
    }

    suspend fun companyCreated(created: String){
        viewModelScope.launch {
            repository.companyCreated(created)
        }
    }

    fun setJobListener(listener: JobListener) {
        this.listener = listener
    }
    fun setJobDetailListener(listener: JobDetailListener) {
        this.jobDetailListener = listener
    }
    fun setCompanyListener(listener: CompanyListener) {
        this.companyListener = listener
    }

    fun getResumesList(page: Int) = viewModelScope.launch {
        when (val response = repository.getMyJobs(page)) {
            is Resource.Success -> {
                listener?.setResume(response.value)
            }
            is Resource.Failure -> {
                listener?.getCvError(response.errorCode)
            }
        }
    }

    fun getJobData(jobId: Int) = viewModelScope.launch {
        when (val response = repository.getJobById(jobId)) {
            is Resource.Success -> {
                jobDetailListener?.setJobDetail(response.value)
            }
            is Resource.Failure -> {
                jobDetailListener?.getJobError(response.errorCode)
            }
        }
    }

    fun getUserData() = viewModelScope.launch {
        when (val response = repository.getUserData()) {
            is Resource.Success -> {
                userDataListener?.setUserData(response.value)
            }
            is Resource.Failure -> {
                userDataListener?.dataError(response.errorCode)
            }
        }
    }

    fun getCountryList() = viewModelScope.launch {
        when (val response = repository.getCountries()){
            is Resource.Success -> {
                countryListener?.setCountry(response.value)
            }
            is Resource.Failure -> {
                countryListener?.getCountryError(response.errorCode)
            }
        }
    }

    fun getCitiesList() = viewModelScope.launch {
        when (val response = repository.getCities()){
            is Resource.Success -> {
                cityListener?.setCities(response.value)
            }
            is Resource.Failure -> {
                cityListener?.getCityError(response.errorCode)
            }
        }
    }

    fun getSpecList() = viewModelScope.launch {
        when (val response = repository.getSpec()){
            is Resource.Success -> {
                specListener?.setSpec(response.value)
            }
            is Resource.Failure -> {
                specListener?.getSpecError(response.errorCode)
            }
        }
    }

    fun getCurrencyList() = viewModelScope.launch {
        when (val response = repository.getCurrencies()){
            is Resource.Success -> {
                currencyListener?.setCurrency(response.value)
            }
            is Resource.Failure -> {
                currencyListener?.getCurrencyError(response.errorCode)
            }
        }
    }

    fun getEmploymentTypes() = viewModelScope.launch {
        when (val response = repository.getEmploymentTypes()){
            is Resource.Success -> {
                emplTypesListener?.setEmplTypes(response.value)
            }
            is Resource.Failure -> {
                emplTypesListener?.getEmplTypesError(response.errorCode)
            }
        }
    }

    fun getPaymentTypes() = viewModelScope.launch {
        when (val response = repository.getPaymentTypes()){
            is Resource.Success -> {
                payTypesListener?.setPayTypes(response.value)
            }
            is Resource.Failure -> {
                payTypesListener?.getPayTypesError(response.errorCode)
            }
        }
    }

    fun getOrganizations() = viewModelScope.launch {
        when (val response = repository.getOrganizations()){
            is Resource.Success -> {
                orgListener?.setOrganization(response.value)
            }
            is Resource.Failure -> {
                orgListener?.getOrgError(response.errorCode)
            }
        }
    }
    fun getMyOrganizations(id: Int) = viewModelScope.launch {
        when (val response = repository.getMyOrganization(id)){
            is Resource.Success -> {
                compListener?.setCompany(response.value)
            }
            is Resource.Failure -> {
                compListener?.getCompanyError(response.errorCode)            }
        }
    }

    fun setCurrencyListener(listener: CurrencyListener) {
        this.currencyListener = listener
    }
    fun setEmplTypesListener(listener: EmplTypeListener) {
        this.emplTypesListener = listener
    }

    fun setPayTypesListener(listener: PayTypeListener) {
        this.payTypesListener = listener
    }

    fun setOrgListener(listener: OrganizationListener) {
        this.orgListener = listener
    }

    fun setCountryListener(listener: CountryListener) {
        this.countryListener = listener
    }

    fun setCityListener(listener: CityListener) {
        this.cityListener = listener
    }

    fun setSpecListener(listener: SpecListener) {
        this.specListener = listener
    }
    fun setUserDataListener(listener: UserDataListener) {
        this.userDataListener = listener
    }
}