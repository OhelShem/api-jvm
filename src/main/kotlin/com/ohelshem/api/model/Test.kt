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


open class Test(val date: Long, val content: String) {
    override fun equals(other: Any?): Boolean {
        return other is Test && date == other.date && content == other.content
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + content.hashCode()
        return result
    }

    override fun toString(): String {
        return "Test(date=$date, content='$content')"
    }


}