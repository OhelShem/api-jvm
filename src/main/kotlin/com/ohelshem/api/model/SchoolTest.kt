package com.ohelshem.api.model

import com.google.gson.annotations.SerializedName

class SchoolTest(val layer: Int, @SerializedName("class") val clazz: Int, date: Long, content: String) : Test(date, content) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        if (!super.equals(other)) return false

        other as SchoolTest

        if (layer != other.layer) return false
        if (clazz != other.clazz) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + layer
        result = 31 * result + clazz
        return result
    }

    override fun toString(): String {
        return "SchoolTest(layer=$layer, clazz=$clazz, ${super.toString()})"
    }


}