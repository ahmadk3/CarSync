package br.com.fei.carsync.manager.model;

import android.util.Log;

import br.com.fei.carsync.view.activity.Comunicacao;
import br.com.fei.carsync.view.activity.Requisicoes;

/**
 * Created by PamelaPeixinho on 4/27/16.
 */
public class Velocidade extends AbstractComandoOBD{


    public Velocidade(){
        super("010D");
    }

    @Override
    public void run() {
//        setSuspend(false);

//        -----------------
//        setRunning(true);
//        while(!this.isInterrupted()){
//        while(isRunning()){
//        -----------------

        while(true){
            Log.d("TAG", "Velocidade");
            setResposta(Comunicacao.sendReceiveOBD(getPID())); //synchronized function
            calculate();
            Requisicoes.respVelocidade = getResposta();
            Log.d("TAG", "resp = " + getResposta());
            Requisicoes.updateRequisicoesView();

            try {
                sleep(1000);
                if (isSuspend()) {
                    synchronized(this) {
                        Log.d("Thread", "Suspender Velocidade");
                        while(isSuspend())
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
        Log.d("TAG", respAux);
        if (!respAux.substring(0,2).equals("41"))
            return;
        respAux = respAux.substring(respAux.length() - 2);
        Long i = Long.parseLong(respAux, 16);
        setResposta(Long.toString(i));
    }

    @Override
    public synchronized void notifyThread(){

        if (!isSuspend())
            notify();
    }
}
