package com.ohelshem.api.model

import com.google.gson.annotations.SerializedName

data class ClassInfo(val layer: Int, @SerializedName("class") val clazz: Int)