package com.pharos.aalamjobsemployer.data.local.db.cv.dao

import androidx.room.*
import com.pharos.aalamjobsemployer.data.local.db.cv.entities.CompanyInfo

@Dao
interface CompanyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyInfo(companyInfo: CompanyInfo)

    @Query("DELETE FROM company_table")
    suspend fun deleteAllFromCompany()

    @Query("SELECT idCompany FROM company_table")
    fun getIdCompany(): Int?
}