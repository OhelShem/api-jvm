package com.ohelshem.api

import com.ohelshem.api.model.*

interface Api {
    class Request(val identity: String, val password: String, val lastUpdateTime: Long, val apiVersion: String)

    class Response(val serverUpdateDate: Long, val changesDate: Long, val userData: UserData, val data: ExtraData,
                   val timetable: Array<Array<Hour>>? = null)

    interface Callback {
        fun onSuccess(response: Response)

        fun onFailure(exception: Exception)
    }

    sealed class ExtraData {
        class Student(val tests: List<Test>? = null, val changes: List<Change>? = null) : ExtraData()

        class Teacher(val classes: List<ClassInfo>? = null, val primaryClass: ClassInfo? = null, val schoolTimetable: List<SchoolHour>? = null,
                      val schoolTests: List<SchoolTest>? = null, val schoolChanges: List<SchoolChange>? = null) : ExtraData()
    }


}