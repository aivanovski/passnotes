package com.ivanovsky.passnotes.data.repository.file.remote

import com.ivanovsky.passnotes.data.entity.RemoteFile
import com.ivanovsky.passnotes.data.repository.file.RemoteFileOutputStream
import com.ivanovsky.passnotes.data.repository.file.remote.exception.RemoteApiException
import com.ivanovsky.passnotes.data.repository.file.remote.exception.RemoteApiNetworkException
import com.ivanovsky.passnotes.util.DateUtils
import com.ivanovsky.passnotes.util.Logger
import java.io.*
import java.util.*

class RemoteApiFileOutputStream(
    private val provider: RemoteFileSystemProvider,
    private val client: RemoteApiClient,
    private val file: RemoteFile,
    private val processingUnitUid: UUID
) : RemoteFileOutputStream() {

    private var failed = false;
    private val outFile = File(file.localPath)
    private var out: OutputStream? = null

    override fun getOutputFile(): File {
        return outFile
    }

    @Throws(IOException::class)
    override fun write(b: Int) {
        if (failed) {
            return
        }

        if (out == null) {
            out = BufferedOutputStream(FileOutputStream(outFile))
        }

        val out = out ?: throw IllegalStateException("OutputStream wasn't initialized")

        try {
            out.write(b)
        } catch (e: IOException) {
            Logger.printStackTrace(e)
            failAndThrow(e)
        }
    }

    @Throws(IOException::class)
    private fun failAndThrow(reason: Exception) {
        fail()
        throw IOException(reason)
    }

    private fun fail() {
        failed = true
        val modifiedFile = file.copy(isUploadFailed = true)
        provider.onFileUploadFailed(modifiedFile, processingUnitUid)
    }

    @Throws(IOException::class)
    override fun flush() {
        if (failed) {
            return
        }

        val out = out ?: return

        try {
            out.flush()
        } catch (e: IOException) {
            Logger.printStackTrace(e)
            failAndThrow(e)
        }
    }

    @Throws(IOException::class)
    override fun close() {
        if (failed) {
            return
        }

        val metadata: RemoteFileMetadata
        try {
            metadata = client.uploadFileOrThrow(file)
        } catch (e: RemoteApiNetworkException) {
            Logger.printStackTrace(e)
            provider.onOfflineWriteFinished(file, processingUnitUid)
            throw IOException(e)

        } catch (e: RemoteApiException) {
            Logger.printStackTrace(e)
            fail()
            throw IOException(e)
        }

        // TODO: remove "!!"
        val modifiedFile = file.copy(
            isUploadFailed = false,
            isLocallyModified = false,
            isUploaded = true,
            lastModificationTimestamp = DateUtils.anyLastTimestamp(
                metadata.lastServerModified,
                metadata.lastClientModified
            )!!,
            lastDownloadTimestamp = System.currentTimeMillis(),
            revision = metadata.revision,
            uid = metadata.uid,
            remotePath = metadata.path
        )
        provider.onFileUploadFinished(modifiedFile, processingUnitUid)
    }
}