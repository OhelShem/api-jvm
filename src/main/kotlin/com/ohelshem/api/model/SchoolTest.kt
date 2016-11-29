package com.ohelshem.api.model

import com.google.gson.annotations.SerializedName

class SchoolTest(override val layer: Int, @SerializedName("class") override val clazz: Int, date: Long, content: String) : Test(date, content), SchoolModel {
    override fun equals(other: Any?): Boolean {
        return other is SchoolTest && super.equals(other) && layer == other.layer && clazz == other.clazz
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