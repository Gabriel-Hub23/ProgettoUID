package com.pptattoo.pptattoo.Model;
public class Guadagno {
    private String data;
    private Integer guadagno;

    public Guadagno(String data, Integer numero){
        this.data = data;
        this.guadagno = numero;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getGuadagno() {
        return guadagno;
    }

    public void setGuadagno(Integer guadagno) {
        this.guadagno = guadagno;
    }
}
