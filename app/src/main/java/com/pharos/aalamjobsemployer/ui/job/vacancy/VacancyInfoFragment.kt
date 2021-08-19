package com.pharos.aalamjobsemployer.ui.job.vacancy

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobsemployer.data.local.db.cv.models.*
import com.pharos.aalamjobsemployer.data.network.JobApi
import com.pharos.aalamjobsemployer.data.repository.JobRepository
import com.pharos.aalamjobsemployer.data.responses.UserResponse
import com.pharos.aalamjobsemployer.databinding.FragmentVacancyInfoBinding
import com.pharos.aalamjobsemployer.ui.base.BaseFragment
import com.pharos.aalamjobsemployer.ui.job.JobListener
import com.pharos.aalamjobsemployer.ui.job.JobViewModel
import com.pharos.aalamjobsemployer.ui.job.UserDataListener
import com.pharos.aalamjobsemployer.ui.job.company.AddCompanyDialogDialogFragment
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import com.pharos.aalamjobsemployer.ui.main.MainActivity
import com.pharos.aalamjobsemployer.utils.dialogfragments.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class VacancyInfoFragment :
    BaseFragment<JobViewModel, FragmentVacancyInfoBinding, JobRepository>(), SearchListener,
    JobListener, UserDataListener {

    private var jobModel: JobModel? = null


    private var mCurrencySign: String = ""
    private var mIdCurrency: Int = 0
    private var mEmplTypeName: String = ""
    private var mIdEmpType: Int = 0
    private var mPayTypeName: String = ""
    private var mIdPayType: Int = 0
    private var mId: Int = 0
    private var mName: String = ""
    private var mIdCity: Int = 0
    private var mNameCity: String = ""
    private var mIdSpec: Int = 0
    private var mNameSpec: String = ""
    private var mIdOrg: Int = 0
    private var mNameOrg: String = ""

    private var s = ""
    private var s2 = ""

    private var idCompany = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setUserDataListener(this)
        viewModel.getUserData()

        launch {
            context?.let {
                val companyId = CvDatabase(it).getCompanyDao().getIdCompany()?.toInt()
                if (companyId != 0){
                    if (companyId != null) {
                        idCompany = companyId
                    }
                }
            }}



        entryChipReq()
        entryChipResp()

        val c = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.YEAR, year)
            updateLable(c)
        }

        val c2 = Calendar.getInstance()

        val datePicker2 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c2.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c2.set(Calendar.MONTH, month)
            c2.set(Calendar.YEAR, year)
            updateLableDeadline(c2)
        }

        binding.tvCountry.editText?.setOnClickListener {
            val countryDialogFragment = CountryDialog2DialogFragment(this)
            val manager = requireActivity().supportFragmentManager
            countryDialogFragment.show(manager, "countryDialog")
        }

        binding.tvCity.editText?.setOnClickListener {
            val cityDialogFragment = CityDialog2DialogFragment(this)
            val manager = requireActivity().supportFragmentManager
            cityDialogFragment.show(manager, "cityDialog")
        }

        binding.tvSpec.editText?.setOnClickListener {
            val sectorDialogFragment = SpecDialog2DialogFragment(this)
            val manager = requireActivity().supportFragmentManager
            sectorDialogFragment.show(manager, "specDialog")
        }

        binding.tvSalaryCurrency.setOnClickListener {
            val currencyDialogFragment = CurrencyDialogDialogFragment(this)
            val manager = requireActivity().supportFragmentManager
            currencyDialogFragment.show(manager, "currencyDialog")
        }

        binding.tvPaymentMethod.editText?.setOnClickListener {
            val payTypeDialogFragment = PaymentTypeDialogDialogFragment(this)
            val manager = requireActivity().supportFragmentManager
            payTypeDialogFragment.show(manager, "payTypeDialog")
        }

        binding.tvEmplType.editText?.setOnClickListener {
            val emplType = EmploymentTypeDialogDialogFragment(this)
            val manager = requireActivity().supportFragmentManager
            emplType.show(manager, "emplTypeDialog")
        }
        binding.tvOrganization.editText?.setOnClickListener {
            launch {
                context?.let {
                    val companyId = CvDatabase(it).getCompanyDao().getIdCompany()?.toInt()
                    if (companyId != 0){
                        if (companyId != null) {
                            idCompany = companyId
                        }
                    }
                }}
            if (idCompany != 0 && idCompany != 21) {
                val org = OrganizationDialogDialogFragment(this)
                val manager = requireActivity().supportFragmentManager
                org.show(manager, "orgDialog")
            } else {
                val addCompanyDialogFragment = AddCompanyDialogDialogFragment()
                val manager = requireActivity().supportFragmentManager
                addCompanyDialogFragment.show(manager, "addCompanyDialog")
            }
        }

        binding.tvStartDate.editText?.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePicker, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.tvJobDeadline.setEndIconOnClickListener {
            DatePickerDialog(
                requireContext(), datePicker2, c2.get(Calendar.YEAR), c2.get(Calendar.MONTH),
                c2.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.vacancyBtnNext.setOnClickListener {

            val title = binding.etJobTitle.text.toString().trim()
            val desc = binding.etJobDescription.text.toString().trim()
            val industry = mIdSpec
            val specialization = mIdSpec
            val startedDate = binding.etStartDate.text.toString().trim()
            val salaryFrom = binding.etSalaryFrom.text.toString().trim().toInt()
            val salaryTo = binding.etSalaryTo.text.toString().trim().toInt()
            val salary = com.pharos.aalamjobsemployer.ui.talents.model.Salary(salaryTo, salaryFrom)
            val salaryCurrency = mIdCurrency
            val employmentType = mIdEmpType
            val paymentType = mIdPayType
            val resp = s.split(",").map { it.split("\n")[0] }
            val req = s2.split(",").map { it.split("\n")[0] }
            val schedule = binding.etJobSchedule.text.toString().trim()
            val deadline = binding.etJobDeadline.text.toString().trim()
            val organization = mIdOrg
            val city = mIdCity
            val country = mId

            if (title != "" && desc != "" && startedDate != "" && salaryFrom != 0 && salaryTo != 0 &&
                salaryCurrency != 0 && employmentType != 0 && specialization != 0 && paymentType != 0 && s != "" && s2 != ""
                && schedule != "" && deadline != "" && organization != 0 && city != 0) {


                jobModel = JobModel(
                    city,
                    salaryCurrency,
                    deadline,
                    desc,
                    employmentType,
                    organization,
                    paymentType,
                    title,
                    req,
                    resp,
                    salary,
                    schedule,
                    specialization,
                    startedDate,
                    title
                )

                viewModel.createNewJob(jobModel!!)



            } else {
                if (city != 0) {
                    binding.tvCity.error = null
                } else  {
                    binding.tvCity.error = R.string.txt_required.toString()
                }

                if (deadline != "") {
                    binding.tvJobDeadline.error = null

                } else {
                    binding.tvJobDeadline.error = resources.getString(R.string.txt_required).toString()
                }
                if (desc != "") {
                    binding.tvJobDescription.error = null
                } else {

                    binding.tvJobDescription.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (employmentType != 0) {
                    binding.tvEmplType.error = null
                } else {

                    binding.tvEmplType.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (organization != 0) {
                    binding.tvOrganization.error = null
                } else {

                    binding.tvOrganization.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (paymentType != 0) {
                    binding.tvPaymentMethod.error = null
                } else {

                    binding.tvPaymentMethod.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (title != "") {
                    binding.tvJobTitle.error = null
                } else {

                    binding.tvJobTitle.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (s != "") {
                    binding.tvJobResponsibilities.error = null
                } else {

                    binding.tvJobResponsibilities.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (s2 != "") {
                    binding.tvReq.error = null
                } else {

                    binding.tvReq.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (salaryFrom != 0) {
                    binding.tvSalaryFrom.error = null
                } else {

                    binding.tvSalaryFrom.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (salaryTo != 0) {
                    binding.tvSalaryTo.error = null
                } else {

                    binding.tvSalaryTo.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (schedule != "") {
                    binding.tvJobSchedule.error = null
                } else {

                    binding.tvJobSchedule.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (specialization != 0) {
                    binding.tvSpec.error = null
                } else {

                    binding.tvSpec.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (startedDate != "") {
                    binding.tvStartDate.error = null
                } else {

                    binding.tvStartDate.error =
                        resources.getString(R.string.txt_required).toString()
                }
                if (title != "") {
                    binding.tvJobTitle.error = null
                } else {

                    binding.tvJobTitle.error =
                        resources.getString(R.string.txt_required).toString()
                }
            }
        }


    }

    private fun initUserData(){

//        val position = runBlocking {userPreferences.position.first()  }
//
//        binding.tvPosition.editText?.setText(position)
    }

    private fun updateLable(c: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etStartDate.setText(sdf.format(c.time))
    }

    private fun updateLableDeadline(c: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etJobDeadline.setText(sdf.format(c.time))
    }

    override fun getViewModel() = JobViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentVacancyInfoBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() : JobRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }

        val api = remoteDataSource.buildApi(JobApi::class.java, token)
        val cvDatabase = CvDatabase
        return JobRepository(api)
    }

    override fun getCountryId(idCountry: Int, nameCountry: String) {
        mId = idCountry
        mName = nameCountry
        binding.tvCountry.editText?.setText(mName)
    }

    override fun getCityId(idCity: Int, nameCity: String) {
        mIdCity = idCity
        mNameCity = nameCity
        binding.tvCity.editText?.setText(mNameCity)
    }

    override fun getSpecId(idSector: Int, nameSector: String) {
        mIdSpec = idSector
        mNameSpec = nameSector
        binding.tvSpec.editText?.setText(mNameSpec)
    }

    override fun getCurrencySign(idCurrency: Int, currencySign: String) {
        mIdCurrency = idCurrency
        mCurrencySign = currencySign
        binding.tvSalaryCurrency.text = mCurrencySign
    }

    override fun getEmplTypeId(idEmplType: Int, nameEmplType: String) {
        mIdEmpType = idEmplType
        mEmplTypeName = nameEmplType
        binding.tvEmplType.editText?.setText(mEmplTypeName)
    }

    override fun getPayTypeId(idPayType: Int, namePayType: String) {
        mIdPayType = idPayType
        mPayTypeName = namePayType
        binding.tvPaymentMethod.editText?.setText(mPayTypeName)

    }

    override fun getOrgId(idOrg: Int, nameOrg: String) {
        mIdOrg = idOrg
        mNameOrg = nameOrg
        binding.tvOrganization.editText?.setText(mNameOrg)
    }

    private fun createChipsResp(responsibility: String) {
        val chip = Chip(requireContext())
        chip.apply {
            text = responsibility
            chipIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_launcher_background)
            isChipIconVisible = false
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            binding.chipGroupResp.addView(chip as View)
            setOnCloseIconClickListener {
                binding.chipGroupResp.removeView(chip as View)
            }
        }
    }

    private fun entryChipResp() {
        binding.tvJobResponsibilities.setEndIconOnClickListener {
//                view, i, keyEvent ->
//            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                binding.apply {
                    val responsibility = etJobResponsibilities.text.toString()
                    createChipsResp(responsibility)
                    if (s.isNotEmpty())
                        s += ",$responsibility"
                    else s+= responsibility
                    etJobResponsibilities.text?.clear()
                }
//                return@setOnKeyListener true
//            }
//            false
        }
    }

    private fun createChipsReq(responsibility: String) {
        val chip = Chip(requireContext())
        chip.apply {
            text = responsibility
            chipIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_launcher_background)
            isChipIconVisible = false
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            binding.chipGroupReq.addView(chip as View)
            setOnCloseIconClickListener {
                binding.chipGroupReq.removeView(chip as View)
            }
        }
    }
    private fun entryChipReq() {
        binding.tvReq.setEndIconOnClickListener {
//                view, i, keyEvent ->
//            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                binding.apply {
                    val responsibility = etReq.text.toString()
                    createChipsReq(responsibility)
                    if (s2.isNotEmpty())
                        s2 += ",$responsibility"
                    else s2+= responsibility
                    etReq.text?.clear()
                }
//                return@setOnKeyListener true
//            }
//            false
        }
    }

    override fun createCvSuccess() {
        val intent2 = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent2)
    }

    override fun createCvFailed(code: Int?) {
        Toast.makeText(requireContext(), "$code", Toast.LENGTH_SHORT).show()
    }

    override fun setResume(job: JobModelResponse) {
        TODO("Not yet implemented")
    }

    override fun getCvError(code: Int?) {
        TODO("Not yet implemented")
    }

    override fun setJobDetail(jobDetail: JobModelResponseItem) {
        TODO("Not yet implemented")
    }

    override fun getJobError(code: Int?) {
        TODO("Not yet implemented")
    }


    override fun setUserData(userResponse: UserResponse) {
        if (userResponse.organization != null) {
            idCompany = userResponse.organization.id
        }
    }

    override fun dataError(code: Int?) {
    }
}