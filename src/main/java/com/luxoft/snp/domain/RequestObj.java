package com.luxoft.snp.domain;

/**
 * Created by vharutyunyan on 20.06.2014.
 */
public class RequestObj {
    private boolean withCredentials;
    private Params params;

    public boolean isWithCredentials() {
        return withCredentials;
    }

    public void setWithCredentials(boolean withCredentials) {
        this.withCredentials = withCredentials;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }
}
