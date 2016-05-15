package br.com.fei.carsync.manager.model;

import android.util.Log;

import br.com.fei.carsync.view.activity.Comunicacao;
import br.com.fei.carsync.view.activity.Requisicoes;

/**
 * Created by ahmad on 27/04/2016.
 */
public class RPM extends AbstractComandoOBD {

    public RPM(){ super("010C"); }
//    public RPM(){ super("015C"); }
//    public RPM(){ super("0105"); }

    @Override
    public void run() {
//        setSuspend(false);
        while(true){
            Log.d("TAG", "RPM");
            setResposta(Comunicacao.sendReceiveOBD(getPID())); //synchronized function
            calculate();
            Requisicoes.respRPM = getResposta();
            Log.d("TAG", "RPM = " + getResposta());
            Requisicoes.updateRequisicoesView();

            try {
                Thread.sleep(1000);
//                if (isSuspend()) {
//                    synchronized(this) {
//                        Log.d("TAG", "Suspender RPM");
//                        while(isSuspend())
//                            wait();
//                        Log.d("TAG", "Sai, não estou mais suspenso RPM");
//                    }
//                }
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
        if (!respAux.substring(0,2).equals("41"))
            return;
        Log.d("TAG ","resp: " +respAux );
        respAux = respAux.substring(respAux.length() - 4);
        Long i = Long.parseLong(respAux, 16);
        i/=4;
        setResposta(Long.toString(i));
    }

    @Override
    public synchronized void notifyThread(){
        if (!isSuspend())
            notify();
    }
}
