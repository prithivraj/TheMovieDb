package com.robdir.themoviedb.core

import java.text.NumberFormat
import java.util.Currency
import javax.inject.Inject

private const val DEFAULT_CURRENCY = "USD"

class DeviceLocaleProvider @Inject constructor() : LocaleProvider {

    override fun getNumberFormat(): NumberFormat =
        NumberFormat.getNumberInstance().apply {
            currency = Currency.getInstance(DEFAULT_CURRENCY)
        }
}
