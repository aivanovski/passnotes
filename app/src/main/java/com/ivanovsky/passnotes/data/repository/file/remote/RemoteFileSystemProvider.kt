package com.ivanovsky.passnotes.data.repository.file.remote

import com.ivanovsky.passnotes.data.entity.FileDescriptor
import com.ivanovsky.passnotes.data.entity.OperationError
import com.ivanovsky.passnotes.data.entity.OperationResult
import com.ivanovsky.passnotes.data.entity.RemoteFile
import com.ivanovsky.passnotes.data.repository.db.dao.RemoteFileDao
import com.ivanovsky.passnotes.data.repository.file.*
import com.ivanovsky.passnotes.util.Logger
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class RemoteFileSystemProvider(
    private val authenticator: FileSystemAuthenticator,
    private val dao: RemoteFileDao,
    private val fsType: FSType
) : FileSystemProvider {

    private val cache = RemoteFileCache(dao, fsType)

    override fun getAuthenticator(): FileSystemAuthenticator {
        return authenticator
    }

    override fun getSyncProcessor(): FileSystemSyncProcessor? {
        return null
    }

    override fun listFiles(dir: FileDescriptor?): OperationResult<MutableList<FileDescriptor>> {
        return OperationResult.error(OperationError.newGenericError("Not implemented"))
    }

    override fun getParent(file: FileDescriptor?): OperationResult<FileDescriptor> {
        return OperationResult.error(OperationError.newGenericError("Not implemented"))
    }

    override fun getRootFile(): OperationResult<FileDescriptor> {
        return OperationResult.error(OperationError.newGenericError("Not implemented"))
    }

    override fun openFileForRead(
        file: FileDescriptor?,
        onConflictStrategy: OnConflictStrategy?,
        cacheOperationsEnabled: Boolean
    ): OperationResult<InputStream> {
        return OperationResult.error(OperationError.newGenericError("Not implemented"))
    }

    override fun openFileForWrite(
        file: FileDescriptor?,
        onConflictStrategy: OnConflictStrategy?,
        cacheOperationsEnabled: Boolean
    ): OperationResult<OutputStream> {
        return OperationResult.error(OperationError.newGenericError("Not implemented"))
    }

    override fun exists(file: FileDescriptor?): OperationResult<Boolean> {
        return OperationResult.error(OperationError.newGenericError("Not implemented"))
    }

    override fun getFile(
        path: String?,
        cacheOperationsEnabled: Boolean
    ): OperationResult<FileDescriptor> {
        return OperationResult.error(OperationError.newGenericError("Not implemented"))
    }

    fun onFileUploadFailed(file: RemoteFile, processingUnitUid: UUID) {
        Logger.d(TAG, "onFileUploadFailed: unitUid=%s, file=%s", processingUnitUid, file)

        cache.update(file)

        onFinishProcessingUnit(processingUnitUid)
    }

    fun onFileUploadFinished(file: RemoteFile, processingUnitUid: UUID) {
        Logger.d(TAG, "onFileUploadFinished: unitUid=%s, file=%s", processingUnitUid, file)

        cache.update(file)

        onFinishProcessingUnit(processingUnitUid)
    }

    fun onOfflineWriteFailed(file: RemoteFile, processingUnitUid: UUID) {
        Logger.d(TAG, "onOfflineWriteFailed: unitUid=%s, file=%s", processingUnitUid, file)

        cache.update(file)

        onFinishProcessingUnit(processingUnitUid)
    }

    fun onOfflineWriteFinished(file: RemoteFile, processingUnitUid: UUID) {
        Logger.d(TAG, "onOfflineWriteFinished: unitUid=%s, file=%s", processingUnitUid, file)

        cache.update(file)

        onFinishProcessingUnit(processingUnitUid)
    }

    private fun onFinishProcessingUnit(processingUid: UUID) {
        // TODO: implement
    }

    companion object {
        private val TAG = RemoteFileSystemProvider::class.simpleName
    }
}