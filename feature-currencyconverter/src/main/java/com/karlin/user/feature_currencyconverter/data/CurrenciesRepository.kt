package com.karlin.user.feature_currencyconverter.data

import com.karlin.user.common.util.NetworkHandler
import com.karlin.user.feature_currencyconverter.data.entities.CurrencyEntity
import com.karlin.user.feature_currencyconverter.exception.CurrencyFailure
import javax.inject.Inject

/**
// Created by Karlin Dmitriy on 16.02.2020.
// PackageName com.karlin.user.feature_currencyconverter.data
 */

const val RUB_CURRENCY = "RUB"

interface CurrenciesRepository {
    suspend fun getCurrencies(base: String?): CurrencyEntity

    class Impl @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val currenciesLocalDataSource: CurrenciesLocalDataSource,
        private val currenciesRemoteDataSource: CurrenciesRemoteDataSource
    ) : CurrenciesRepository {
        override suspend fun getCurrencies(base: String?): CurrencyEntity =
            if (networkHandler.isConnected) {
                currenciesRemoteDataSource.getCurrencies(base ?: RUB_CURRENCY).also {
                    currenciesLocalDataSource.saveCurrencies(it)
                }
            } else {
                currenciesLocalDataSource.getCurrencies(base ?: RUB_CURRENCY)
                    ?: throw CurrencyFailure.ExceptionNetworkConnection
            }
    }
}
