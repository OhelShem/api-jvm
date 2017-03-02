package com.ohelshem.api.controller.declaration

import com.github.kittinunf.result.Result
import com.ohelshem.api.Api.Request
import com.ohelshem.api.Api.Response


interface ApiEngine {
    val apiVersion: String
    val parser: ApiParser

    fun call(request: Request): Result<Response, Exception>

    fun call(identity: String, password: String, lastUpdateTime: Long) = call(Request(identity, password, lastUpdateTime, apiVersion))
}
