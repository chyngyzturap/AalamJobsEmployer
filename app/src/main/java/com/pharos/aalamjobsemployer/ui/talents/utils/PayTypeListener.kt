package com.pharos.aalamjobsemployer.ui.talents.utils

import com.pharos.aalamjobsemployer.data.responses.dialog.PaymentTypeResponse


interface PayTypeListener {
    fun setPayTypes(payTypes: PaymentTypeResponse)
    fun getPayTypesError(code: Int?)
}