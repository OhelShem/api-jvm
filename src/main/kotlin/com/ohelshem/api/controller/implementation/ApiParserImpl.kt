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
import com.google.gson.*
import com.ohelshem.api.Api
import com.ohelshem.api.Api.ExtraData
import com.ohelshem.api.controller.declaration.ApiParser
import com.ohelshem.api.controller.declaration.ColorProvider
import com.ohelshem.api.model.*
import mu.KLogging
import java.util.*

class ApiParserImpl(private val colorProvider: ColorProvider) : ApiParser {
    private val gson = GsonBuilder()
            .registerTypeAdapter<Change> {
                deserialize {
                    val content = it.json["content"].string
                    Change(it.json["clazz"].int, it.json["hour"].int, content, colorProvider.of(content))
                }

            }
            .registerTypeAdapter<SchoolChange> {
                deserialize {
                    val content = it.json["content"].string
                    SchoolChange(it.json["layer"].int, it.json["clazz"].int, it.json["hour"].int, content, colorProvider.of(content))
                }
            }
            .create()

    override fun parse(data: String): Result<Api.Response, ApiParser.ApiException> {
        try {
            val obj = JsonParser().parse(data).obj
            val errorObject = obj["error"].obj
            val errorCode = errorObject["code"].int
            if (errorCode == 0) {
                val response = MutableResponse(serverUpdateDate = obj["updateDate"].long, changesDate = obj["changesDate"].long, userData = obj.userData())
                response.apply {
                    timetable = obj.timetable()
                    if (userData.isTeacher()) {
                        this.data = ExtraData.Teacher(obj.classes(), obj.primaryClass(), obj.schoolTimetable(), obj.schoolTests(), obj.schoolChanges())

                    } else {
                        this.data = ExtraData.Student(obj.tests(), obj.changes())
                    }
                }
                return Result.Success(response.toResponse())
            } else {
                return Result.error(ApiParser.ApiException(errorCode))
            }
        } catch (e: Exception) {
            logger.error(e) { "Error parsing the response." }
            return Result.error(ApiParser.ApiException(4))
        }
    }

    private fun JsonObject.userData(): UserData {
        return gson.fromJson(obj["userInfo"])
    }

    private fun JsonObject.timetable(): Array<Array<Hour>>? {
        if ("timetable" in obj) {
            val timetableData = obj["timetable"].arrayOrNull
            if (timetableData != null) {
                val lessons = HashMap<String, Int>(20)
                var c = 0
                val timetable = Array(6) { arrayOfNulls<Hour>(MaxHoursADay) }
                val timetableColors = colorProvider.timetableColors
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
                @Suppress("UNCHECKED_CAST")
                return timetable as Array<Array<Hour>>
            } else return null
        } else return null
    }

    private fun JsonObject.changes(): List<Change>? {
        return obj["changes"].let { if (it.isJsonObject) null else gson.fromJson<ArrayList<Change>>(it) }
    }

    private fun JsonObject.tests(): List<Test>? {
        return obj["tests"].let { if (it.isJsonObject) null else gson.fromJson<ArrayList<Test>>(it) }
    }

    private fun JsonObject.schoolTimetable(): List<SchoolHour>? {
        if ("schoolTimetable" in obj) {
            val schoolTimetable = obj["timetable"].arrayOrNull
            if (schoolTimetable != null) {
                val lessons = HashMap<String, Int>(100)
                var c = 0
                val timetableColors = colorProvider.timetableColors
                val timetable = ArrayList<SchoolHour>(schoolTimetable.size())
                schoolTimetable.forEachIndexed { index, it ->
                    val layer = it["layer"].int
                    val clazz = it["class"].int
                    val lesson = it["name"].string
                    val teacher = it["teacher"].string
                    val day = it["day"].int - 1
                    val hour = it["hour"].int - 1
                    var color = lessons[lesson]
                    if (hour < MaxHoursADay) {
                        if (color != null)
                            timetable[index] = SchoolHour(layer, clazz, day, hour, lesson, teacher, color)
                        else {
                            c++
                            if (c == timetableColors.size) c = 0
                            color = timetableColors[c]
                            timetable[index] = SchoolHour(layer, clazz, day, hour, lesson, teacher, color)
                            lessons[lesson] = color
                        }
                    }
                }
                @Suppress("CAST_NEVER_SUCCEEDS")
                return timetable
            } else return null
        } else return null
    }

    private fun JsonObject.schoolTests(): List<SchoolTest>? {
        return obj["tests"].let { if (it.isJsonObject) null else gson.fromJson<ArrayList<SchoolTest>>(it) }
    }

    private fun JsonObject.schoolChanges(): List<SchoolChange>? {
        return obj["changes"].let { if (it.isJsonObject) null else gson.fromJson<ArrayList<SchoolChange>>(it) }
    }

    private fun JsonObject.classes(): List<ClassInfo>? {
        return obj["classes"].let { if (it.isJsonObject) null else gson.fromJson<ArrayList<ClassInfo>>(it) }
    }

    private fun JsonObject.primaryClass(): ClassInfo? {
        return obj["classes"].let { if (it.isJsonObject) null else it.array.firstOrNull { it["prefer"].int == 1 }?.let { gson.fromJson<ClassInfo>(it) } }
    }


    companion object : KLogging() {
        val MaxHoursADay = 11

        private val JsonElement.arrayOrNull: JsonArray?
            get() = if (isJsonObject) null else array

        class MutableResponse(var serverUpdateDate: Long, var changesDate: Long, var userData: UserData, var data: Api.ExtraData? = null,
                              var timetable: Array<Array<Hour>>? = null) {
            fun toResponse() = Api.Response(serverUpdateDate, changesDate, userData, data!!, timetable)
        }

    }
}