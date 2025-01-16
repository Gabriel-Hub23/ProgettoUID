package com.pptattoo.pptattoo.Model;

import java.util.LinkedList;

public class DialogResponse {

    private LinkedList<String> response;

    public DialogResponse(LinkedList<String> response) {
        this.response = response;
    }

    public LinkedList<String> getResponse() {
        return response;
    }

}
