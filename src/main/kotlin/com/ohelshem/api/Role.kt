package com.ohelshem.api

import com.google.gson.annotations.SerializedName

enum class Role {
    @SerializedName("administrator")
    Admin,
    @SerializedName("coadmin")
    CoAdmin,
    @SerializedName("layeradmin")
    LayerAdmin,
    @SerializedName("rakaz")
    SubjectAdmin,
    @SerializedName("teacher")
    Teacher,
    @SerializedName("student")
    Student
}