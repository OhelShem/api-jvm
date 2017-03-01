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

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.ohelshem.api.controller.declaration.Requests
import com.ohelshem.api.controller.declaration.Requests.Response

object RequestsImpl : Requests {
    private const val DefaultTimeout = 5000

    override suspend fun get(url: String): Pair<Response, Result<String, Exception>> {
        val (_, response, result) = url.httpGet().timeout(DefaultTimeout).responseString()

        return Response(response.httpStatusCode, response.httpResponseHeaders) to result
    }

    override suspend fun post(url: String, body: String?): Pair<Response, Result<String, Exception>> {
        val (_, response, result) = url.httpPost().timeout(DefaultTimeout).apply { if (body != null) body(body) }.responseString()

        return Response(response.httpStatusCode, response.httpResponseHeaders) to result
    }
}