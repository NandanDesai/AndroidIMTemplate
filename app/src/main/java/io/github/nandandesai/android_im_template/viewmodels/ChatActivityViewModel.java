package io.github.nandandesai.android_im_template.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

import io.github.nandandesai.android_im_template.models.ChatMessage;
import io.github.nandandesai.android_im_template.models.ChatSession;
import io.github.nandandesai.android_im_template.repositories.ChatListRepository;
import io.github.nandandesai.android_im_template.repositories.ChatMessageRepository;

public class ChatActivityViewModel extends AndroidViewModel {

    private ChatMessageRepository chatMessageRepository;
    private ChatListRepository chatListRepository;

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);
        chatMessageRepository=new ChatMessageRepository(application);
        chatListRepository=new ChatListRepository(application);
    }

    public LiveData<ChatSession> getChatSession(String chatId){
        return chatListRepository.getChatSession(chatId);
    }

    public void insert(ChatMessage chatMessage){
        chatMessageRepository.insert(chatMessage);
    }

    public List<ChatMessage> getChatMessages(String chatId) {
        return chatMessageRepository.getChatMessages(chatId);
    }

    public LiveData<List<ChatMessage>> getChatMessagesLiveData(String chatId) {
        return chatMessageRepository.getChatMessagesLiveData(chatId);
    }

    public void updateMessageStatusWithChatId(String chatId, String fromStatus, String toStatus){
        chatMessageRepository.updateMessageStatusWithChatId(chatId, fromStatus, toStatus);
    }
}
