package com.soundhub.data.api.requests

import java.util.UUID

data class CompatibleUsersRequestBody(
    val listUsersCompareWith: List<UUID> = emptyList()
)