package br.com.fei.carsync.view.activity;

import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import br.com.fei.carsync.R;
import br.com.fei.carsync.manager.model.TemperaturaOleoMotor;
import br.com.fei.carsync.manager.model.Velocidade;
import br.com.fei.carsync.manager.model.NivelEntradaCombustivelTanque;
import br.com.fei.carsync.manager.model.RPM;

/**
 * Created by ahmad on 25/04/2016.
 */
public class Requisicoes extends AppCompatActivity {


    private TabHost tabHost;
    private Button btnCombustivel;

    private static TextView txtVelocidade;
    private static TextView txtRPM;
    private static TextView txtTemperaturaOleoMotor;
    private static TextView txtNivelCombustivelTanque;

    public static String respVelocidade;
    public static String respNivelCombustivelTanque;
    public static String respTemperaturaOleoMotor;
    public static String respRPM;

    private Velocidade velocidade;
    private RPM rpm;
    private NivelEntradaCombustivelTanque nivelCombustivelTanque;
    private TemperaturaOleoMotor temperaturaOleoMotor;


    private static Handler mHandler = new Handler();

    private int tabAnterior;

    public void onCreate(Bundle savedInstanceState) {

        //Set Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        tabHost = (TabHost) findViewById(R.id.tabHost);

        if (tabHost == null) {
            Log.d("Thread", "TabHost == NULL");
            return;
        }

        tabHost.setup();

        TabHost.TabSpec aba1 = tabHost.newTabSpec("0");
        aba1.setContent(R.id.PRIMEIRA);
        aba1.setIndicator("Performance");

        TabHost.TabSpec aba2 = tabHost.newTabSpec("1");
        aba2.setContent(R.id.SEGUNDA);
        aba2.setIndicator("Controle de Gastos");

        TabHost.TabSpec aba3 = tabHost.newTabSpec("2");
        aba3.setContent(R.id.TERCEIRA);
        aba3.setIndicator("Prevenção de Problemas");

        tabHost.addTab(aba1);
        tabHost.addTab(aba2);
        tabHost.addTab(aba3);

        txtVelocidade = (TextView) findViewById(R.id.textView_velocidade);
        txtRPM = (TextView) findViewById(R.id.textView_rpm);
//        txtNivelCombustivelTanque = (TextView)findViewById(R.id.textView_combustivel);
        txtTemperaturaOleoMotor = (TextView) findViewById(R.id.textView_tempOleoMotor);

        Log.d("TAG", "ESTOU AQUI");

        //Set Threads
        velocidade = new Velocidade();
        rpm = new RPM();
//        nivelCombustivelTanque = new NivelEntradaCombustivelTanque();
//        temperaturaOleoMotor = new TemperaturaOleoMotor();

        tabAnterior = 0;

        velocidade.start();
        Log.d("TAG", "start velocidade");
        rpm.start();
        Log.d("TAG", "start RPM");

//        -----
//        nivelCombustivelTanque.start();
//        nivelCombustivelTanque.setSuspend(true);
//        -----
//        -----
//        temperaturaOleoMotor.start();
//        temperaturaOleoMotor.setSuspend(true);
//        -----

        //Manage Threads with layout
        tabHost.getCurrentTabTag();
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.d("TAG", "Tabanterior = " + String.valueOf(tabAnterior));
                Log.d("TAG", "Tabatual = " + String.valueOf(tabHost.getCurrentTab()));

                if (tabAnterior == 0 && tabHost.getCurrentTab() != 0) {
//                    velocidade.setSuspend(true);
//                    rpm.setSuspend(true);
//                    -----------------
//                    velocidade.setRunning(false);
//                    Log.d("Running Velocidade", String.valueOf(velocidade.isRunning()));
//                    rpm.interrupt();
//                    -----------------
                }
                if (tabAnterior != 0 && tabHost.getCurrentTab() == 0) {
//                    velocidade.setSuspend(false);
//                    velocidade.notifyThread();
//
//                    rpm.setSuspend(false);
//                    rpm.notifyThread();

//                    -----------------
//                    velocidade = new Velocidade();
//                    velocidade.start();
//                    Log.d("Running Velocidade", String.valueOf(velocidade.isRunning()));
//                    rpm.interrupt();
//                    -----------------
                }
                if (tabAnterior == 1 && tabHost.getCurrentTab() != 1) {
//                    nivelCombustivelTanque.setSuspend(true);
                }
                if (tabAnterior != 1 && tabHost.getCurrentTab() == 1) {
//                    nivelCombustivelTanque.setSuspend(false);
//                    nivelCombustivelTanque.notifyThread();
                }
                if (tabAnterior == 2 && tabHost.getCurrentTab() != 2) {
//                    temperaturaOleoMotor.setSuspend(true);
                }
                if (tabAnterior != 2 && tabHost.getCurrentTab() == 2) {
//                    temperaturaOleoMotor.setSuspend(false);
//                    temperaturaOleoMotor.notifyThread();
                }

                tabAnterior = tabHost.getCurrentTab();
//                Log.d("COMB 2", " "+ tabHost.getCurrentTab() + " " + tabId);
            }
        });
    }

    public synchronized static void updateRequisicoesView() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                txtVelocidade.setText(respVelocidade);
                txtRPM.setText(respRPM);
//                txtNivelCombustivelTanque.setText(respNivelCombustivelTanque);
//                txtTemperaturaOleoMotor.setText(respTemperaturaOleoMotor);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Comunicacao.closeSocketConnection();
    }

    @Override
    public void onDestroy(){
        Comunicacao.closeSocketConnection();
        Log.d("TAG", "Destroy");
    }
}
