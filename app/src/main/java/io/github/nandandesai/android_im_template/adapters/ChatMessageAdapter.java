package io.github.nandandesai.android_im_template.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.github.nandandesai.android_im_template.R;
import io.github.nandandesai.android_im_template.models.ChatMessage;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int OUTGOING_VIEWTYPE=0;
    private static final int INCOMING_VIEWTYPE=1;
    private static final String TAG = "ChatMessageAdapter";

    private Context context;
    private List<ChatMessage> chatMessages=new ArrayList<>();

    public ChatMessageAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).getMessageFrom().equalsIgnoreCase("me")){
            return OUTGOING_VIEWTYPE;
        }else{
            return INCOMING_VIEWTYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case OUTGOING_VIEWTYPE: {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.outgoing_chat_msg_item,viewGroup,false);
                return new OutgoingMessageViewHolder(view);
            }
            case INCOMING_VIEWTYPE: {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.incoming_chat_msg_item,viewGroup,false);
                return new IncomingMessageViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ChatMessage chatMessage=chatMessages.get(i);
        switch (viewHolder.getItemViewType()){
            case OUTGOING_VIEWTYPE:{
                OutgoingMessageViewHolder outgoingMessageViewHolder=(OutgoingMessageViewHolder) viewHolder;
                outgoingMessageViewHolder.messageTextView.setText(chatMessage.getMessageContent());
                outgoingMessageViewHolder.messageTimeView.setText(getFormattedMessageTime(chatMessage.getMessageTime()));
                break;
            }
            case INCOMING_VIEWTYPE:{
                IncomingMessageViewHolder incomingMessageViewHolder=(IncomingMessageViewHolder) viewHolder;
                incomingMessageViewHolder.messageTextView.setText(chatMessage.getMessageContent());
                //incomingMessageViewHolder.messageAuthorView.setText(chatMessage.getMessageFrom());
                incomingMessageViewHolder.messageAuthorView.setText("");/////////////////CHANGE THIS MAYBE?
                incomingMessageViewHolder.messageTimeView.setText(getFormattedMessageTime(chatMessage.getMessageTime()));
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void setChatMessages(List<ChatMessage> chatMessages){
        this.chatMessages=chatMessages;
        notifyItemInserted(chatMessages.size());
    }

    public void addChatMessage(ChatMessage chatMessage){
        this.chatMessages.add(chatMessage);
        notifyItemInserted(chatMessages.size());
    }

    private class IncomingMessageViewHolder extends RecyclerView.ViewHolder{
        TextView messageAuthorView;
        TextView messageTimeView;
        EmojiTextView messageTextView;

        public IncomingMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.messageAuthorView=itemView.findViewById(R.id.incomingMsgAuthor);
            this.messageTimeView=itemView.findViewById(R.id.incomingMsgTime);
            this.messageTextView=itemView.findViewById(R.id.incomingMsgText);
        }
    }

    private class OutgoingMessageViewHolder extends RecyclerView.ViewHolder{
        ImageView messageStatusView;
        TextView messageTimeView;
        EmojiTextView messageTextView;

        public OutgoingMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.messageStatusView=itemView.findViewById(R.id.outgoingMsgStatus);
            this.messageTimeView=itemView.findViewById(R.id.outgoingMsgTime);
            this.messageTextView=itemView.findViewById(R.id.outgoingMsgText);
        }
    }

    private String getFormattedMessageTime(long unixTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime);
        Date date = new Date(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(calendar.getTime());
    }

}
