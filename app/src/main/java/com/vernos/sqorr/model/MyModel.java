package com.vernos.sqorr.model;

import com.vernos.sqorr.contracts.MyContracts;

public class MyModel implements MyContracts.Model {

    @Override
    public String getData() {

        String msg = "Welcome to my World";


        return msg;
    }
}
