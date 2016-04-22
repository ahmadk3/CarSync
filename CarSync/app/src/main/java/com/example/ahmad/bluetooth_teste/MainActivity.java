package com.example.ahmad.bluetooth_teste;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button bOn,bOff,bList,bConnect;
    TextView text;

    private BluetoothAdapter BA;
    private Set<BluetoothDevice>pairedDevices;
    private ArrayAdapter newDevices;

    RadioGroup RG, RG2;
    RelativeLayout rl;
    ArrayList list = new ArrayList();
    ArrayList newList = new ArrayList();

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static String EXTRA_DEVICE_ADDRESS = "device_address";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rl = (RelativeLayout)findViewById(R.id.relativelayout);

        BA = BluetoothAdapter.getDefaultAdapter();

        bOn = (Button)findViewById(R.id.button_on);
        bOff = (Button)findViewById(R.id.button_off);
        bList = (Button)findViewById(R.id.button_list);
        bConnect = (Button)findViewById(R.id.button_connect);

        text = (TextView)findViewById(R.id.textView_BluetoothTeste);

        RG = (RadioGroup)findViewById(R.id.radioGroup);
        RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(RG2.getCheckedRadioButtonId() != -1)
                    RG2.clearCheck();
            }
        });
        RG2 = (RadioGroup)findViewById(R.id.radioGroup2);
        RG2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(RG.getCheckedRadioButtonId() != -1)
                    RG.clearCheck();
            }
        });

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

        newDevices = new ArrayAdapter(this, R.layout.activity_main);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
    }

//    public boolean onCreateOptionsMenu(Menu menu){
//        menu.add(0, 0, 0, "STRING");
//        return true;
//    }

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

    public void getList(View v){
        list.clear();
        RG.removeAllViews();
        RG2.removeAllViews();

        pairedDevices = BA.getBondedDevices();
        for(BluetoothDevice bt : pairedDevices)
            list.add(bt.getName());

        RadioButton r[] = new RadioButton[list.size()];
        for(int i = 0; i < list.size(); i++){
            r[i] = new RadioButton(this);
            r[i].setText(list.get(i).toString());
            RG.addView(r[i]);
        }

        doDiscovery();
        //Toast.makeText(getApplicationContext(),"Showing Paired Devices" + newDevices.size(),Toast.LENGTH_SHORT).show();

    }

    private void doDiscovery() {
        // If we're already discovering, stop it
        if (BA.isDiscovering()) {
            BA.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        Log.d("start", "NEW");
        BA.startDiscovery();
        Log.d("startou", "NEW");
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    Log.d("DISCOVERY", device.getName() + " " + device.getAddress());
                    newDevices.add(device.getName() + "\n" + device.getAddress());
                    RadioButton rb = new RadioButton(context);
                    rb.setText(device.getName());
                    RG2.addView(rb);
                }
                // When discovery is finished, change the Activity title
            }
        }
    };

    public void connect(View v){
        try {
            BA.cancelDiscovery();
            BluetoothDevice btd = null;
            int btDevice = RG.getCheckedRadioButtonId();

            for(BluetoothDevice bt : pairedDevices) {
                //Aqui será selecionado o dispositivo a ser conectado
                if (bt.getName().equals(list.get(btDevice - 1))) {
                    String address = bt.getAddress();

                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
//
            }
           // Toast.makeText(getApplicationContext(),"Connecting to "+list.get(btDevice - 1).toString(),Toast.LENGTH_SHORT).show();
        }catch(Exception ee){
            Log.d("ERRO:", "2");

        }
    }

    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (BA != null) {
            BA.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }
}
