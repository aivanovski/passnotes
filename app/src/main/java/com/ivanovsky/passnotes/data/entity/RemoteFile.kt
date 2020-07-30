package com.ivanovsky.passnotes.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ivanovsky.passnotes.data.repository.file.FSType

@Entity(tableName = "remote_file")
data class RemoteFile(

    @ColumnInfo(name = "fs_type")
    val fsType: FSType,

    @ColumnInfo(name = "locally_modified")
    val isLocallyModified: Boolean = false,

    @ColumnInfo(name = "uploaded")
    val isUploaded: Boolean = false,

    @ColumnInfo(name = "upload_failed")
    val isUploadFailed: Boolean = false,

    @ColumnInfo(name = "downloading")
    val isDownloading: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,

    @ColumnInfo(name = "retry_count")
    val retryCount: Int = 0,

    @ColumnInfo(name = "last_retry_timestamp")
    val lastRetryTimestamp: Long = 0,

    @ColumnInfo(name = "last_download_timestamp")
    val lastDownloadTimestamp: Long = 0,

    @ColumnInfo(name = "last_modification_timestamp")
    val lastModificationTimestamp: Long = 0,

    @ColumnInfo(name = "local_path")
    val localPath: String,

    @ColumnInfo(name = "remote_path")
    val remotePath: String,

    @ColumnInfo(name = "uid")
    val uid: String,

    @ColumnInfo(name = "revision")
    val revision: String? = null
)