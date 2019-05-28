package io.github.nandandesai.android_im_template.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.nandandesai.android_im_template.ChatActivity;
import io.github.nandandesai.android_im_template.R;
import io.github.nandandesai.android_im_template.models.Contact;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{
    private Context context;

    private List<Contact> contactList=new ArrayList<>();

    public ContactListAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contactList=contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_list_item,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Glide.with(context)
                .asBitmap()
                .load(contactList.get(i).getProfilePic())
                .into(viewHolder.profilePicImageView);

        viewHolder.contactNameView.setText(contactList.get(i).getName());

        viewHolder.contactListItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("chatId", contactList.get(i).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void setContactList(List<Contact> contacts){
        contactList=contacts;

        //this could be changed to notifyItemInserted or something to get animation effects
        //watch video from Coding In Flow youtube channel
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout contactListItemLayout;
        CircleImageView profilePicImageView;
        TextView contactNameView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactListItemLayout=itemView.findViewById(R.id.contactListItemLayout);
            profilePicImageView=itemView.findViewById(R.id.contactProfileImage);
            contactNameView=itemView.findViewById(R.id.contactName);
        }
    }
}
