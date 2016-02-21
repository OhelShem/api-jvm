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

import com.ohelshem.api.controller.declaration.ApiProvider
import com.ohelshem.api.controller.declaration.ColorProvider
import com.ohelshem.api.controller.implementation.ApiParserImpl
import com.ohelshem.api.controller.implementation.ApiProviderImpl
import com.ohelshem.api.controller.implementation.ColorProviderImpl
import com.ohelshem.api.controller.implementation.RequestsControllerImpl

object ApiFactory {
    @JvmStatic
    fun create(colorProvider: ColorProvider): ApiProvider = ApiProviderImpl(ApiParserImpl(colorProvider), RequestsControllerImpl)

    @JvmStatic
    fun defaultColorProvider(defaultColor: Int, filters: List<Pair<Int, String>>) = ColorProviderImpl(filters, defaultColor)

    @JvmStatic
    fun defaultColorProvider(defaultColor: Int, filters: Map<String, Int>) = ColorProviderImpl(filters.entries.map { it.value to it.key }, defaultColor)
}