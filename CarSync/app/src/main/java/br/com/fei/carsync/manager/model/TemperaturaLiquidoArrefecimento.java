package br.com.fei.carsync.manager.model;

import android.util.Log;

import br.com.fei.carsync.view.activity.Comunicacao;
import br.com.fei.carsync.view.activity.Requisicoes;

/**
 * Created by PamelaPeixinho on 5/9/16.
 */
    public class TemperaturaLiquidoArrefecimento extends AbstractComandoOBD {

    // TESTAR
//    1F - Time Since Engine Start
//public TemperaturaLiquidoArrefecimento(){ super("011F"); }

    public TemperaturaLiquidoArrefecimento(){ super("0105"); }

            @Override
            public void run() {
    //        setSuspend(false);
                while(true){
                    setResposta(Comunicacao.sendReceiveOBD(getPID())); //synchronized function
                    calculate();
                    Requisicoes.respTemperaturaLiquidoArrefecimento = getResposta();
                    Requisicoes.updateRequisicoesView();
                    Log.d("Thread", "TempLiquidoArrefecimento");
                    try {
                        Thread.sleep(1000);
                        if (isSuspend()) {
                            synchronized(this) {
                                Log.d("Thread", "Suspender TempLiquidoArrefecimento");
                                while(isSuspend())
                                    wait();
                                Log.d("Thread", "Sai, não estou mais suspenso TempLiquidoArrefecimento");
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
                if (!respAux.substring(0,2).equals("41"))
                    return;
                respAux = respAux.substring(respAux.length() - 2);
                Long i = Long.parseLong(respAux, 16);
                i-=40;
                Log.d("Thread", "Resp TempLiquidoArrefecimento Dec: " + String.valueOf(i));
                setResposta(Long.toString(i));

            }

            @Override
            public synchronized void notifyThread(){
                if (!isSuspend())
                    notify();
            }
        }

