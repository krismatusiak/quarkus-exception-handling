package com.qwerty

import com.fasterxml.jackson.annotation.JsonProperty

data class MyDto(
    @JsonProperty("not_required") val notRequired: String?,
    @JsonProperty("required") val required: String,
)
