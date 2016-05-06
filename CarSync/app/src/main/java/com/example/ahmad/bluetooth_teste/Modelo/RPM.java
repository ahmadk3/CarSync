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
        while(true){
            setResposta(Comunicacao.sendReceiveOBD(getPID())); //synchronized function
            calculate();
            Requisicoes.respRPM = getResposta();
            Requisicoes.updateRequisicoesView();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("RPM ", getResposta());
        }
    }

    @Override
    protected void calculate() {
        String respAux = getResposta();
        respAux = respAux.substring(respAux.length() - 4);
        Long i = Long.parseLong(respAux, 16);
        i/=4;
        setResposta(Long.toString(i));
    }
}
