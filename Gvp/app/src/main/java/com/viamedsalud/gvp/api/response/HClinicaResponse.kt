package com.viamedsalud.gvp.api.response


import com.google.gson.annotations.SerializedName

data class HClinicaResponse(
    @SerializedName("message")
    var message: String
)