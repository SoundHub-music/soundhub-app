package com.soundhub.data.model

abstract class MusicEntity<T> {
	abstract val id: T
	abstract var name: String?
	abstract var cover: String?
}
