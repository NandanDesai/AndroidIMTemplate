package io.github.nandandesai.android_im_template.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.github.nandandesai.android_im_template.models.ChatMessage;

@Dao
public interface ChatMessageDao {

    @Insert
    void insert(ChatMessage chatMessage);

    @Query("SELECT * FROM ChatMessage WHERE chatId=:chatId ORDER BY messageTime ASC")
    List<ChatMessage> getAllChatMessages(String chatId);

    @Query("SELECT * FROM ChatMessage WHERE chatId=:chatId ORDER BY messageTime ASC")
    LiveData<List<ChatMessage>> getAllChatMessagesLiveData(String chatId);

    @Query("SELECT * FROM ChatMessage WHERE messageStatus='"+ChatMessage.STATUS.USER_NOT_READ+"' AND chatId=:chatId")
    LiveData<List<ChatMessage>> getAllUnreadMsgs(String chatId);

    @Query("SELECT * FROM ChatMessage WHERE messageStatus='"+ChatMessage.STATUS.WAITING_TO_SEND+"'")
    LiveData<List<ChatMessage>> getAllUnsentMsgs();

    @Query("UPDATE ChatMessage SET messageStatus=:status WHERE messageId=:messageId")
    void updateMessageStatusWithMessageId(String messageId, String status);

    //fromStatus and toStatus means you can change the status in the database from a particular value to another value
    //like updating from a USER_NOT_READ to USER_READ
    //If there are multiple unread messages for a particular chatID, then this method can be called for changing
    //unread to read.
    @Query("UPDATE ChatMessage SET messageStatus=:toStatus WHERE messageStatus=:fromStatus AND chatId=:chatId")
    void updateMessageStatusWithChatId(String chatId, String fromStatus, String toStatus);

    @Query("SELECT Count(*) FROM ChatMessage WHERE messageStatus='"+ChatMessage.STATUS.USER_NOT_READ+"' AND chatId=:chatId")
    LiveData<Integer> getNumberOfUnreadMsgs(String chatId);

    @Query("SELECT * FROM ChatMessage WHERE chatId=:chatId AND messageTime=(SELECT Max(messageTime) FROM ChatMessage WHERE chatId=:chatId)")
    LiveData<ChatMessage> getRecentMsg(String chatId);

    @Query("SELECT chatId FROM (SELECT chatId, MAX(messageTime) AS lastUpdated FROM ChatMessage GROUP BY chatId)  ORDER BY lastUpdated DESC")
    LiveData<List<String>> getAllChatIds();
}
