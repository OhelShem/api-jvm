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

package com.ohelshem.api.model

/**
 * Represent a school hour. Every school hour in the user's timetable has a [name], [teacher] and a [color].
 *
 * The [name] & [teacher] may be not so specific because the app is based on the school timetable,
 * which is not customisable per student, but generalized.
 *
 * The [color] field should be equal across hour with same [name].
 *
 * An hour can also be empty. In that case, the hour's [name] will be empty string.
 */
open class Hour(val name: String = "", val teacher: String = "", val color: Int = 0) {
    /**
     * Checks if the hour is empty.
     */
    fun isEmpty(): Boolean = name.trim().isEmpty()

    /**
     * Checks if an hour is equal to another hour.
     * Since [Change] extends [Hour], an hour may be equal to a change, but the change may not be equal to an hour.
     *
     * @return True if both are hours and both have same [Hour.name]
     */
    override fun equals(other: Any?): Boolean = other is Hour && other.name == name && other.teacher == teacher

    override fun toString(): String = "Hour{name: $name :: teacher: $teacher :: color: $color}"

    override fun hashCode(): Int {
        var result = name.hashCode()
        result += 31 * result + teacher.hashCode()
        result += 31 * result + color
        return result
    }
    companion object {
        val Empty = Hour()
    }
}