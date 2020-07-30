package com.ivanovsky.passnotes.data.repository.file.remote

import com.ivanovsky.passnotes.data.entity.RemoteFile
import com.ivanovsky.passnotes.data.repository.file.remote.exception.RemoteApiException

interface RemoteApiClient {

    @Throws(RemoteApiException::class)
    fun uploadFileOrThrow(file: RemoteFile): RemoteFileMetadata
}