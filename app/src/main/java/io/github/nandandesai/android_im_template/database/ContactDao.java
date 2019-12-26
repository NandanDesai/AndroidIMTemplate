package io.github.nandandesai.android_im_template.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
