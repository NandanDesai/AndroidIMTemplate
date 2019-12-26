package io.github.nandandesai.android_im_template.services;

import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import io.github.nandandesai.android_im_template.models.ChatMessage;
import io.github.nandandesai.android_im_template.repositories.ChatMessageRepository;

public class PeerLinkMainService extends LifecycleService {
    private static final String TAG = "PeerLinkMainService";

    private ExecutorService executorService;
    private ChatMessageRepository chatMessageRepository;
    private Observer<List<ChatMessage>> chatMessagesObserver;
    public PeerLinkMainService() {}

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: service started");
        super.onStartCommand(intent, flags, startId);
        executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                //Temporarily disabling this for testing websockets
                /*
                try {
                    Log.d(TAG, "run: ABOUT TO START THE SERVER");
                    peerLinkReceiver = new PeerLinkReceiver(getApplication(),9000);
                }catch (IOException io){
                    io.printStackTrace();
                }
                */
            }
        });

        chatMessageRepository=new ChatMessageRepository(getApplication());
        chatMessagesObserver=new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(@Nullable List<ChatMessage> chatMessages) {
                if(chatMessages!=null){

                }
            }
        };

        chatMessageRepository.getAllUnsentMsgs().observe(this, chatMessagesObserver);





        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: service destroyed");
        Intent broadcastIntent = new Intent(this, PeerLinkRestartReceiver.class);
        sendBroadcast(broadcastIntent);


        Log.d(TAG, "onDestroy: SERVER STOPPED");
        executorService.shutdown();

    }


}
