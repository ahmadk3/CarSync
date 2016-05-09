package br.com.fei.carsync.manager.model;

import android.util.Log;
import android.util.StringBuilderPrinter;

import br.com.fei.carsync.view.activity.Comunicacao;
import br.com.fei.carsync.view.activity.Requisicoes;

/**
 * Created by PamelaPeixinho on 5/6/16.
 */
public class NivelEntradaCombustivelTanque extends AbstractComandoOBD {

    public NivelEntradaCombustivelTanque() {
        super("012F");
    }

    @Override
    public void run() {
//        -----------------
//        setRunning(true);
//        while(!this.isInterrupted()){
//        while(isRunning()){
//        -----------------

        while(true){
            setResposta(Comunicacao.sendReceiveOBD(getPID())); //synchronized function
            calculate();
            Requisicoes.respNivelCombustivelTanque = getResposta();
            Requisicoes.updateRequisicoesViewControleGastos();
            Log.d("Thread", "Nivel Combustivel");
            try {
                sleep(1000);
                if (isSuspend()) {
                    synchronized(this) {
                        Log.d("Thread", "Suspender Combustivel");
                        while(isSuspend())
                            wait();
                        Log.d("Thread", "Sai, não estou mais suspenso Combustivel");
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
        Log.d("Thread", "Resp Combustivel: " + respAux);
        Long i = Long.parseLong(respAux, 16);
        i*= (100/255);
        Log.d("Thread", "Resp Combustivel Dec: " + String.valueOf(i));
        setResposta(Long.toString(i));
    }

    @Override
    public synchronized void notifyThread() {
        if (!isSuspend()){
            notify();
        }
    }
}
