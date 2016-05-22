/*
 * Copyright 2016 Yoav Sternberg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ohelshem.api

import com.github.kittinunf.fuel.Fuel
import com.ohelshem.api.controller.declaration.ApiEngine
import com.ohelshem.api.util.BaseTestCase
import com.ohelshem.api.util.getData
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

/**
 * Test [ApiEngine] integration with remove server. the credentials are stored on `resources` folder, under `credentials.txt`
 * where the first line is the id, and the second line is password
 */
class ApiEngineTest : BaseTestCase() {
    val apiEngine: ApiEngine = ApiFactory.create(colorProvider)


    @org.junit.Test
    fun testProvider() {
        Fuel.testMode {

        }
        val (id, password) = "credentials.txt".getData().split(System.lineSeparator())
        apiEngine.call(id, password, 0, object : Api.Callback {
            override fun onSuccess(response: Api.Response) {
                assertNotNull(response.userData)
                assertNotEquals(0, response.changesDate)
                assertNotEquals(0, response.serverUpdateDate)
            }

            override fun onFailure(exception: Exception) {
                fail("should not fail with $exception")
            }

        })
    }
}