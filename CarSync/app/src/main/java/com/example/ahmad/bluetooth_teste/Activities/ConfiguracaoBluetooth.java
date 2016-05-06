package com.example.ahmad.bluetooth_teste.Activities;

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
import android.view.Display;
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

import com.example.ahmad.bluetooth_teste.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ConfiguracaoBluetooth extends AppCompatActivity {

    Button bOn,bOff,bList,bConnect;
    TextView text;

    private BluetoothAdapter BA;
    private Set<BluetoothDevice>pairedDevices;

    RadioGroup RG;
    RelativeLayout rl;
    ArrayList list = new ArrayList();

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String connectedDevice;


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


        bOn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
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

//        Display display=getWindowManager().getDefaultDisplay();
//        int width=display.getWidth();
//        Log.d("Size = ", String.valueOf(width));
//        bOn.setWidth((width-1)/2);
//        Log.d("Bnts size = ", String.valueOf(width/2));
//        bOff.setWidth((width-1)/2);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Inicia um sinal de broadcast
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
        RG.clearDisappearingChildren();

        //Recebe todos os dispositivos já pareados
        pairedDevices = BA.getBondedDevices();
        for(BluetoothDevice bt : pairedDevices)
            list.add(bt.getName() + "\n" + bt.getAddress());

        //Adiciona os dispositivos já pareados a um RadioGroup
        RadioButton r[] = new RadioButton[list.size()];
        for(int i = 0; i < list.size(); i++){
            r[i] = new RadioButton(this);
            r[i].setText(list.get(i).toString());
            RG.addView(r[i]);
        }
        //Busca novos dispositivos
        doDiscovery();

    }

    private void doDiscovery() {
        // Caso já esteja buscando
        if (BA.isDiscovering()) {
            BA.cancelDiscovery();
        }

        // Começa a busca por novos dispositivos
        BA.startDiscovery();
        Log.d("STARTDISCOVERY", "TRUE");
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Quando um dispositivo é encontrado
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Recebe o novo dispositivo
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Caso não esteja pareado
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    Log.d("DISCOVERD", device.getName() + " " + device.getAddress());

                    //Adiciona o novo dispositivo ao RadioGroup
                    RadioButton rb = new RadioButton(context);
                    rb.setText(device.getName() + "\n" + device.getAddress());
                    RG.addView(rb);
                }
            }
        }
    };

    public void connect(View v){
        try {
            BA.cancelDiscovery();
            //Pega as inforamações do dispositivo selecionado
            int id = RG.getCheckedRadioButtonId();
            View rb = RG.findViewById(id);
            int radioId = RG.indexOfChild(rb);
            RadioButton b = (RadioButton) RG.getChildAt(radioId);
            String content = (String)b.getText();

            String address = content.substring(content.indexOf('\n') + 1, content.length());
            connectedDevice = content.substring(0, content.indexOf('\n') - 1);
            Log.d("CONNECTED ADDRESS ", address);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            //Finaliza a Activity e retorna ok para a classe Comunicacao
            setResult(Activity.RESULT_OK, intent);
            finish();

        }catch(Exception ee){
            Log.d("ERRO", "Falha na conexão");
        }
    }

    protected void onDestroy() {
        super.onDestroy();

        // Para ter certeza de que não está sendo feito discovery
        if (BA != null) {
            BA.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }
}
