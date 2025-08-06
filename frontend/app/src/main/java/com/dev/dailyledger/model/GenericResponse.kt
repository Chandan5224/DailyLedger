package com.dev.dailyledger.model

import com.google.gson.JsonElement

data class GenericResponse(
    val body: Body,
    val header: Header
)

data class Header(
    val code: Int
)

data class Body(
    val error: String,
    val value: JsonElement
)
