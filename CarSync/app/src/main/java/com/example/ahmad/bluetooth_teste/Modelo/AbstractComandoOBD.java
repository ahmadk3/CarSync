package com.example.ahmad.bluetooth_teste.Modelo;

import android.os.Handler;
import android.util.Log;

import com.example.ahmad.bluetooth_teste.Activities.Comunicacao;
import com.example.ahmad.bluetooth_teste.Activities.Requisicoes;
import com.example.ahmad.bluetooth_teste.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

/**
 * Created by PamelaPeixinho on 4/27/16.
 */
public abstract class AbstractComandoOBD extends Thread{

    private String PID;
    private String resposta;
    private boolean isRunning;
    private boolean suspend;

    public AbstractComandoOBD(String PID){
        setPID(PID);
    }

    @Override
    public abstract void run();
    protected abstract void calculate();
    public abstract void notifyThread();

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isSuspend() {
        return suspend;
    }

    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }
}
