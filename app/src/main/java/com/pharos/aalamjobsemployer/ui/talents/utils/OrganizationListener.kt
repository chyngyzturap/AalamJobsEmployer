package com.pharos.aalamjobsemployer.ui.talents.utils

import com.pharos.aalamjobsemployer.data.responses.dialog.OrganizationResponse


interface OrganizationListener {
    fun setOrganization(org: OrganizationResponse)
    fun getOrgError(code: Int?)
}