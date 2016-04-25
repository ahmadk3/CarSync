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
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by ahmad on 25/04/2016.
 */
public class Requisicoes extends AppCompatActivity{

    private Button btnVelocidade;
    private Button btnRpm;

    private TextView txtVelocidade;
    private TextView txtRPM;

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

        txtVelocidade = (TextView)findViewById(R.id.textView_velocidade);
        txtRPM = (TextView)findViewById(R.id.textView_rpm);

        btnVelocidade = (Button)findViewById(R.id.button_velocidade);
        btnVelocidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVelocidade(v);
            }
        });

        btnRpm = (Button)findViewById(R.id.button_rpm);
        btnRpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRPM(v);
            }
        });
    }

    public void getVelocidade(View v){

        String respDec = Comunicacao.msgRun("010D").substring(4);
        Log.d("RESP", respDec);
        Long i = Long.parseLong(respDec, 16);
        Log.d("RESP", Long.toString(i));
        txtVelocidade.setText(Long.toString(i) + " Km/h");
    }

    public void getRPM(View v){
        String resp = Comunicacao.msgRun("010C");
        txtRPM.setText(resp);
    }
}
