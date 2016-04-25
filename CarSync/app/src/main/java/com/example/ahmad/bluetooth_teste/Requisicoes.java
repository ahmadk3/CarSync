package com.example.ahmad.bluetooth_teste;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by ahmad on 25/04/2016.
 */
public class Requisicoes extends AppCompatActivity{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec aba1=tabHost.newTabSpec("PRIMEIRA");
        aba1.setContent(R.id.PRIMEIRA);
        aba1.setIndicator("PRIMEIRA");

        TabHost.TabSpec aba2=tabHost.newTabSpec("SEGUNDA");
        aba2.setContent(R.id.SEGUNDA);
        aba2.setIndicator("SEGUNDA");

        TabHost.TabSpec aba3=tabHost.newTabSpec("TERCEIRA");
        aba3.setContent(R.id.TERCEIRA);
        aba3.setIndicator("TERCEIRA");

        tabHost.addTab(aba1);
        tabHost.addTab(aba2);
        tabHost.addTab(aba3);
    }
}
