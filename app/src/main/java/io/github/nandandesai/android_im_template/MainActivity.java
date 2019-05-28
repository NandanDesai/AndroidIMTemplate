package io.github.nandandesai.android_im_template;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import io.github.nandandesai.android_im_template.adapters.TabLayoutPagerAdapter;
import io.github.nandandesai.android_im_template.services.PeerLinkMainService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Intent peerLinkServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayoutPagerAdapter tabLayoutPagerAdapter = new TabLayoutPagerAdapter(getSupportFragmentManager());
        tabLayoutPagerAdapter.addFragment(new ChatListFragment(), "");
        tabLayoutPagerAdapter.addFragment(new ContactListFragment(), "");
        viewPager.setAdapter(tabLayoutPagerAdapter);

        TabLayout tabLayout=findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_chat_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_contacts_white_24dp);

        peerLinkServiceIntent=new Intent(this, PeerLinkMainService.class);
        if(!isMyServiceRunning(PeerLinkMainService.class)){
            Log.d(TAG, "onCreate: starting the background service");
            startService(peerLinkServiceIntent);
        }

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: stopping the service");
        stopService(peerLinkServiceIntent);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.settings_menu){
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.about_menu){
            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //this method needs code to detect that the ActivityResult is coming from Orbot.


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d(TAG, "isMyServiceRunning: true");
                return true;
            }
        }
        Log.d(TAG, "isMyServiceRunning: false");
        return false;
    }


}
