package com.example.waterstrider.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.waterstrider.ConnectedActivity;
import com.example.waterstrider.R;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ControlFragment extends Fragment {
    Socket socket;
    ImageView turnRight;
    ImageView turnLeft;
    ImageView turnUp;
    ImageView turnDown;
    OutputStream out;
    SeekBar seekBar;
    int seekBarProgress;
    WebView webView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, null);
        new Thread(new ConnectionControl()).start();
        turnRight = view.findViewById(R.id.right);
        turnLeft = view.findViewById(R.id.left);
        turnUp = view.findViewById(R.id.up);
        turnDown = view.findViewById(R.id.down);
        seekBar = view.findViewById(R.id.zoomBar);
        turnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new TurnRight()).start();
            }
        });
        turnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new TurnLeft()).start();
            }
        });
        turnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new TurnUp()).start();
            }
        });
        turnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new TurnDown()).start();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;
                new Thread(new ProgressSender()).start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarProgress = seekBar.getProgress();
                new Thread(new ProgressSender()).start();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        webView = view.findViewById(R.id.liveFeed);
        webView.loadUrl("http://192.168.0.102:8081/");
        return view;
    }

    public class ProgressSender implements Runnable
    {

        @Override
        public void run() {
            try {
                out = socket.getOutputStream();
                switch(seekBarProgress)
                {
                    case 0: out.write("0".getBytes());
                        break;
                    case 1: out.write("1".getBytes());
                        break;
                    case 2: out.write("2".getBytes());
                        break;
                    case 3: out.write("3".getBytes());
                        break;
                    case 4: out.write("4".getBytes());
                        break;
                    case 5: out.write("5".getBytes());
                        break;
                    case 6: out.write("6".getBytes());
                        break;
                    case 7: out.write("7".getBytes());
                        break;
                    case 8: out.write("8".getBytes());
                        break;
                    case 9: out.write("9".getBytes());
                        break;
                    case 10: out.write("10".getBytes());
                        break;
                    case 11: out.write("11".getBytes());
                        break;
                    case 12: out.write("12".getBytes());
                        break;
                    case 13: out.write("13".getBytes());
                        break;
                    case 14: out.write("14".getBytes());
                        break;
                    case 15: out.write("15".getBytes());
                        break;
                    case 16: out.write("16".getBytes());
                        break;
                    case 17: out.write("17".getBytes());
                        break;
                    case 18: out.write("18".getBytes());
                        break;
                    case 19: out.write("19".getBytes());
                        break;
                    case 20: out.write("20".getBytes());
                        break;
                }
                out.flush();
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

    public class TurnRight implements Runnable
    {

        @Override
        public void run() {
            try {
                out = socket.getOutputStream();
                out.write("Right".getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class TurnDown implements Runnable
    {

        @Override
        public void run() {
            try {
                out = socket.getOutputStream();
                out.write("Down".getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class TurnLeft implements Runnable
    {

        @Override
        public void run() {
            try {
                out = socket.getOutputStream();
                out.write("Left".getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class TurnUp implements Runnable
    {

        @Override
        public void run() {
            try {
                out = socket.getOutputStream();
                out.write("Up".getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
