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
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.ohelshem.api.controller.declaration.ApiProvider
import com.ohelshem.api.controller.implementation.ApiParserImpl
import com.ohelshem.api.controller.implementation.ApiProviderImpl
import com.ohelshem.api.controller.implementation.RequestsControllerImpl
import com.ohelshem.api.model.AuthData
import com.ohelshem.api.util.BaseTestCase
import com.ohelshem.api.util.getData
import java.util.*
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

/**
 * Test [ApiProvider] integration with remove server. the credentials are stored on `resources` folder, under `credentials.txt`
 * where the first line is the id, and the second line is password
 */
class ApiProviderTest : BaseTestCase() {
    val apiProvider: ApiProvider = ApiProviderImpl(ApiParserImpl(colorProvider), RequestsControllerImpl)


    @org.junit.Test
    fun testProvider() {
        Fuel.testMode {

        }
        val (id, password) = "credentials.txt".getData().split(System.lineSeparator())
        apiProvider.update(AuthData(id, password), 0) {
            it.success {
                if (Calendar.getInstance()[Calendar.DAY_OF_WEEK] != Calendar.SATURDAY) {
                    assertNotNull(it.changes)
                }
                assertNotNull(it.timetable)
                assertNotNull(it.tests)
                assertNotNull(it.messages)
                assertNotNull(it.userData)
                assertNotEquals(0, it.changesDate)
                assertNotEquals(0, it.serverUpdateDate)
            }
            it.failure {
                fail("should not fail with $it")
            }
        }
    }
}