package com.ohelshem.api.controller.implementation

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.flatMap
import com.ohelshem.api.Api
import com.ohelshem.api.Api.Response
import com.ohelshem.api.controller.declaration.ApiEngine
import com.ohelshem.api.controller.declaration.ApiParser
import com.ohelshem.api.controller.declaration.Requests


class ApiEngineImpl(override val parser: ApiParser, private val requests: Requests) : ApiEngine {
    override val apiVersion: String = "2.0.1"

    override suspend fun call(request: Api.Request): Result<Response, Exception> {
        val (_, result) =  requests.post(ApiEndpoint, headers(request.identity, request.password, request.lastUpdateTime))
        return result.flatMap(parser::parse)
    }

    private fun headers(id: String, password: String, lastUpdateTime: Long) = listOf(
            "identity" to id,
            "password" to password,
            "lastUpdateTime" to lastUpdateTime.toString(),
            "apiVersion" to apiVersion)

    companion object {
        private const val ApiEndpoint = "http://ohel-shem.com/portal6/system/mobile_api_v2/api.php"
    }
}