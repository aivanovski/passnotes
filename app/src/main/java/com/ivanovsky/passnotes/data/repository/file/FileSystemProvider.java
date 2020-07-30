package com.ivanovsky.passnotes.data.repository.file;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ivanovsky.passnotes.data.entity.FileDescriptor;
import com.ivanovsky.passnotes.data.entity.OperationResult;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface FileSystemProvider {

    @NonNull
	FileSystemAuthenticator getAuthenticator();

    @Nullable
	FileSystemSyncProcessor getSyncProcessor();

	@NonNull
	OperationResult<List<FileDescriptor>> listFiles(FileDescriptor dir);

	@NonNull
	OperationResult<FileDescriptor> getParent(FileDescriptor file);

	@NonNull
	OperationResult<FileDescriptor> getRootFile();

	@NonNull
	OperationResult<InputStream> openFileForRead(FileDescriptor file,
												 OnConflictStrategy onConflictStrategy,
												 boolean cacheOperationsEnabled);

	@NonNull
	OperationResult<OutputStream> openFileForWrite(FileDescriptor file,
												   OnConflictStrategy onConflictStrategy,
												   boolean cacheOperationsEnabled);

	@NonNull
	OperationResult<Boolean> exists(FileDescriptor file);

	@NonNull
	OperationResult<FileDescriptor> getFile(String path, boolean cacheOperationsEnabled);
}
