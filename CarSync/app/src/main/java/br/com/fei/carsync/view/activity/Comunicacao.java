package br.com.fei.carsync.view.activity;

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
import java.util.StringTokenizer;
import java.util.UUID;

import br.com.fei.carsync.R;


/**
 * Created by ahmad on 22/04/2016.
 */
public class Comunicacao extends AppCompatActivity {

    private final int REQUEST_CONNECT_DEVICE = 1;
    private static BluetoothAdapter BA = null;

    private static BluetoothSocket socket = null;

    private static InputStream in = null;
    private static OutputStream out = null;

    private static String resposta = null;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

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
                    BluetoothDevice device = BA.getRemoteDevice(address);
                    Log.d("ADDRESS", address);

                    try {
                        // Cria o socket utilizando o UUID
                        socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                        Log.d("TAG", "pegou socket");

                        Log.d("TAG", String.valueOf(socket.isConnected()));

                        // Conecta ao dispositivo escolhido
                        if (!socket.isConnected())
                            socket.connect();

                        Log.d("TAG", "connect foi");

                        // Obtem os fluxos de entrada e saida que lidam com transmissões através do socket
                        in = socket.getInputStream();
                        out = socket.getOutputStream();
                        Log.d("TAG", " in e out foi");

                        runConectionConfigurations();

                        Log.d("TAG", "configs");
                        Intent intent = new Intent(this, Requisicoes.class);
                        startActivity(intent);
                        Log.d("TAG", "Requisicoes");
                    } catch (IOException e) {

                        Toast.makeText(this, "Erro de conexão\nFavor verificar o dispositivo", Toast.LENGTH_LONG).show();
                        Intent serverIntent = new Intent(this, ConfiguracaoBluetooth.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                    }
                }
                break;
        }
    }

    private void runConectionConfigurations() {

        sendReceiveOBD("AT D");
        sendReceiveOBD("AT Z");
        sendReceiveOBD("AT E0");
        sendReceiveOBD("AT E0");
        sendReceiveOBD("AT L0");
        sendReceiveOBD("AT H0");
        sendReceiveOBD("AT S0");
        sendReceiveOBD("AT ST " + Integer.toHexString(0xFF & 62));
//        sendReceiveOBD("AT MA"); // teste

//        Possiveis problemas podem ser solucionados esses protocolos
//        ATD
//        ATZ
//        AT E0
//        AT L0
//        AT S0
//        AT H0
//        AT SP 0

        // Protocolo
        sendReceiveOBD("AT SP 0");
        Log.d("Configs", "PROTOCOLS RUNNED");
    }

    public synchronized static String sendReceiveOBD(String cod) {
//        try {
//            in = socket.getInputStream();
//            out = socket.getOutputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("TAG", "COMUNICACAO COM OUT E IN DO OBD");
//        }

        try {
            out.write((cod + "\r").getBytes());
            out.flush();
            Log.d("TAG", "mandando msg " + cod);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "Nao foi possivel mandar msg " + cod);
        }

        try {
            resposta = readRawData(in);
            Log.d("TAG", "Recebeu Resp: " + resposta);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "Nao foi possivel receber msg " + cod);
        }
        return resposta;
    }

    protected synchronized static String readRawData(InputStream in) throws IOException {
        byte b;
        StringBuilder res = new StringBuilder();
        Log.d("TAG", "is connected? " + String.valueOf(socket.isConnected()));
        Log.d("TAG", "estou aqui read raw data");
        char c;
        while (((b = (byte) in.read()) > -1)) {
            c = (char) b;
            Log.d("TAG", "char = " + c);

            if (c == '>') // read until '>' arrives
                break;

            Log.d("TAG while rawData", res.toString());
            res.append(c);
        }

        Log.d("TAG", res.toString());
        String rawData = res.toString().replaceAll("SEARCHING", "");
        rawData = rawData.replaceAll("\\s", ""); //removes all [ \t\n\x0B\f\r]
        Log.d("TAG", rawData);

        return rawData;
    }

    protected static void closeSocketConnection(){
        try {
            Log.d("TAG", String.valueOf(socket.isConnected()));
            Log.d("TAG", "Close socket");
            if (socket != null && socket.isConnected())
                socket.close();
            socket = null;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
