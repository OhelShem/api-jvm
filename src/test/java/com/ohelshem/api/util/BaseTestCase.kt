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

package com.ohelshem.api.util

import com.ohelshem.api.controller.declaration.Logger
import com.ohelshem.api.logger
import com.ohelshem.api.util.WhiteColorProvider
import org.junit.Before

abstract class BaseTestCase {
    val colorProvider = WhiteColorProvider
    @Before
    final fun setUp() {
        registerLoggerAndDefaults()
        setup()
    }

    open fun setup() {

    }

    fun registerLoggerAndDefaults() {
        logger = TestLogger
    }

    object TestLogger : Logger {
        override fun log(data: String) {
            println(data)
        }

        override fun log(data: String, exception: Exception) {
            println(data)
            exception.printStackTrace()
        }

        override fun log(exception: Exception) {
            exception.printStackTrace()
        }

    }

}