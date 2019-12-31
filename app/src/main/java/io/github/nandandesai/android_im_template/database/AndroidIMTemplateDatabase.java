package io.github.nandandesai.android_im_template.database;

import android.annotation.SuppressLint;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.util.Log;

import com.commonsware.cwac.saferoom.SafeHelperFactory;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.github.nandandesai.android_im_template.models.ChatSession;
import io.github.nandandesai.android_im_template.models.ChatMessage;
import io.github.nandandesai.android_im_template.models.Contact;

@Database(entities = {ChatMessage.class, ChatSession.class, Contact.class},version = 1)
public abstract class AndroidIMTemplateDatabase extends RoomDatabase {
    private static final String TAG = "AndroidIMTemplateDatabase";
    private static AndroidIMTemplateDatabase instance;
    private static final String DB_NAME="androidimtemplate.db";
    public abstract ChatMessageDao chatMessageDao();
    public abstract ChatSessionDao chatSessionDao();
    public abstract ContactDao contactDao();

    public static synchronized AndroidIMTemplateDatabase getInstance(Context context){

        if(instance==null) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

                SecureRandom secureRandom = new SecureRandom();
                int keyBitSize = 256;
                keyGenerator.init(keyBitSize, secureRandom);

                SecretKey secretKey = keyGenerator.generateKey();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }


            String passPhrase="templateSecret";

            SafeHelperFactory factory = new SafeHelperFactory(passPhrase.toCharArray());
            instance=Room.databaseBuilder(context, AndroidIMTemplateDatabase.class, DB_NAME)
                    .openHelperFactory(factory)
                    //.allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        //Log.d(TAG,"Is database encrypted? : "+ SQLCipherUtils.getDatabaseState(context, DB_NAME));
        return instance;
    }

    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            new InitializeDb(instance).execute();
        }
    };


    //this class will initialize database with some sample rows
    private static class InitializeDb extends AsyncTask<Void, Void, Void>{
        private ContactDao contactDao;
        private ChatSessionDao chatSessionDao;
        public InitializeDb(AndroidIMTemplateDatabase database) {
            this.contactDao = database.contactDao();
            this.chatSessionDao=database.chatSessionDao();
        }

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... voids) {

            Contact contact = new Contact("me", "https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80", "Me","Hey there! I'm using AndroidIMTemplate.");
            contactDao.insert(contact);

            Log.d(TAG, "doInBackground: Sample contact inserted which represents the owner of the app as 'Me'");


            ChatSession chatSession=new ChatSession("abcd123", "Sample", ChatSession.TYPE.DIRECT, "https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
            chatSessionDao.insert(chatSession);

            Log.d(TAG, "doInBackground: Sample ChatSession inserted");

            return null;
        }
    }

}
