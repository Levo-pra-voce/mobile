package com.levopravoce.mobile.database.converter

import androidx.room.TypeConverter
import com.levopravoce.mobile.features.app.data.dto.MessageType
import java.sql.Date

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromMessageType(value: String?): MessageType? {
        return value?.let { MessageType.valueOf(it) }
    }

    @TypeConverter
    fun messageTypeToString(type: MessageType?): String? {
        return type?.name
    }
}