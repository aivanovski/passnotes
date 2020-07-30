package com.ivanovsky.passnotes.data.repository.db.converters

import androidx.room.TypeConverter
import com.ivanovsky.passnotes.data.repository.file.FSType

class FSTypeConverter {

    @TypeConverter
    fun fromString(value: String): FSType {
        return FSType.fromTextName(value)
    }

    @TypeConverter
    fun fsTypeToString(type: FSType): String {
        return type.textName
    }
}