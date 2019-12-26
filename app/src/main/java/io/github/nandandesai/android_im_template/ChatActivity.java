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
                    //sending the message to myself for testing
                    //String messageTo=preferences.getMyOnionAddress();
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


        /*
        * TODO: When I save the contact from CreateContactActivity, then in ContactRepository, even the chatSession table will be updated.
                So, I should just use whatever name is present in ChatSession table right? Whats the use of the below getContact observer code?
                I can simplify this.
        */
        /*
        chatActivityViewModel.getContact(chatId).observe(this, new Observer<Contact>() {
            @Override
            public void onChanged(@Nullable Contact contact) {
                if (contact == null) {
                    Log.d(TAG, "onChanged: contact doesn't exists. So, trying to get info from ChatSession table");
                    contactExists = false;
                    return;
                }
                contactExists = true;
                Log.d(TAG, "onChanged: contact exists! Setting info on toolbar from Contact table");
                chatNameView.setText(contact.getName());
                Glide.with(ChatActivity.this)
                        .asBitmap()
                        .load(contact.getProfilePic())
                        .into(toolbarProfileImageView);
            }
        });
        */

        //the below code is temporary to some extent.
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
                    //dynamically update

                }

        });



        chatActivityViewModel.getChatMessagesLiveData(chatId).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(@Nullable List<ChatMessage> chatMessages) {

                ///////the below code is temporary.
                /////////////////////////////////
                ///////////////////////////////////
                if (chatMessages != null) {
                    //here, I'm updating all the not_read messages to read. But, in actual case
                    //I need to send a response to the sender that I have read the message.
                    //figure out what you need to do in such case later.
                    chatActivityViewModel.updateMessageStatusWithChatId(chatId, ChatMessage.STATUS.USER_NOT_READ, ChatMessage.STATUS.USER_READ);
                    chatMessageAdapter.setChatMessages(chatMessages);
                    chatMessagesRecyclerView.scrollToPosition(chatMessagesRecyclerView.getAdapter().getItemCount()-1);
                }
            }
        });


        /*
        * The below code adds the newly entered chatMessage to the list of chatmessages in adapter on by one as opposed
        * to replacing the entire list of messages as done in the above commented code.
        * */

        /*here, I was facing a problem of where, when I open this activity, the last element of the ChatMessages array was getting
        * added twice. That was because, after I first set the ChatMessage array in the Adapter, the below code would also trigger
        * which then would add the last element separately again. Hence, the last element was getting repeated when I first open the Activity.
        * To avoid that, here I added firstTime variable to check if the activity is being opened for the first time or is it already open and running.
        * If it's the firstTime, then don't add the last ("recent") ChatMessage in the Adapter. Otherwise, add it. That's the logic behind this.
        * It's not too good of an approach but it works.*/

        /*
        //UPDATE: Duplicate entries are being added to Recycler view because of the below code.

        final boolean[] firstTime = {true};
        chatActivityViewModel.getRecentChatMessage(chatId).observe(this, new Observer<ChatMessage>() {
            @Override
            public void onChanged(@Nullable ChatMessage chatMessage) {

                ///////the below code is temporary.
                /////////////////////////////////
                ///////////////////////////////////
                if (chatMessage != null && !firstTime[0]) {
                    //here, I'm updating all the not_read messages to read. But, in actual case
                    //I need to send a response to the sender that I have read the message.
                    //figure out what you need to do in such case later.
                    chatActivityViewModel.updateMessageStatusWithChatId(chatId, ChatMessage.STATUS.USER_NOT_READ, ChatMessage.STATUS.USER_READ);
                    Log.d(TAG, "onChanged: adding the message \""+chatMessage.getMessageContent()+"\" to the Recycler view");
                    chatMessageAdapter.addChatMessage(chatMessage);
                    chatMessagesRecyclerView.scrollToPosition(chatMessagesRecyclerView.getAdapter().getItemCount()-1);
                }
                firstTime[0] =false;
            }
        });
        */

    }

    private static class SetupChatListViewTask extends AsyncTask<Void, Void, Void>{

        private RecyclerView chatMessagesRecyclerView;
        private ChatActivityViewModel chatActivityViewModel;
        private ChatMessageAdapter chatMessageAdapter;
        private List<ChatMessage> chatMessages;
        private String chatId;

        public SetupChatListViewTask(RecyclerView chatMessagesRecyclerView, ChatActivityViewModel chatActivityViewModel, ChatMessageAdapter chatMessageAdapter, String chatId) {
            this.chatMessagesRecyclerView = chatMessagesRecyclerView;
            this.chatActivityViewModel = chatActivityViewModel;
            this.chatMessageAdapter = chatMessageAdapter;
            this.chatId = chatId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: fetching the list of chatMessages.");
            chatMessages=chatActivityViewModel.getChatMessages(chatId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "onPostExecute: setting the chatMessagesAdapter with the fetched list of chatMessages");
            //chatMessages.remove(chatMessages.size()-1); //remove the last element because that will be added separately later using LiveData observer.
            chatMessageAdapter.setChatMessages(chatMessages);
            chatMessagesRecyclerView.scrollToPosition(chatMessagesRecyclerView.getAdapter().getItemCount()-1);
        }
    }

}
