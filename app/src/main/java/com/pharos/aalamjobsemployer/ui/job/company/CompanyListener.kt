package com.pharos.aalamjobsemployer.ui.job.company

interface CompanyListener {
    fun createCompanySuccess(id: Int)
    fun createCompanyFailed(code: Int?)
}