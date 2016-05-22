package com.ohelshem.api.controller.declaration

import com.ohelshem.api.Api


interface ApiEngine {
    val apiVersion: String
    val parser: ApiParser

    fun call(request: Api.Request, callback: Api.Callback)

    fun call(identity: String, password: String, lastUpdateTime: Long, callback: Api.Callback) = call(Api.Request(identity, password, lastUpdateTime, apiVersion), callback)


}
