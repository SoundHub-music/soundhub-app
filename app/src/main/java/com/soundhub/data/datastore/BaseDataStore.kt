package com.soundhub.data.datastore

import kotlinx.coroutines.flow.Flow

abstract class BaseDataStore<S, K> {
	protected abstract val preferenceKeys: K

	abstract suspend fun updateCreds(creds: S?)
	abstract fun getCreds(): Flow<S>
	abstract suspend fun clear()
}