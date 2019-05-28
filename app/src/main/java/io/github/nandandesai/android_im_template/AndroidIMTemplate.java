package io.github.nandandesai.android_im_template;

import android.app.Application;
import android.util.Log;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;


public class AndroidIMTemplate extends Application {
    private static final String TAG = "AndroidIMTemplate";

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: The first line of the app executed");
        super.onCreate();

        //initialize emoji keyboard thing...
        EmojiManager.install(new IosEmojiProvider());
    }




}
