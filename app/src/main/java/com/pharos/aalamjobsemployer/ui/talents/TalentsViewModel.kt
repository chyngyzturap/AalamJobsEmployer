package com.pharos.aalamjobsemployer.ui.talents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pharos.aalamjobs.data.model.TokenAccess
import com.pharos.aalamjobs.data.model.TokenRefresh
import com.pharos.aalamjobsemployer.data.local.prefs.UserPreferences
import com.pharos.aalamjobsemployer.data.network.Resource
import com.pharos.aalamjobsemployer.data.repository.TalentsRepository
import com.pharos.aalamjobsemployer.data.responses.TalentsResponse
import com.pharos.aalamjobsemployer.ui.base.BaseViewModel
import com.pharos.aalamjobsemployer.ui.talents.model.ResumeId
import com.pharos.aalamjobsemployer.ui.talents.utils.*
import com.pharos.aalamjobsemployer.ui.talents.utils.CurrencyListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class TalentsViewModel(
    private val repository: TalentsRepository
) : BaseViewModel(repository) {
    private var currencyListener: CurrencyListener? = null
    private var talentsListener: TalentsListener? = null
    private var favListener: FavoriteListener? = null
    private var countryListener: CountryListener? = null
    private var cityListener: CityListener? = null
    private var specListener: SpecListener? = null
    private var emplTypesListener: EmplTypeListener? = null
    private var payTypesListener: PayTypeListener? = null
    private var talentListener: TalentListener? = null
    private val userPreferences: UserPreferences? = null

    private val _talents: MutableLiveData<Resource<TalentsResponse>> = MutableLiveData()
    val talents: MutableLiveData<Resource<TalentsResponse>> get() = _talents

    fun verify(verify: TokenAccess) = viewModelScope.launch {
        when (val response = repository.verify(verify)) {
            is Resource.Success -> {
            }
            is Resource.Failure -> {
                if (response.errorCode == 401) {
                    val tokenRef = userPreferences?.tokenRefresh?.first()
                    val refresh = TokenRefresh(tokenRef)
                    refresh(refresh)
                }
            }
        }
    }

    private fun refresh(refresh: TokenRefresh) = viewModelScope.launch {
        when (val response = repository.refresh(refresh)) {
            is Resource.Success -> {
                saveAccessToken(response.value.access)
            }
            is Resource.Failure -> {

            }
        }
    }

    private suspend fun saveAccessToken(tokenAccess: String?) {
        viewModelScope.launch {
            if (tokenAccess != null) {
                repository.saveAuthToken(tokenAccess)
            }
        }
    }

    fun getTalentsList(page: Int, search: String) = viewModelScope.launch {
        when (val response = repository.getTalents(page, search)) {
            is Resource.Success -> {
                talentsListener?.setTalent(response.value)
            }
            is Resource.Failure -> {
                talentsListener?.getTalentError(response.errorCode)
            }
        }
    }

    fun getTalentsFilteredList(
        options: Map<String, String>
    ) = viewModelScope.launch {
        when (val response = repository.getTalentsFiltered(options)) {
            is Resource.Success -> {
                talentsListener?.setTalent(response.value)
            }
            is Resource.Failure -> {
                talentsListener?.getTalentError(response.errorCode)
            }
        }
    }

    fun getFavJobsList() = viewModelScope.launch {
        when (val response = repository.getFavoriteJobs()) {
            is Resource.Success -> {
                favListener?.setFavoriteJob(response.value)
            }
            is Resource.Failure -> {
                favListener?.getFavJobError(response.errorCode)
            }
        }
    }

    fun getCountryList() = viewModelScope.launch {
        when (val response = repository.getCountries()) {
            is Resource.Success -> {
                countryListener?.setCountry(response.value)
            }
            is Resource.Failure -> {
                countryListener?.getCountryError(response.errorCode)
            }
        }
    }

    fun getCitiesList() = viewModelScope.launch {
        when (val response = repository.getCities()) {
            is Resource.Success -> {
                cityListener?.setCities(response.value)
            }
            is Resource.Failure -> {
                cityListener?.getCityError(response.errorCode)
            }
        }
    }

    fun getSectorsList() = viewModelScope.launch {
        when (val response = repository.getSectors()) {
            is Resource.Success -> {
                specListener?.setSpec(response.value)
            }
            is Resource.Failure -> {
                specListener?.getSpecError(response.errorCode)
            }
        }
    }

    fun getCurrencyList() = viewModelScope.launch {
        when (val response = repository.getCurrencies()) {
            is Resource.Success -> {
                currencyListener?.setCurrency(response.value)
            }
            is Resource.Failure -> {
                currencyListener?.getCurrencyError(response.errorCode)
            }
        }
    }

    fun getEmploymentTypes() = viewModelScope.launch {
        when (val response = repository.getEmploymentTypes()) {
            is Resource.Success -> {
                emplTypesListener?.setEmplTypes(response.value)
            }
            is Resource.Failure -> {
                emplTypesListener?.getEmplTypesError(response.errorCode)
            }
        }
    }

    fun getPaymentTypes() = viewModelScope.launch {
        when (val response = repository.getPaymentTypes()) {
            is Resource.Success -> {
                payTypesListener?.setPayTypes(response.value)
            }
            is Resource.Failure -> {
                payTypesListener?.getPayTypesError(response.errorCode)
            }
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

    fun setCountryListener(listener: CountryListener) {
        this.countryListener = listener
    }

    fun setCityListener(listener: CityListener) {
        this.cityListener = listener
    }

    fun setSectorListener(listener: SpecListener) {
        this.specListener = listener
    }

    fun setFavJobsListener(listener: FavoriteListener) {
        this.favListener = listener
    }

    fun setJobListener(listener: TalentListener) {
        this.talentListener = listener
    }


    fun setJobsListener(listener: TalentsListener) {
        this.talentsListener = listener
    }

    fun getTalentData(resumeId: Int) = viewModelScope.launch {
        when (val response = repository.getTalentById(resumeId)) {
            is Resource.Success -> {
                talentListener?.setJob(response.value)
            }
            is Resource.Failure -> {
                talentListener?.getJobError(response.errorCode)
            }
        }
    }

    fun setFavorite(resumeId: ResumeId) = viewModelScope.launch {
        when (val response = repository.setFavorite(resumeId)) {
            is Resource.Success -> {
                favListener?.postFavJobSuccess()
            }
            is Resource.Failure -> {
                favListener?.addToFavFailed(response.errorCode)
            }
        }
    }

    fun deleteFromFav(jobId: Int) = viewModelScope.launch {

        when (val response = repository.deleteFavorite(jobId)) {
            is Resource.Success -> favListener?.deleteFromFav()
            is Resource.Failure -> favListener?.addToFavFailed(response.errorCode)
        }
    }
}