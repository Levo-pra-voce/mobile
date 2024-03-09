package com.levopravoce.mobile.features.chat.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.levopravoce.mobile.features.chat.data.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("""
        SELECT * FROM messages
            WHERE channelId = :channelId
        ORDER BY date
    """)
    fun getMessagesByChannel(channelId: Long): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE channelId = :channelId AND date >= :date")
    fun getMessagesByChannelAndRange(channelId: Long, date: Long): Flow<List<Message>>

    @Query("""
        SELECT MAX(date) from messages
            WHERE channelId = :channelId
    """)
    fun getMaxDateByChannel(channelId: Long): Long?

    @Query("""
        SELECT MAX(m.date)
            FROM messages m
                where m.channelId = :channelId
        group by m.channelId
    """)
    fun getLastMessageDate(channelId: Long): Long?;

    @Insert
    suspend fun save(message: Message)

    @Insert
    suspend fun saveAll(messages: List<Message>)
}