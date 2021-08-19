package com.pharos.aalamjobsemployer.data.local.db.cv.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_table")
data class CompanyInfo(
    val idCompany: Int
){
    @PrimaryKey
    var id: Int = 1
}