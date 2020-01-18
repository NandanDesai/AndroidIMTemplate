package io.github.nandandesai.android_im_template;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.nandandesai.android_im_template.adapters.ChatMessageAdapter;
import io.github.nandandesai.android_im_template.models.ChatMessage;
import io.github.nandandesai.android_im_template.models.ChatSession;
import io.github.nandandesai.android_im_template.viewmodels.ChatActivityViewModel;


public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private ChatActivityViewModel chatActivityViewModel;
    private ChatMessageAdapter chatMessageAdapter;
    private String chatId;
    private List<ChatMessage> chatMessages=new ArrayList<>();

    private ImageView emojiButton;
    private EmojiEditText messageInput;
    private ImageButton sendButton;
    private RecyclerView chatMessagesRecyclerView;
    private EmojiPopup emojiPopup;

    private boolean contactExists = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        emojiButton = findViewById(R.id.emojiButton);
        messageInput = findViewById(R.id.msgInput);
        sendButton = findViewById(R.id.send);
        chatMessagesRecyclerView = findViewById(R.id.chatMessageList);


        chatId = getIntent().getStringExtra("chatId");
        Log.d(TAG, "onCreate: Opened ChatActivity with chatId: " + chatId);

        chatActivityViewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);

        //setup Toolbar info
        setupToolbar();

        chatMessageAdapter=new ChatMessageAdapter(this,  chatMessages);
        chatMessagesRecyclerView.setAdapter(chatMessageAdapter);
        chatMessagesRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatMessagesRecyclerView.setLayoutManager(linearLayoutManager);




        //new SetupChatListViewTask(chatMessagesRecyclerView, chatActivityViewModel, chatMessageAdapter, chatId).execute();


        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiPopup.toggle();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messageContent = messageInput.getText().toString();
                messageContent = messageContent.trim();
                if (!messageContent.equals("")) {
                    Log.d(TAG, "onClick: " + messageInput.getText());
                    String messageFrom = "me";
                    String messageTo = chatId;
                    String messageStatus = ChatMessage.STATUS.WAITING_TO_SEND;
                    long messageTime = System.currentTimeMillis();
                    String messageType = ChatMessage.TYPE.TEXT;
                    String id = chatId;
                    String messageId = messageFrom + messageTime;
                    ChatMessage chatMessage = new ChatMessage(messageId, messageContent, messageFrom, messageTo, messageStatus, messageTime, messageType, id);
                    chatActivityViewModel.insert(chatMessage);
                    messageInput.getText().clear();

                }
            }
        });

        messageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojiPopup.isShowing()) {
                    emojiPopup.dismiss();
                }
            }
        });


        setUpEmojiPopup();
    }

    @Override
    protected void onStop() {
        if (emojiPopup != null) {
            emojiPopup.dismiss();
        }
        super.onStop();
    }

    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.chatLayout)).build(messageInput);
    }

    private void setupToolbar(){
        //setup toolbar info
        Toolbar toolbar = (Toolbar) findViewById(R.id.chatActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView chatNameView = toolbar.findViewById(R.id.chatToolbarTitle);
        CircleImageView toolbarProfileImageView = findViewById(R.id.chatToolbarIcon);

        setupLiveDataObservers(chatNameView, toolbarProfileImageView);
    }

    private void setupLiveDataObservers(final TextView chatNameView, final CircleImageView toolbarProfileImageView) {

        //if the contact doesn't exists, then it is probably a group chat or it is a chat with an unsaved contact.
        //so we'll try to check if the contact exists or not. If it doesn't then we'll get the data from ChatSession
        //and then use it to set the Toolbar info.
        chatActivityViewModel.getChatSession(chatId).observe(this, new Observer<ChatSession>() {
            @Override
            public void onChanged(@Nullable ChatSession chatSession) {

                    if (chatSession == null) {
                        //if contact doesn't exists and chatSession is null
                        //I think this case won't happen.
                        //maybe in the future when all the Signal stuff is done, it will happen. But not now.
                        Log.d(TAG, "onChanged: ChatSession doesn't exists on chatId: " + chatId);
                        chatNameView.setText(chatId);
                        return;
                    }
                    Log.d(TAG, "onChanged: ChatSession info exists. I'm using that to set the toolbar info");
                    chatNameView.setText(chatSession.getName());
                    Glide.with(ChatActivity.this)
                            .asBitmap()
                            .load(chatSession.getIcon())
                            .into(toolbarProfileImageView);
                    //You should also add an "Online" status to the ChatSession and use it here. Using LiveData here is useful to
                    //dynamically update "Online" status

                }

        });



        chatActivityViewModel.getChatMessagesLiveData(chatId).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(@Nullable List<ChatMessage> chatMessages) {
                if (chatMessages != null) {
                    //here, I'm updating all the not_read messages to read. But, in actual case
                    //I need to send a response to the sender that I have read the message and then update
                    //it in my local database.
                    chatActivityViewModel.updateMessageStatusWithChatId(chatId, ChatMessage.STATUS.USER_NOT_READ, ChatMessage.STATUS.USER_READ);
                    chatMessageAdapter.setChatMessages(chatMessages);
                    chatMessagesRecyclerView.scrollToPosition(chatMessagesRecyclerView.getAdapter().getItemCount()-1);
                }
            }
        });

    }

}
