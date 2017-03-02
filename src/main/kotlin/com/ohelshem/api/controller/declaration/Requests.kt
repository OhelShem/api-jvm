package com.ohelshem.api.controller.declaration

import com.github.kittinunf.result.Result

interface Requests {
    fun get(url: String): Pair<Response, Result<String, Exception>>
    fun post(url: String, body: String? = null): Pair<Response, Result<String, Exception>>
    fun post(url: String, params: List<Pair<String, String>>) = post(url, params.encode())

    data class Response(val statusCode: Int, val headers: Map<String, List<String>>)

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
    }
}