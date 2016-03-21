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

import com.github.kittinunf.result.Result
import com.github.salomonbrys.kotson.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.ohelshem.api.controller.declaration.ApiParser
import com.ohelshem.api.controller.declaration.ColorProvider
import com.ohelshem.api.logger
import com.ohelshem.api.model.Change
import com.ohelshem.api.model.Hour
import com.ohelshem.api.model.Message
import com.ohelshem.api.model.Test
import java.util.*

class ApiParserImpl(private val colorProvider: ColorProvider, override var timetableColors: IntArray = intArrayOf()) : ApiParser {
    private val gson = GsonBuilder()
            .registerTypeAdapter<Change> {
                deserialize {
                    val content = it.json["content"].string
                    Change(it.json["clazz"].int, it.json["hour"].int, content, colorProvider.of(content))
                }
            }
            .create()

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun parse(data: String): Result<ApiParser.ParsedData, ApiParser.ApiException> {
        try {
            val obj = JsonParser().parse(data).obj
            val errorObject = obj["error"].obj
            if (errorObject["code"].int == 0) {
                val parsedData = ApiParser.ParsedData()
                parsedData.serverUpdateDate = obj["updateDate"].long
                parsedData.changesDate = obj["changesDate"].long
                if ("changes" in obj) {
                    parsedData.changes = obj["changes"].let { if (it.isJsonObject) null else gson.fromJson<ArrayList<Change>>(it) }
                }
                if ("tests" in obj) {
                    parsedData.tests = obj["tests"].let { if (it.isJsonObject) null else gson.fromJson<ArrayList<Test>>(it) }
                }
                if ("messages" in obj) {
                    parsedData.messages = obj["messages"].let { if (it.isJsonObject) null else gson.fromJson<ArrayList<Message>>(it) }
                }
                if ("userInfo" in obj) {
                    parsedData.userData = gson.fromJson(obj["userInfo"])
                }
                if ("timetable" in obj) {
                    val timetableData = obj["timetable"].array
                    val lessons = HashMap<String, Int>(20)
                    var c = 0
                    val timetable = Array(6) { arrayOfNulls<Hour>(MaxHoursADay) }
                    timetableData.forEach {
                        val lesson = it["name"].string
                        val teacher = it["teacher"].string
                        val day = it["day"].int - 1
                        val hour = it["hour"].int - 1
                        val color = lessons[lesson]
                        if (hour < MaxHoursADay) {
                            if (color != null)
                                timetable[day][hour] = Hour(lesson, teacher, color)
                            else {
                                c++
                                if (c == timetableColors.size) c = 0
                                timetable[day][hour] = Hour(lesson, teacher, timetableColors[c])
                                lessons[lesson] = timetableColors[c]
                            }
                        }
                    }
                    parsedData.timetable = timetable as Array<Array<Hour>>
                }
                return Result.Success(parsedData)
            } else {
                return Result.error(ApiParser.ApiException(errorObject.obj["code"].int))
            }
        } catch (e: Exception) {
            logger.log(e)
            return Result.error(ApiParser.ApiException(4))
        }
    }

    companion object {
        val MaxHoursADay = 11
    }
}