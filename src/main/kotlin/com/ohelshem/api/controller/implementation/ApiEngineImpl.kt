package com.ohelshem.api.controller.implementation

import com.github.kittinunf.result.failure
import com.github.kittinunf.result.flatMap
import com.github.kittinunf.result.success
import com.ohelshem.api.Api
import com.ohelshem.api.controller.declaration.ApiEngine
import com.ohelshem.api.controller.declaration.ApiParser
import com.ohelshem.api.controller.declaration.RequestsController


class ApiEngineImpl(override val parser: ApiParser, private val requestsController: RequestsController) : ApiEngine {
    override val apiVersion: String = "2.0.0"

    override fun call(request: Api.Request, callback: Api.Callback) {
        requestsController.post(ApiEndpoint, headers(request.identity, request.password, request.lastUpdateTime)) { response, result ->
            result.flatMap { parser.parse(it) }.apply {
                success {
                    callback.onSuccess(it)
                }
                failure {
                    callback.onFailure(it)
                }
            }
        }
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