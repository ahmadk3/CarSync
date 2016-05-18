package br.com.fei.carsync.manager.model;

import android.util.Log;
import android.util.StringBuilderPrinter;

import br.com.fei.carsync.view.activity.Comunicacao;
import br.com.fei.carsync.view.activity.Requisicoes;

/**
 * Created by PamelaPeixinho on 5/6/16.
 */
public class CargaMotor extends AbstractComandoOBD {

    public CargaMotor() {
        super("0104");
    }

    @Override
    public void run() {
//        -----------------
//        setRunning(true);
//        while(!this.isInterrupted()){
//        while(isRunning()){
//        -----------------

        while(true){
            Log.d("Thread", "Carga Motor");
            setResposta(Comunicacao.sendReceiveOBD(getPID())); //synchronized function
            calculate();
            Requisicoes.respCargaMotor = getResposta();
            Requisicoes.updateRequisicoesView();
            Log.d("TAG", "Resp = " + getResposta());
            try {
                sleep(1000);
                if (isSuspend()) {
                    synchronized(this) {
                        Log.d("TAG", "Carga Motor");
                        while(isSuspend())
                            wait();
                        Log.d("TAG", "Sai, não estou mais suspenso Carga Motor");
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
        if (!respAux.substring(0,2).equals("41"))
            return;
        setResposta(respAux);
        respAux = respAux.substring(respAux.length() - 2);
        double i = Long.parseLong(respAux, 16);
        i*= (100/255.0);
        Log.d("Thread", "Resp CargaMotor Dec: " + String.valueOf(i));
        setResposta(String.valueOf(i));
    }

    @Override
    public synchronized void notifyThread() {
        if (!isSuspend()){
            notify();
        }
    }
}
