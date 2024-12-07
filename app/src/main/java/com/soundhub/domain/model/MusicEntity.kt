package com.soundhub.domain.model

import java.io.Serializable

abstract class MusicEntity<T> : Serializable {
	abstract val id: T
	abstract var name: String?
	abstract var cover: String?
}
