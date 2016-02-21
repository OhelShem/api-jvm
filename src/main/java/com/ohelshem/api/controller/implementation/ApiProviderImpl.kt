package com.ohelshem.api.controller.implementation

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.flatMap
import com.ohelshem.api.controller.declaration.ApiParser
import com.ohelshem.api.controller.declaration.ApiProvider
import com.ohelshem.api.controller.declaration.RequestsController
import com.ohelshem.api.model.AuthData

class ApiProviderImpl(override val parser: ApiParser, private val requestsController: RequestsController) : ApiProvider {
    override fun update(authData: AuthData, lastUpdateTime: Long, callback: (Result<ApiParser.ParsedData, Exception>) -> Unit) {
        requestsController.post(ApiEndpoint, headers(authData, lastUpdateTime)) { response, result ->
            callback(result.flatMap { parser.parse(it) })
        }
    }

    private fun headers(authData: AuthData, lastUpdateTime: Long) = listOf(
            "identity" to authData.id,
            "password" to authData.password,
            "lastUpdateTime" to lastUpdateTime.toString())


    companion object {
        private const val ApiEndpoint = "http://ohel-shem.com/portal6/system/mobile_api_v1/api.php"
    }
}