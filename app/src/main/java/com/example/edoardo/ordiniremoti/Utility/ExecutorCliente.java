package com.example.edoardo.ordiniremoti.Utility;


import com.example.edoardo.ordiniremoti.Activity.SearchClient;

import java.util.List;

public class ExecutorCliente implements Runnable {
    private SearchClient sc;
    String param;

    public SearchClient getSc() {
        return sc;
    }

    public void setSc(SearchClient sc) {
        this.sc = sc;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public ExecutorCliente(SearchClient sc, String param) {
        this.sc = sc;
        this.param = param;
    }

    @Override
    public void run() {

    }

}
