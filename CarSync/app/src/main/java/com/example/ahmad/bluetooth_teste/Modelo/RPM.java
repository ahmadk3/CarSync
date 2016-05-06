package com.example.ahmad.bluetooth_teste.Modelo;

import android.util.Log;

import com.example.ahmad.bluetooth_teste.Activities.Comunicacao;
import com.example.ahmad.bluetooth_teste.Activities.Requisicoes;

/**
 * Created by ahmad on 27/04/2016.
 */
public class RPM extends AbstractComandoOBD {

    public RPM(){ super("010C"); }

    @Override
    public void run() {
        setSuspend(false);
        while(!isSuspend()){
            setResposta(Comunicacao.sendReceiveOBD(getPID())); //synchronized function
            calculate();
            Requisicoes.respRPM = getResposta();
            Requisicoes.updateRequisicoesView();
            Log.d("Thread", "RPM");
            try {
                Thread.sleep(1000);
                if (isSuspend()) {
                    synchronized(this) {
                        Log.d("Thread", "Suspender RPM");
                        wait();
                        Log.d("Thread", "Sai, não estou mais suspenso RPM");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            Log.d("RPM ", getResposta());
        }

//        Log.d("Thread", "SAI DO LOOP DE REQUISICOES DO RPM");
//        this.currentThread().interrupt();
//        Log.d("Thread", "isInterrupted?" + String.valueOf(this.currentThread().isInterrupted()));
    }

    @Override
    protected void calculate() {
        String respAux = getResposta();
        respAux = respAux.substring(respAux.length() - 4);
        Long i = Long.parseLong(respAux, 16);
        i/=4;
        setResposta(Long.toString(i));
    }

    @Override
    public synchronized void notifyThread(){
        setSuspend(false);
        if (!isSuspend())
            notify();
    }
}
