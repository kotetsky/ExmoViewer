package com.globallogic.currencyviewer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.globallogic.currencyviewer.R;
import com.globallogic.currencyviewer.data_layer.CurrencyManager;
import com.globallogic.currencyviewer.model.CryptoCurrency;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CurrencyManager.TickerCallback {

    public static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private CurrencyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<CryptoCurrency> mCurrencies;

    private Button mStartExmoFetchingButton;
    private Button mShowChartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.poloniex_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        initExampleCurrencies();

        mAdapter = new CurrencyAdapter(mCurrencies);
        mRecyclerView.setAdapter(mAdapter);

        mStartExmoFetchingButton = findViewById(R.id.exmo_fetching_button);
        mStartExmoFetchingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTickersFromExmo();
            }
        });

        mShowChartButton = findViewById(R.id.exmo_show_chart);
        mShowChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChartActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initExampleCurrencies() {

        mCurrencies = new ArrayList<>(5);
        mCurrencies.add(new CryptoCurrency("ZCashe", 500));
        mCurrencies.add(new CryptoCurrency("Ripple", 2));
        mCurrencies.add(new CryptoCurrency("Bitcoin", 15000));
        mCurrencies.add(new CryptoCurrency("BitcoinCash", 2600));
        mCurrencies.add(new CryptoCurrency("Etherium", 1000));
    }

    public void getTickersFromExmo() {
        Log.d(TAG, "in getTickersFromExmo");
        CurrencyManager currencyManager = new CurrencyManager(this);
        currencyManager.getExmoTicker();
    }

    public void getPoloniexTicker (){
        Log.d(TAG, "get poloniex ticker");

        CurrencyManager currencyManager = new CurrencyManager(this);
        currencyManager.getPoloniexTicker();
    }

    public void startChartActivity(){
        Intent intent = new Intent(this, CandleChartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTickerReceived(List<CryptoCurrency> cryptoCurrencyList) {
        Log.d(TAG, "onTickerReceived currencyExmoTicker: ");
        for (CryptoCurrency currency : cryptoCurrencyList) {
            Log.d(TAG, "currency name = " + currency.getName() + ", value " + currency.getValue());
        }

        mAdapter.addCurrencies(cryptoCurrencyList);

    }
}
