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

import com.ohelshem.api.controller.declaration.ApiController
import com.ohelshem.api.controller.declaration.ApiDatabase
import com.ohelshem.api.controller.implementation.ApiControllerImpl
import com.ohelshem.api.controller.implementation.ApiParserImpl
import com.ohelshem.api.model.*
import com.ohelshem.api.util.BaseTestCase
import com.ohelshem.api.util.RequestsControllerBlockingImpl
import com.ohelshem.api.util.getData
import kotlin.test.*

/**
 * Test [ApiController] integration with remove server. the credentials are stored on `resources` folder, under `credentials.txt`
 * where the first line is the id, and the second line is password
 */
class ApiControllerTest: BaseTestCase() {
    val apiController: ApiController = ApiControllerImpl(StubApiDatabase, ApiParserImpl(colorProvider).apply {
        timetableColors = intArrayOf(0xF44336, 0xE91E63, 0x9C27B0, 0x673AB7, 0x3F51B5, 0x2196F3, 0x03A9F4,
                0x00BCD4, 0x009688, 0x4CAF50, 0x8BC34A, 0xCDDC39, 0xFF9800, 0xFF5722, 0x795548, 0x607D8B)
    }, RequestsControllerBlockingImpl)


    @org.junit.Test
    fun testController() {
        apiController.setNetworkAvailabilityProvider { true }
        apiController[1] = object : ApiController.Callback {
            override fun onSuccess(apis: List<ApiController.Api>) {
                assertEquals(5, apis.size)
                assertNotNull(StubApiDatabase.changes)
                assertNotNull(StubApiDatabase.timetable)
                assertNotNull(StubApiDatabase.tests)
                assertNotNull(StubApiDatabase.messages)
                assertNotNull(StubApiDatabase.userData)
                assertNotEquals(0, StubApiDatabase.changesDate)
                assertNotEquals(0, StubApiDatabase.serverUpdateDate)
                assertNotEquals(0, StubApiDatabase.updateDate)
            }

            override fun onFail(error: UpdateError) {
                fail("should not fail with $error")
            }
        }
        val (id, password) = "credentials.txt".getData().split(System.lineSeparator())
        apiController.authData = AuthData(id, password)
        assertTrue { apiController.update() }
    }


    object StubApiDatabase: ApiDatabase {
        override var updateDate: Long = 0
        override var serverUpdateDate: Long = 0
        override var changesDate: Long = 0
        override lateinit var userData: UserData
        override var timetable: Array<Array<Hour>>? = null
        override var changes: List<Change>? = null
        override var tests: List<Test>? = null
        override var messages: List<Message>? = null
    }


}