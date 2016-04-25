package com.example.ahmad.bluetooth_teste;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


/**
 * Created by ahmad on 22/04/2016.
 */
public class Comunicacao extends AppCompatActivity{

    private final int REQUEST_CONNECT_DEVICE = 1;
    private static BluetoothAdapter BA = null;

    private static BluetoothSocket socket = null;
    private static BluetoothDevice device = null;

    private static InputStream in = null;
    private static OutputStream out = null;

    private static String resposta = null;
    private static String rawData = null;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//Toast.makeText(getApplicationContext(),"Conectando a "+ ConfiguracaoBluetooth.connectedDevice,Toast.LENGTH_SHORT).show();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        BA = BluetoothAdapter.getDefaultAdapter();

        //Inicia a classe de conexao ConfiguracaoBluetooth
        Intent serverIntent = new Intent(this, ConfiguracaoBluetooth.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    //Cancelar a descoberta
                    BA.cancelDiscovery();

                    // Obtem o endereço do dispositivo
                    String address = data.getExtras().getString(ConfiguracaoBluetooth.EXTRA_DEVICE_ADDRESS);
                    // Obtem o BluetoothDevice
                    device = BA.getRemoteDevice(address);
                    Log.d("ADDRESS", address);

                    try {
                        // Cria o socket utilizando o UUID
                        socket = device.createRfcommSocketToServiceRecord(MY_UUID);

                        // Conecta ao dispositivo escolhido
                        Log.d("Connect", "foi");
                        socket.connect();

                        // Obtem os fluxos de entrada e saida que lidam com transmissões através do socket
                        in = socket.getInputStream();
                        out = socket.getOutputStream();
                        Log.d("Run", " foi");
                        runConectionConfigurations();
                        Intent intent = new Intent(this, Requisicoes.class);
                        startActivity(intent);
                    } catch (IOException e) {

                        Toast.makeText(this, "Ocorreu um erro!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void runConectionConfigurations (){

        msgRun("AT Z");
        msgRun("AT E0");
        msgRun("AT E0");
        msgRun("AT L0");
        msgRun("AT ST" + Integer.toHexString(0xFF & 62));
        // Protocolo
        msgRun("AT SP 0");
    }

    public static String msgRun (String cod){
        try {
            out.write((cod + "\r").getBytes());
            out.flush();
            Log.d("msgRun", "Mandou MSG");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("msgRun", "Nao foi possivel mandar msg " + cod );
        }

        try {
            resposta = readRawData(in);
            Log.d("msgRun", "Recebeu Resp: " + resposta);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("msgRun", "Nao foi possivel receber msg " + cod );
        }
        return resposta;
    }

    protected static String readRawData(InputStream in) throws IOException {
        byte b = 0;
        StringBuilder res = new StringBuilder();

        char c;
        while (((b = (byte) in.read()) > -1)) {
            c = (char) b;
            if (c == '>') // read until '>' arrives
            {
                break;
            }
            res.append(c);
        }

        rawData = res.toString().replaceAll("SEARCHING", "");
        rawData = rawData.replaceAll("\\s", "");//removes all [ \t\n\x0B\f\r]

        return rawData;
    }
}
