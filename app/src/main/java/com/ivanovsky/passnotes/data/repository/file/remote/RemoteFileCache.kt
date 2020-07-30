package com.ivanovsky.passnotes.data.repository.file.remote

import com.ivanovsky.passnotes.data.entity.RemoteFile
import com.ivanovsky.passnotes.data.repository.db.dao.RemoteFileDao
import com.ivanovsky.passnotes.data.repository.file.FSType

class RemoteFileCache(
    private val dao: RemoteFileDao,
    private val fsType: FSType
) {

    fun getByRemotePath(remotePath: String): RemoteFile? {
        return dao.findByRemotePath(remotePath, fsType.textName)
    }

    fun getByUid(uid: String): RemoteFile? {
        return dao.findByUid(uid, fsType.textName)
    }

    fun put(file: RemoteFile) {
        if (file.fsType != fsType) {
            throw IllegalArgumentException(
                "Incorrect fsType: required=$fsType, actual=${file.fsType}"
            )
        }

        dao.insert(file)
    }

    fun update(file: RemoteFile) {
        dao.update(file)
    }

    fun getLocallyModifiedFiles(): List<RemoteFile> {
        return dao.getAll().filter { file -> fsType == file.fsType && file.isLocallyModified }
    }
}