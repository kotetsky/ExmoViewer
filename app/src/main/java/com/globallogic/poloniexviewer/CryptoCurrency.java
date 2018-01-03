package com.globallogic.poloniexviewer;

/**
 * Created by oleh.kotetskyi on 1/3/2018.
 */

public class CryptoCurrency {

    private String mName;
    private float mValue;


    public CryptoCurrency(String name, float value) {
        mName = name;
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public float getValue() {
        return mValue;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setValue(float value) {
        mValue = value;
    }

}
