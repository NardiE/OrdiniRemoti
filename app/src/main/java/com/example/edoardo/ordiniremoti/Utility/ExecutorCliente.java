package com.example.edoardo.ordiniremoti.Utility;


import com.example.edoardo.ordiniremoti.Activity.CercaCliente;

public class ExecutorCliente implements Runnable {
    private CercaCliente sc;
    String param;

    public CercaCliente getSc() {
        return sc;
    }

    public void setSc(CercaCliente sc) {
        this.sc = sc;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public ExecutorCliente(CercaCliente sc, String param) {
        this.sc = sc;
        this.param = param;
    }

    @Override
    public void run() {

    }

}
