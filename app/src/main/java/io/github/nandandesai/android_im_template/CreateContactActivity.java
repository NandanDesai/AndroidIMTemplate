package io.github.nandandesai.android_im_template;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.github.nandandesai.android_im_template.models.Contact;
import io.github.nandandesai.android_im_template.repositories.ContactRepository;

public class CreateContactActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText idEditText;
    private Button saveContactButton;

    private ContactRepository contactRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        nameEditText=findViewById(R.id.contact_input_name);
        idEditText =findViewById(R.id.contact_input_id);
        saveContactButton=findViewById(R.id.contact_save);

        contactRepository=new ContactRepository(getApplication());

        saveContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameEditText.getText().toString();
                String id= idEditText.getText().toString();
                if(name.trim().isEmpty() || id.trim().isEmpty()){
                    return ;
                }
                Contact contact=new Contact(id, "", name, "");
                contactRepository.insert(contact);
                Toast.makeText(CreateContactActivity.this, "Contact saved.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
