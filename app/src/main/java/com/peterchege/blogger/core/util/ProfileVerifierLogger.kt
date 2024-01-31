package com.peterchege.blogger.core.util

import androidx.profileinstaller.ProfileVerifier
import androidx.work.await
import com.peterchege.blogger.core.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ProfileVerifierLogger @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
) {
    companion object {
        private val TAG = ProfileVerifierLogger::class.java.simpleName
    }

    operator fun invoke() = scope.launch {
        val status = ProfileVerifier.getCompilationStatusAsync().await()
        Timber.tag(TAG).d("Status code: %s", status.profileInstallResultCode)
        when {
            status.isCompiledWithProfile -> {
                Timber.tag(TAG).d(message ="App compiled with profile")
            }
            status.hasProfileEnqueuedForCompilation() -> {
                Timber.tag(TAG).d(message = "Profile enqueued for compilation")
            }
            else -> {
                Timber.tag(TAG).d(message ="Profile not compiled nor enqueued")
            }
        }

    }
}