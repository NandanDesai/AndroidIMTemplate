package io.github.nandandesai.android_im_template.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

import io.github.nandandesai.android_im_template.database.AndroidIMTemplateDatabase;
import io.github.nandandesai.android_im_template.database.ChatMessageDao;
import io.github.nandandesai.android_im_template.database.ChatSessionDao;
import io.github.nandandesai.android_im_template.models.ChatMessage;
import io.github.nandandesai.android_im_template.models.ChatSession;


public class ChatListRepository {
    private static final String TAG = "ChatListRepository";
    private ChatSessionDao chatSessionDao;
    private LiveData<List<ChatSession>> chats;
    private ChatMessageDao chatMessageDao;

    public ChatListRepository(Application application){
        AndroidIMTemplateDatabase androidIMTemplateDatabase = AndroidIMTemplateDatabase.getInstance(application);
        chatSessionDao = androidIMTemplateDatabase.chatSessionDao();
        chatMessageDao= androidIMTemplateDatabase.chatMessageDao();
        chats= chatSessionDao.getAllChats();
    }

    public LiveData<List<ChatSession>> getChats() {
        return chats;
    }

    public LiveData<Integer> getNumberOfUnreadMsgs(String chatId){
        return chatMessageDao.getNumberOfUnreadMsgs(chatId);
    }

    public LiveData<ChatMessage> getRecentMsg(String chatId){
        return chatMessageDao.getRecentMsg(chatId);
    }

    public LiveData<List<String>> getAllChatIds(){
        return chatMessageDao.getAllChatIds();
    }

    public void insertChat(final ChatSession chatSession){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatSessionDao.insert(chatSession);
            }
        }).start();
    }

    public LiveData<ChatSession> getChatSession(String chatId){
        return chatSessionDao.getChatSession(chatId);
    }

    public void deleteChat(final String chatId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatSessionDao.delete(chatId);
            }
        }).start();
    }
}
