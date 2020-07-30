package com.ivanovsky.passnotes.data.repository.file.remote

data class RemoteFileMetadata(
    val uid: String,
    val lastServerModified: Long,
    val lastClientModified: Long,
    val revision: String,
    val path: String
)