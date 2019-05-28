package io.github.nandandesai.android_im_template.models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/*
This is used to save contacts of other users

*/

@Entity
public class Contact {
    @PrimaryKey @NonNull
    private String id;
    private String profilePic;
    private String name;
    private String statusMessage;

    public Contact(@NonNull String id, String profilePic, String name, String statusMessage) {
        this.id = id;
        this.profilePic = profilePic;
        this.name = name;
        this.statusMessage = statusMessage;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
