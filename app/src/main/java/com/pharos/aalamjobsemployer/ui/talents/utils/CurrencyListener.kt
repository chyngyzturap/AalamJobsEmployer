package com.pharos.aalamjobsemployer.ui.talents.utils

import com.pharos.aalamjobsemployer.data.responses.dialog.CurrencyResponse


interface CurrencyListener {


    fun setCurrency(currency: CurrencyResponse)
    fun getCurrencyError(code: Int?)


}