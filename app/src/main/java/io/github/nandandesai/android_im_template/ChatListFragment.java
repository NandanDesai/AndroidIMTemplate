package io.github.nandandesai.android_im_template;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.nandandesai.android_im_template.adapters.ChatListAdapter;
import io.github.nandandesai.android_im_template.models.ChatMessage;
import io.github.nandandesai.android_im_template.models.ChatSession;
import io.github.nandandesai.android_im_template.viewmodels.ChatListViewModel;


public class ChatListFragment extends Fragment {

    private static final String TAG = "ChatListFragment";
    private ChatListAdapter chatListAdapter;
    private RecyclerView recyclerView;
    private List<ChatListAdapter.DataHolder> chatDataHolders=new ArrayList<>();

    private ChatListViewModel chatListViewModel;

    public ChatListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_list, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //do something here
            }
        });

        Log.d(TAG, "onCreateView: running...");

        initRecyclerView(view);

        chatListViewModel=ViewModelProviders.of(this).get(ChatListViewModel.class);
        chatListViewModel.getAllChatIds().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> chatIds) {

                Log.d(TAG, "onChanged: CHAT IDS are: "+chatIds.toString());

                try {
                    LiveData<ChatSession> chatSession;
                    LiveData<ChatMessage> recentMsg;
                    LiveData<Integer> noOfUnreadMsgs;
                    if (chatIds != null) {
                        chatDataHolders.clear();
                        for (String chatId : chatIds) {
                            Log.d(TAG, "onChanged: for loop chatId: "+chatId);
                            chatSession=chatListViewModel.getChatSession(chatId);
                            recentMsg=chatListViewModel.getRecentMsg(chatId);
                            noOfUnreadMsgs=chatListViewModel.getNumberOfUnreadMsgs(chatId);
                            ChatListAdapter.DataHolder dataHolder = new ChatListAdapter.DataHolder(chatSession, recentMsg, noOfUnreadMsgs);
                            chatDataHolders.add(dataHolder);
                        }
                    }
                    chatListAdapter.setDataHolders(chatDataHolders);
                }catch (NullPointerException npe){
                    Log.d(TAG, "onChanged: Null Pointer: "+npe.getMessage());
                }
            }
        });



        return view;
    }

/*
    private void addNewChatItem(){
        chatTitles.add(0,"Alice");
        profilePics.add(0,"https://www.trickscity.com/wp-content/uploads/2018/02/cute-girl-profile-pics.jpg");
        recentChatMsgs.add(0,"Nandan Desai: Hi");
        noOfUnreadMsgs.add(0,1);

        chatListAdapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }
*/
    private void initRecyclerView(View view){
        Context context=getContext();
        recyclerView=view.findViewById(R.id.chatList);
        chatListAdapter=new ChatListAdapter(context,this, chatDataHolders, (AppCompatActivity)getActivity());
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        recyclerView.addItemDecoration(divider);
    }


}
