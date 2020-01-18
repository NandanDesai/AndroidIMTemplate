package io.github.nandandesai.android_im_template.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.util.Log;

import net.sqlcipher.database.SQLiteConstraintException;

import java.util.List;

import io.github.nandandesai.android_im_template.database.AndroidIMTemplateDatabase;
import io.github.nandandesai.android_im_template.database.ChatMessageDao;
import io.github.nandandesai.android_im_template.models.ChatMessage;
import io.github.nandandesai.android_im_template.models.ChatSession;

public class ChatMessageRepository {
    private static final String TAG = "ChatMessageRepository";
    private ChatMessageDao chatMessageDao;
    private AndroidIMTemplateDatabase androidIMTemplateDatabase;
    private Application application;

    public ChatMessageRepository(Application application) {
        this.application = application;
        androidIMTemplateDatabase = AndroidIMTemplateDatabase.getInstance(application);
        chatMessageDao = androidIMTemplateDatabase.chatMessageDao();
    }

    public void insert(final ChatMessage chatMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String contactName = androidIMTemplateDatabase.contactDao().getContactName(chatMessage.getChatId());
                String chatName;
                if (contactName != null) {
                    chatName = contactName;
                } else {
                    chatName = chatMessage.getChatId();
                }

                if (androidIMTemplateDatabase.chatSessionDao().chatSessionExists(chatMessage.getChatId()) != 1) {

                    //THIS IS TEMPORARY
                    //if chat session doesn't exists, create a new one.
                    //in actual case, this would involve key exchanges and stuff for Signal protocol.
                    //for time being, I'm just going to create some temporary rows for it.
                    Log.d(TAG, "run: Chat session didn't exist previously. So, creating a new one before inserting the message.");
                    androidIMTemplateDatabase.chatSessionDao().insert(new ChatSession(chatMessage.getChatId(), chatName, ChatSession.TYPE.DIRECT, "https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80"));

                }
                try {
                    chatMessageDao.insert(chatMessage);
                } catch (SQLiteConstraintException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        }).start();
    }

    public List<ChatMessage> getChatMessages(String chatId) {
        return chatMessageDao.getAllChatMessages(chatId);
    }

    public LiveData<List<ChatMessage>> getChatMessagesLiveData(String chatId){
        return chatMessageDao.getAllChatMessagesLiveData(chatId);
    }

    public void updateMessageStatusWithChatId(final String chatId, final String fromStatus, final String toStatus) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatMessageDao.updateMessageStatusWithChatId(chatId, fromStatus, toStatus);
            }
        }).start();
    }
}
