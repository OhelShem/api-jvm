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

package com.ohelshem.api.controller.declaration

import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result

/**
 * An interface for classes that control the http connection to the web.
 */
interface RequestsController {
    fun get(url: String, callback: (Response, Result<String, Exception>) -> Unit)
    fun post(url: String, body: String? = null, callback: (Response, Result<String, Exception>) -> Unit)
    fun post(url: String, params: List<Pair<String, String>>, callback: (Response, Result<String, Exception>) -> Unit) = post(url, params.encode(), callback)

    companion object {
        fun List<Pair<String, String>>.encode(): String {
            if (isEmpty()) return ""
            var output = first().let { it.first + ValueSeparator + it.second }
            if (size > 1) {
                for (i in 1..(size - 1)) {
                    output += ParamSeparator + get(i).let { it.first + ValueSeparator + it.second }
                }
            }
            return output
        }
        const val ValueSeparator = "="
        const val ParamSeparator = "&"

        const val DefaultTimeout = 5000
    }
}