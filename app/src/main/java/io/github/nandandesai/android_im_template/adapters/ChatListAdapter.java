package io.github.nandandesai.android_im_template.adapters;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.nandandesai.android_im_template.ChatActivity;
import io.github.nandandesai.android_im_template.R;
import io.github.nandandesai.android_im_template.models.ChatMessage;
import io.github.nandandesai.android_im_template.models.ChatSession;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>{

    private static final String TAG = "ChatListAdapter";

    private Context context;
    private List<DataHolder> dataHolders=new ArrayList<>();
    private LifecycleOwner lifecycleOwner;
    private ActionMode actionMode;
    private AppCompatActivity activity;

    //testing this variable for mulitple select and delete in ActionMode contextual menu. It's not completely implemented yet.
    private HashMap<String, RelativeLayout> chatIdsSelectedInActionMode=new HashMap<>();

    public ChatListAdapter(Context context, LifecycleOwner lifecycleOwner, List<DataHolder> dataHolders, AppCompatActivity activity) {
        this.context = context;
        this.lifecycleOwner=lifecycleOwner;
        this.dataHolders=dataHolders;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_list_item,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");

        dataHolders.get(i).chatSession.observe(lifecycleOwner, new Observer<ChatSession>() {
            @Override
            public void onChanged(@Nullable final ChatSession chatSession) {
                if(chatSession!=null){

                    //set profile pic
                    Glide.with(context)
                            .asBitmap()
                            .load(chatSession.getIcon())
                            .into(viewHolder.profilePicImageView);

                    //set chat title
                    viewHolder.chatTitleView.setText(chatSession.getName());

                    //configure onClickListener to go to next activity
                    viewHolder.chatListItemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, ChatActivity.class);
                            intent.putExtra("chatId", chatSession.getChatId());
                            context.startActivity(intent);
                        }
                    });

                    viewHolder.chatListItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if(actionMode!=null){
                                return false;
                            }
                            chatIdsSelectedInActionMode.put(chatSession.getChatId(), viewHolder.chatListItemLayout);
                            actionMode=activity.startSupportActionMode(actionModeCallback);
                            viewHolder.chatListItemLayout.setBackgroundColor(context.getResources().getColor(R.color.lightBlue));
                            return true;
                        }
                    });

                }
            }
        });

        //set the recent chat message
        dataHolders.get(i).recentMsg.observe(lifecycleOwner, new Observer<ChatMessage>() {
            @Override
            public void onChanged(@Nullable ChatMessage chatMessage) {
                viewHolder.recentChatMsgView.setText(chatMessage.getMessageContent());
            }
        });

        //set the unread messages count
        dataHolders.get(i).noOfUnreadMsgs.observe(lifecycleOwner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer unreadMsgs) {
                if(unreadMsgs!=null && unreadMsgs>0){
                    viewHolder.unreadMsgCountView.setVisibility(View.VISIBLE);
                    viewHolder.unreadMsgCountView.setText(unreadMsgs+"");
                }else{
                    viewHolder.unreadMsgCountView.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    public void setDataHolders(List<DataHolder> dataHolders) {
        this.dataHolders = dataHolders;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataHolders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout chatListItemLayout;
        CircleImageView profilePicImageView;
        TextView chatTitleView;
        EmojiTextView recentChatMsgView;
        TextView unreadMsgCountView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatListItemLayout=itemView.findViewById(R.id.chatListItemLayout);
            profilePicImageView=itemView.findViewById(R.id.profileImage);
            chatTitleView=itemView.findViewById(R.id.chatTitle);
            recentChatMsgView=itemView.findViewById(R.id.recentChatMsg);
            unreadMsgCountView=itemView.findViewById(R.id.noOfUnreadMsgs);
        }
    }

    public static class DataHolder{
        LiveData<ChatSession> chatSession;
        LiveData<ChatMessage> recentMsg;
        LiveData<Integer> noOfUnreadMsgs;

        public DataHolder(LiveData<ChatSession> chatSession, LiveData<ChatMessage> recentMsg, LiveData<Integer> noOfUnreadMsgs) {
            this.chatSession = chatSession;
            this.recentMsg = recentMsg;
            this.noOfUnreadMsgs = noOfUnreadMsgs;
        }

    }

    private ActionMode.Callback actionModeCallback=new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.main_activity_contextual_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.main_contextual_delete_menu:{
                    //get a set of selected keys.
                    Iterator iterator=chatIdsSelectedInActionMode.keySet().iterator();
                    while(iterator.hasNext()){
                        //iterate through each of the keys and do the job that is intended.
                        //for testing, I'm just displaying a toast
                        Toast.makeText(context, "Delete clicked on: "+iterator.next(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onActionItemClicked: Size of chatIdsSelectedInActionMode array:"+chatIdsSelectedInActionMode.size());
                    mode.finish();
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode=null;
            cleanUpSelectedItems();
        }
    };

    private void cleanUpSelectedItems(){
        //the below code is to clear off the color set on selected layout items.
        Iterator iterator=chatIdsSelectedInActionMode.keySet().iterator();
        while(iterator.hasNext()){
            RelativeLayout selectedLayout = (RelativeLayout)chatIdsSelectedInActionMode.get(iterator.next());
            selectedLayout.setBackgroundColor(0);
        }

        //finally, clear off the HashMap.
        chatIdsSelectedInActionMode.clear();
    }
}
