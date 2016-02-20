/*
 * Copyright 2016 Yoav Sternberg.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.ohelshem.api.controller.implementation

import com.ohelshem.api.controller.declaration.ApiController
import com.ohelshem.api.model.AuthData
import com.ohelshem.api.controller.declaration.ApiController.Api
import com.ohelshem.api.controller.declaration.ApiDatabase
import com.ohelshem.api.controller.declaration.ApiParser
import com.ohelshem.api.controller.declaration.RequestsController
import com.ohelshem.api.logger
import com.ohelshem.api.model.UpdateError
import java.util.HashMap
import java.util.LinkedList

class ApiControllerImpl(override val database: ApiDatabase, override val parser: ApiParser, private val requestsController: RequestsController) : ApiController {
    private var networkProvider: () -> Boolean = { true }
    override lateinit var authData: AuthData
    override var isBusy: Boolean = false
        private set

    private val callbacks: MutableMap<Int, ApiController.Callback> = HashMap(2)

    override fun set(id: Int, callback: ApiController.Callback) {
        callbacks += id to callback
    }

    override fun minusAssign(id: Int) {
        callbacks.remove(id)
    }

    override fun setNetworkAvailabilityProvider(provider: () -> Boolean) {
        networkProvider = provider
    }

    override fun update(): Boolean {
        if (!isBusy) {
            if (networkProvider()) {
                isBusy = true
                requestsController.post(ApiEndpoint, headers()) { response, result ->
                    result.fold(OnUpdateSuccess, OnUpdateFail)
                    isBusy = false
                }
                return true
            } else forEach { it.onFail(UpdateError.Connection) }
        }
        return false
    }

    private fun headers() = listOf("identity" to authData.id, "password" to authData.password, "lastUpdateTime" to database.serverUpdateDate.toString())

    private val OnUpdateSuccess = { data: String ->
        try {
            val result = parser.parse(data)
            result.fold({
                val apis: MutableList<Api> = LinkedList()
                it.apply {
                    database.changesDate = changesDate
                    database.serverUpdateDate = serverUpdateDate
                    database.updateDate = System.currentTimeMillis()
                    userData?.let { database.userData = it; apis += Api.UserData }
                    changes?.let { database.changes = it; apis += Api.Changes }
                    tests?.let { database.tests = it; apis += Api.Tests }
                    messages?.let { database.messages = it; apis += Api.Messages }
                    timetable?.let { database.timetable = it; apis += Api.Timetable }
                }
                if (apis.isEmpty()) forEach { it.onFail(UpdateError.NoData) }
                else forEach { it.onSuccess(apis) }
            }) {
                val code = it.error
                logger.log("Failed to update. Code: $code")
                when (code) {
                    1, 2 -> {
                        logger.log(IllegalStateException("Should not return those error codes"))
                        forEach { it.onFail(UpdateError.Exception) }
                    }
                    3 -> forEach { it.onFail(UpdateError.Login) }
                    else -> forEach { it.onFail(UpdateError.Exception) }
                }
            }
        } catch (e: Exception) {
            logger.log(e)
            forEach { it.onFail(UpdateError.Exception) }
        }
    }

    private val OnUpdateFail = { exception: Exception ->
        logger.log(exception)
        forEach { it.onFail(UpdateError.Exception) }
    }

    private inline fun forEach(callback: (ApiController.Callback) -> Unit) {
        callbacks.values.forEach(callback)
    }

    companion object {
        private const val ApiEndpoint = "http://ohel-shem.com/portal6/system/mobile_api_v1/api.php"
    }
}