package com.globallogic.poloniexviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<CryptoCurrency> mCurrencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.poloniex_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        initCurrencies();

        mAdapter = new CurrencyAdapter(mCurrencies);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initCurrencies() {

        mCurrencies = new ArrayList<>(5);
        mCurrencies.add(new CryptoCurrency("Bitcoin", 15000));
        mCurrencies.add(new CryptoCurrency("BitcoinCash", 2600));
        mCurrencies.add(new CryptoCurrency("Etherium", 1000));
        mCurrencies.add(new CryptoCurrency("ZCashe", 500));
        mCurrencies.add(new CryptoCurrency("Ripple", 2));

    }
}
