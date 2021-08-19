package com.pharos.aalamjobsemployer.ui.talents.utils

interface SearchListener {

    fun getCountryId(idCountry: Int, nameCountry: String)
    fun getCityId(idCity: Int, nameCity: String)
    fun getSpecId(idSector: Int, nameSector: String)
    fun getCurrencySign(idCurrency: Int, currencySign: String)
    fun getEmplTypeId(idEmplType: Int, nameEmplType: String)
    fun getPayTypeId(idPayType: Int, namePayType: String)
    fun getOrgId(idOrg: Int, nameOrg: String)

}