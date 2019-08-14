package com.example.waterstrider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    EditText ipEditText;
    EditText portEditText;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBarMain);
        setSupportActionBar(toolbar);
        ipEditText = findViewById(R.id.ipEditText);
        portEditText = findViewById(R.id.portEditText);
    }

    public void connect(View view)
    {
        Intent intent = new Intent(MainActivity.this, ConnectedActivity.class);
        intent.putExtra("IP", ipEditText.getText().toString());
        intent.putExtra("Port", portEditText.getText().toString());
        startActivity(intent);
    }
}