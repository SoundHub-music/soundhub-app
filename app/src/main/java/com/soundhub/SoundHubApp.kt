package com.soundhub

import android.app.Application
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SoundHubApp: Application()

@GlideModule
class GlideApp: AppGlideModule() {}