package com.ohelshem.api.controller.declaration

import com.github.kittinunf.result.Result
import com.ohelshem.api.model.AuthData

interface ApiProvider {
    val parser: ApiParser

    fun update(authData: AuthData, lastUpdateTime: Long, callback: (Result<ApiParser.ParsedData, Exception>) -> Unit)
}