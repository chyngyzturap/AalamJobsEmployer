package com.pharos.aalamjobsemployer.ui.talents

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.ui.talents.search.location.CityDialogDialogFragment
import com.pharos.aalamjobsemployer.ui.talents.search.location.CountryDialogDialogFragment
import com.pharos.aalamjobsemployer.ui.talents.search.sector.SpecDialogDialogFragment
import com.pharos.aalamjobsemployer.ui.talents.utils.SearchListener
import com.pharos.aalamjobsemployer.ui.main.MainActivity
import com.pharos.aalamjobsemployer.utils.dialogfragments.CurrencyDialogDialogFragment
import com.pharos.aalamjobsemployer.utils.dialogfragments.EmploymentTypeDialogDialogFragment
import kotlinx.android.synthetic.main.activity_talent_search_detail.*

class TalentSearchDetailActivity : AppCompatActivity(), SearchListener {
    private var mId: Int = 0
    private var mName: String = ""
    private var mIdCity: Int = 0
    private var mNameCity: String = ""
    private var mIdSector: Int = 0
    private var mNameSector: String = ""
    private var mCurrencySign: String = ""
    private var mIdCurrency: Int = 0
    private var mEmplTypeName: String = ""
    private var mIdEmpType: Int = 0
    private var salaryExpect: Int = 0

    override fun getCountryId(idCountry: Int, nameCountry: String) {
    }
    override fun getCityId(idCity: Int, nameCity: String) {
    }

    override fun getSpecId(idSector: Int, nameSector: String) {
    }

    override fun getCurrencySign(idCurrency: Int,currencySign: String) {
        mIdCurrency = idCurrency
        mCurrencySign = currencySign
        tv_salary_currency.text = mCurrencySign
    }

    override fun getEmplTypeId(idEmplType: Int, nameEmplType: String) {
        mIdEmpType = idEmplType
        mEmplTypeName = nameEmplType
        tv_employment_type.editText?.setText(mEmplTypeName)
    }

    override fun getPayTypeId(idPayType: Int, namePayType: String) {
    }

    override fun getOrgId(idOrg: Int, nameOrg: String) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talent_search_detail)

        tv_salary_currency.setOnClickListener {
            val currencyDialogFragment = CurrencyDialogDialogFragment(this)
            val manager = supportFragmentManager
            currencyDialogFragment.show(manager, "currencyDialog")
        }
        tv_employment_type.editText?.setOnClickListener {
            val emplType = EmploymentTypeDialogDialogFragment(this)
            val manager = supportFragmentManager
            emplType.show(manager, "emplTypeDialog")
        }
        tv_country.setEndIconOnClickListener {
            val countryDialogFragment = CountryDialogDialogFragment(this)
            val manager = supportFragmentManager
            countryDialogFragment.show(manager, "countryDialog")
        }
        tv_city.setEndIconOnClickListener {
            val cityDialogFragment = CityDialogDialogFragment(this)
            val manager = supportFragmentManager
            cityDialogFragment.show(manager, "cityDialog")
        }
        tv_industry.setEndIconOnClickListener {
            val sectorDialogFragment = SpecDialogDialogFragment(this)
            val manager = supportFragmentManager
            sectorDialogFragment.show(manager, "sectorDialog")
        }

        btn_applied.setOnClickListener {
            var salaryExpectString = et_salary.text.toString().trim()
            if(salaryExpectString != "") {
                salaryExpect = salaryExpectString.toInt()
            }

            val intent = Intent(this, MainActivity::class.java)
            if (salaryExpect != 0){
                intent.putExtra("salary", salaryExpect)
            }
            if (mIdCurrency != 0){
                intent.putExtra("currencyId", mIdCurrency)
            }
            if (mIdEmpType!=0){
                intent.putExtra("emplId", mIdEmpType)
            }
            startActivity(intent)
            finish()
        }
        tv_clear_all.setOnClickListener {
            tv_salary.editText?.setText("")
            tv_employment_type.editText?.setText("")
            tv_salary_currency.text = "Currency"
            mIdCurrency = 0
            mIdEmpType = 0
            salaryExpect = 0
        }
        iv_backpressed.setOnClickListener { onBackPressed() }
    }
}