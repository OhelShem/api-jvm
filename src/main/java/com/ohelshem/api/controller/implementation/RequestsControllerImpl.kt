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

import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.ohelshem.api.controller.declaration.RequestsController

object RequestsControllerImpl : RequestsController {
    override fun get(url: String, callback: (Response, Result<String, Exception>) -> Unit) {
        url.httpGet().timeout(RequestsController.DefaultTimeout).responseString { request, response, result ->
            callback(response, result)
        }
    }

    override fun post(url: String, body: String?, callback: (Response, Result<String, Exception>) -> Unit) {
        url.httpPost().timeout(RequestsController.DefaultTimeout).apply { if (body != null) body(body) }.responseString { request, response, result ->
            callback(response, result)
        }
    }
}