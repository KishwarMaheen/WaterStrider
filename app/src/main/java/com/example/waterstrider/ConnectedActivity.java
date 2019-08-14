package com.example.waterstrider;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.example.waterstrider.adapter.ViewPagerAdapter;
import com.example.waterstrider.fragment.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ConnectedActivity extends AppCompatActivity {
    Intent intent;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    static String ip;
    static int port;
    static Socket socket;
    OutputStream out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);
        intent = getIntent();
        ip = intent.getStringExtra("IP");
        port = Integer.parseInt(intent.getStringExtra("Port"));
        new Thread(new Connection()).start();
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        setDataToViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setDataToViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ControlFragment(), "Control");
        viewPagerAdapter.addFragment(new InformationFragment(), "Information");
        viewPager.setAdapter(viewPagerAdapter);
    }

    public static Socket getSocket() {
        return socket;
    }

    public class Connection implements Runnable
    {

        @Override
        public void run() {
            try {
                socket = new Socket(InetAddress.getByName(ip), port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
