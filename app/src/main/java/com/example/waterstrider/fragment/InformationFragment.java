package com.example.waterstrider.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.waterstrider.ConnectedActivity;
import com.example.waterstrider.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class InformationFragment extends Fragment {
    Socket socket;
    Button update;
    TextView temperature;
    OutputStream out;
    InputStream in;
    Button draw;
    String tempStr;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, null);
        new Thread(new ConnectionControl()).start();
        update = view.findViewById(R.id.update);
        draw = view.findViewById(R.id.draw);
        temperature = view.findViewById(R.id.temperatureText);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new DrawWater()).start();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Updater()).start();
                temperature.setText("Temperature: " + tempStr);
            }
        });
        return view;
    }

    public class DrawWater implements Runnable
    {

        @Override
        public void run() {
            try {
                out = socket.getOutputStream();
                out.write("Draw".getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Updater implements Runnable
    {

        @Override
        public void run() {
            try {
                out = socket.getOutputStream();
                out.write("Update".getBytes());
                out.flush();
                in = socket.getInputStream();
                int lockSeconds = 10 * 1000;
                long lockThreadCheckpoint = System.currentTimeMillis();
                int availableBytes = in.available();
                while (availableBytes <= 0 && (System.currentTimeMillis() < lockThreadCheckpoint + lockSeconds)) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    availableBytes = in.available();
                }
                byte [] b = new byte[availableBytes];
                in.read(b, 0, availableBytes);
                tempStr = new String(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ConnectionControl implements Runnable
    {
        @Override
        public void run() {
            socket = ConnectedActivity.getSocket();
        }
    }

}

