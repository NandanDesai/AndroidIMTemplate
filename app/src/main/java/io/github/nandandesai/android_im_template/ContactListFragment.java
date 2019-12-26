package io.github.nandandesai.android_im_template;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.nandandesai.android_im_template.adapters.ContactListAdapter;
import io.github.nandandesai.android_im_template.models.Contact;
import io.github.nandandesai.android_im_template.viewmodels.ContactListViewModel;


public class ContactListFragment extends Fragment {
    private List<Contact> contacts=new ArrayList<>();

    private RecyclerView recyclerView;

    private ContactListViewModel contactListViewModel;

    private ContactListAdapter contactListAdapter;

    public ContactListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contact_list, container, false);
        FloatingActionButton fab = view.findViewById(R.id.contact_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add new contact
                Intent intent=new Intent(getContext(), CreateContactActivity.class);
                startActivity(intent);
            }
        });
        initRecyclerView(view);

        contactListViewModel = ViewModelProviders.of(this).get(ContactListViewModel.class);
        contactListViewModel.getContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable List<Contact> contacts) {
                contactListAdapter.setContactList(contacts);
            }
        });

        return view;
    }

    private void initRecyclerView(View view){
        recyclerView=view.findViewById(R.id.contact_list);
        Context context=getContext();
        contactListAdapter=new ContactListAdapter(context, contacts);
        recyclerView.setAdapter(contactListAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        recyclerView.addItemDecoration(divider);
    }


}
