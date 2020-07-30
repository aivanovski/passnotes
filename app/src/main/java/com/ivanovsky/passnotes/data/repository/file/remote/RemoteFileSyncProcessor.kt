package com.ivanovsky.passnotes.data.repository.file.remote

import com.ivanovsky.passnotes.data.entity.FileDescriptor
import com.ivanovsky.passnotes.data.entity.OperationResult
import com.ivanovsky.passnotes.data.repository.file.FileSystemSyncProcessor
import com.ivanovsky.passnotes.data.repository.file.OnConflictStrategy
import com.ivanovsky.passnotes.data.repository.file.SyncStrategy
import com.ivanovsky.passnotes.domain.FileHelper

class RemoteFileSyncProcessor(
    private val provider: RemoteFileSystemProvider,
    private val cache: RemoteFileCache,
    private val fileHelper: FileHelper
) : FileSystemSyncProcessor {

    override fun getLocallyModifiedFiles(): MutableList<FileDescriptor> {
        val modifiedFiles = mutableListOf<FileDescriptor>()

        val files = cache.getLocallyModifiedFiles()
        for (file in files) {
            val descriptor = FileDescriptor()

            descriptor.fsType = file.fsType
            descriptor.uid = file.uid
            descriptor.path = file.remotePath
            descriptor.modified = file.lastModificationTimestamp

            modifiedFiles.add(descriptor)
        }

        return modifiedFiles
    }

    override fun process(
        file: FileDescriptor?,
        syncStrategy: SyncStrategy?,
        onConflictStrategy: OnConflictStrategy?
    ): OperationResult<FileDescriptor> {
    }
}