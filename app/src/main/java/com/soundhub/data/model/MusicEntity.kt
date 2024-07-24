package com.soundhub.data.model

interface MusicEntity<T> {
    val id: T
    var name: String?
    var cover: String?
}
