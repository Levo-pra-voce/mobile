package com.levopravoce.mobile.features.chat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.levopravoce.mobile.features.app.data.dto.MessageType
import java.sql.Date

@Entity(tableName = "messages",
    indices = [Index(value = ["channelId"])]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "text")
    var text: String?,
    @ColumnInfo(name = "imagePath")
    var imagePath: String?,
    @ColumnInfo(name = "type")
    var type: MessageType,
    @ColumnInfo(name = "channelId")
    var channelId: Long,
    @ColumnInfo(name = "date")
    var date: Date,
    @ColumnInfo(name = "sender")
    var sender: String
)