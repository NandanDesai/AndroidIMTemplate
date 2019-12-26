package io.github.nandandesai.android_im_template.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.github.nandandesai.android_im_template.database.ContactDao;
import io.github.nandandesai.android_im_template.database.AndroidIMTemplateDatabase;
import io.github.nandandesai.android_im_template.models.Contact;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact>> contacts;
    private AndroidIMTemplateDatabase androidIMTemplateDatabase;
    public ContactRepository(Application application){
        androidIMTemplateDatabase = AndroidIMTemplateDatabase.getInstance(application);
        contactDao= androidIMTemplateDatabase.contactDao();
        contacts=contactDao.getAllContacts();
    }

    public void insert(final Contact contact){
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactDao.insert(contact);
                if(androidIMTemplateDatabase.chatSessionDao().chatSessionExists(contact.getId())==1){
                    androidIMTemplateDatabase.chatSessionDao().updateChatName(contact.getId(), contact.getName());
                }
            }
        }).start();
    }

    public void update(final Contact contact){
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactDao.update(contact);
            }
        }).start();
    }

    public LiveData<Contact> getContact(String id){
       return contactDao.getContact(id);
    }

    public void delete(final String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactDao.delete(id);
            }
        }).start();
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }
}
