package io.github.nandandesai.android_im_template.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PeerLinkRestartReceiver extends BroadcastReceiver {

    private static final String TAG = "PeerLinkRestartReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: service is being restarted from Broadcast Receiver");
        context.startService(new Intent(context, PeerLinkMainService.class));;
    }


}
