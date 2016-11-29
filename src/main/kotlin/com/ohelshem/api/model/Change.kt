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
 * Represents a change from the changes table.
 * A change belongs to a [clazz], at a specific [hour].
 * A change has a [content] and a [color].
 */
open class Change(val clazz: Int, val hour: Int, val content: String, val color: Int) {

    override fun equals(other: Any?): Boolean {
        return other is Change && clazz == other.clazz && hour == other.hour && content == other.content && color == other.color
    }

    override fun hashCode(): Int {
        var result = clazz
        result = 31 * result + hour
        result = 31 * result + content.hashCode()
        result = 31 * result + color
        return result
    }

    override fun toString(): String {
        return "Change(clazz=$clazz, hour=$hour, content='$content', color=$color)"
    }


}