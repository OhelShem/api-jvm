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

package com.ohelshem.api

import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.ohelshem.api.controller.declaration.ApiParser
import com.ohelshem.api.controller.implementation.ApiParserImpl
import com.ohelshem.api.util.BaseTestCase
import com.ohelshem.api.util.getData
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

class ApiParserTest : BaseTestCase() {
    val parser: ApiParser = ApiParserImpl(colorProvider)

    override fun setup() {
        parser.timetableColors = intArrayOf(0xF44336, 0xE91E63, 0x9C27B0, 0x673AB7, 0x3F51B5, 0x2196F3, 0x03A9F4,
                0x00BCD4, 0x009688, 0x4CAF50, 0x8BC34A, 0xCDDC39, 0xFF9800, 0xFF5722, 0x795548, 0x607D8B)
    }

    @Test
    fun testParsingData() {
        val data = parser.parse("response.json".getData())
        data.failure {
            fail("Exception should not be thrown from the method")
        }
        data.success {
            assertEquals(1454861839L, it.serverUpdateDate)
            assertNotNull(it.userData).apply {
                assertEquals(42, id)
                assertEquals("123456789", identity)
                assertEquals("יואב", privateName)
                assertEquals("שטרנברג", familyName)
                assertEquals(1, gender)
                assertEquals(4, clazz)
                assertEquals(11, layer)
                assertEquals("522324577", phone)
                assertEquals("1999-05-18", birthday)
                assertEquals(email, "yoav.sternberg@gmail.com")
            }
            assertEquals(1454796000L, it.changesDate)
            assertNotNull(it.changes).apply {
                assertEquals(24, size)
            }
        }
    }
}