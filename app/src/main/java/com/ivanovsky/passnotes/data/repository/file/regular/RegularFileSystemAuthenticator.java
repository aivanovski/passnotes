package com.ivanovsky.passnotes.data.repository.file.regular;

import android.content.Context;

import com.ivanovsky.passnotes.data.repository.file.FileSystemAuthenticator;

class RegularFileSystemAuthenticator implements FileSystemAuthenticator {

    @Override
    public boolean isAuthenticationRequired() {
        return false;
    }

    @Override
    public void startAuthActivity(Context context) {
    }
}
