package io.github.nandandesai.android_im_template.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import io.github.nandandesai.android_im_template.models.ChatSession;

@Dao
public interface ChatSessionDao {
    @Insert
    void insert(ChatSession chatSession);

    @Query("SELECT * FROM ChatSession")
    LiveData<List<ChatSession>> getAllChats();

    @Query("SELECT * FROM ChatSession WHERE chatId=:chatId")
    LiveData<ChatSession> getChatSession(String chatId);

    @Query("SELECT COUNT(*) FROM ChatSession WHERE chatId=:chatId")
    int chatSessionExists(String chatId); //1 for true, 0 for false

    @Query("DELETE FROM ChatSession WHERE chatId=:chatId")
    void delete(String chatId);

    @Query("UPDATE ChatSession SET name=:chatName WHERE chatId=:chatId")
    void updateChatName(String chatId, String chatName);
}
