package com.ohelshem.api.model

import com.google.gson.annotations.SerializedName

class SchoolHour(override val layer: Int, @SerializedName("class") override val clazz: Int, val day: Int, val hour: Int, name: String = "",
                 teacher: String = "", color: Int = 0) : Hour(name, teacher, color), SchoolModel {

    override fun equals(other: Any?): Boolean {
        return other is SchoolHour && super.equals(other) && layer == other.layer && clazz == other.clazz && day == other.day && hour == other.hour
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + layer
        result = 31 * result + clazz
        result = 31 * result + day
        result = 31 * result + hour
        return result
    }

    override fun toString(): String {
        return "SchoolHour(layer=$layer, clazz=$clazz, day=$day, hour=$hour, ${super.toString()})"
    }


}