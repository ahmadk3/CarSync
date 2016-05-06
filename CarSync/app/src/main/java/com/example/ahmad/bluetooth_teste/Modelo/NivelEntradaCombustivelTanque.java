package com.example.ahmad.bluetooth_teste.Modelo;

import android.util.Log;

import com.example.ahmad.bluetooth_teste.Activities.Comunicacao;
import com.example.ahmad.bluetooth_teste.Activities.Requisicoes;

/**
 * Created by PamelaPeixinho on 5/6/16.
 */
public class NivelEntradaCombustivelTanque extends AbstractComandoOBD {

    public NivelEntradaCombustivelTanque() {
        super("012F");
    }

    @Override
    public void run() {
        setSuspend(false);

//        -----------------
//        setRunning(true);
//        while(!this.isInterrupted()){
//        while(isRunning()){
//        -----------------

        while(!isSuspend()){
            setResposta(Comunicacao.sendReceiveOBD(getPID())); //synchronized function
            calculate();
            Requisicoes.respNivelCombustivelTanque = getResposta();
            Requisicoes.updateRequisicoesViewControleGastos();
            Log.d("Thread", "Nivel Combustivel");
            try {
                Thread.sleep(100000);
                if (isSuspend()) {
                    synchronized(this) {
                        Log.d("Thread", "Suspender Velocidade");
                        wait();
                        Log.d("Thread", "Sai, não estou mais suspenso Velocidade");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Log.d("Velocidade: ", getResposta());
        }

//        Log.d("Thread", "SAI DO LOOP DE REQUISICOES DA VELOCIDADE");
//        this.currentThread().interrupt();
//        Log.d("Thread", "isInterrupted?" + String.valueOf(this.currentThread().isInterrupted()));
    }

    @Override
    protected void calculate() {
        String respAux = getResposta();
        respAux = respAux.substring(respAux.length() - 2);
        Long i = Long.parseLong(respAux, 16);
        i*= (100/255);
        setResposta(Long.toString(i));
    }

    @Override
    public void notifyThread() {
        setSuspend(false);
        if (!isSuspend())
            notify();
    }
}
