package com.example.ahmad.bluetooth_teste;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button bOn,bOff,bList,bVisibilty,bConnect;
    TextView text;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice>pairedDevices;
    RadioGroup lv;
    RelativeLayout rl;
    ArrayList list = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl = (RelativeLayout)findViewById(R.id.relativelayout);

        bOn = (Button)findViewById(R.id.button_on);
        bOff = (Button)findViewById(R.id.button_off);
        bVisibilty = (Button)findViewById(R.id.button_visibilty);
        bList = (Button)findViewById(R.id.button_list);
        bConnect = (Button)findViewById(R.id.button_connect);

        text = (TextView)findViewById(R.id.textView_BluetoothTeste);

        lv = (RadioGroup)findViewById(R.id.listView);

        BA = BluetoothAdapter.getDefaultAdapter();

        bOn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                turnOn(v);
            }
        });
        bOff.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                turnOff(v);
            }
        });
        bVisibilty.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                makeVisible(v);
            }
        });
        bList.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                getList(v);
            }
        });
        bConnect.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                connect(v);
            }
        });
    }

    public void turnOn(View v){
        if(!BA.isEnabled()){
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(),"Turned on",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"Already on",Toast.LENGTH_LONG).show();
        }
    }

    public void turnOff(View v){
        BA.disable();
        Toast.makeText(getApplicationContext(),"Turned off" ,Toast.LENGTH_LONG).show();
    }

    public  void makeVisible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    public void getList(View v){
        pairedDevices = BA.getBondedDevices();
        for(BluetoothDevice bt : pairedDevices){
            list.add(bt.getName());
        }
        RadioButton r[] = new RadioButton[list.size()];
        Toast.makeText(getApplicationContext(),"Showing Paired Devices",Toast.LENGTH_SHORT).show();
        lv.removeAllViews();
        for(int i = 0; i < list.size(); i++){
            r[i] = new RadioButton(this);
            r[i].setText(list.get(i).toString());
            lv.addView(r[i]);
        }
        //rl.addView(lv);
        //final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        Toast.makeText(getApplicationContext(),"Showing Paired Devices",Toast.LENGTH_SHORT).show();
    }

    public void connect(View v){
        int btDevice = lv.getCheckedRadioButtonId();

        Toast.makeText(getApplicationContext(),"Connecting to "+list.get(btDevice - 1).toString(),Toast.LENGTH_SHORT).show();
    }
}
