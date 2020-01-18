package io.github.nandandesai.android_im_template.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

import io.github.nandandesai.android_im_template.models.ChatMessage;
import io.github.nandandesai.android_im_template.models.ChatSession;
import io.github.nandandesai.android_im_template.repositories.ChatListRepository;

public class ChatListViewModel extends AndroidViewModel {

    private ChatListRepository chatListRepository;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        chatListRepository =new ChatListRepository(application);
    }

    public void insert(ChatSession chatSession){
        chatListRepository.insertChat(chatSession);
    }

    public LiveData<Integer> getNumberOfUnreadMsgs(String chatId){
        return chatListRepository.getNumberOfUnreadMsgs(chatId);
    }

    public LiveData<ChatMessage> getRecentMsg(String chatId){
        return chatListRepository.getRecentMsg(chatId);
    }

    public LiveData<ChatSession> getChatSession(String chatId){
        return chatListRepository.getChatSession(chatId);
    }

    public LiveData<List<String>> getAllChatIds(){
        return chatListRepository.getAllChatIds();
    }
}
