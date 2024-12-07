package com.soundhub.domain.model

import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

abstract class ContentEntity : Serializable {
	abstract val id: UUID
	abstract var author: User?
	abstract val createdAt: LocalDateTime
	abstract var content: String
}