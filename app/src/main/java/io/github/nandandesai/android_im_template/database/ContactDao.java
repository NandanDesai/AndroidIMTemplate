package io.github.nandandesai.android_im_template.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.github.nandandesai.android_im_template.models.Contact;

@Dao
public interface ContactDao {
    @Insert
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Query("DELETE FROM Contact WHERE id=:id")
    void delete(String id);

    @Query("SELECT * FROM Contact")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * FROM Contact WHERE id=:id")
    LiveData<Contact> getContact(String id);

    @Query("SELECT name FROM Contact WHERE id=:id")
    String getContactName(String id);
}
