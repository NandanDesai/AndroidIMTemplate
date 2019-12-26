package io.github.nandandesai.android_im_template.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

import io.github.nandandesai.android_im_template.models.Contact;
import io.github.nandandesai.android_im_template.repositories.ContactRepository;

public class ContactListViewModel  extends AndroidViewModel {
    private ContactRepository contactRepository;
    private LiveData<List<Contact>> contacts;

    public ContactListViewModel(@NonNull Application application) {
        super(application);
        contactRepository=new ContactRepository(application);
        contacts=contactRepository.getContacts();
    }

    public ContactRepository getContactRepository() {
        return contactRepository;
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }
}
