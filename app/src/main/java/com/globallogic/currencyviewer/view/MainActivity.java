package com.globallogic.currencyviewer.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.globallogic.currencyviewer.R;
import com.globallogic.currencyviewer.data_layer.controller.CurrencyManager;
import com.globallogic.currencyviewer.model.CryptoCurrency;
import com.globallogic.currencyviewer.model.CurrencyExmoTicker;
import com.globallogic.currencyviewer.model.TickerItem;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements CurrencyManager.TickerCallback, CurrencyAdapter.TickerItemClickCallback {

    public static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private CurrencyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TickerItem> mTickerItems;
    private TickerItem mCurrentTickerItem;
    private List<CryptoCurrency> mCurrencies;

    private Button mStartExmoFetchingButton;
    private Button mShowChartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.poloniex_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mStartExmoFetchingButton = findViewById(R.id.exmo_fetching_button);
        mStartExmoFetchingButton.setOnClickListener((View v) -> {
            getTickersFromExmo();
        });

        mShowChartButton = findViewById(R.id.exmo_show_chart);
        mShowChartButton.setOnClickListener((View v) -> {
            CandleChartActivity.startActivity(MainActivity.this);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTickersFromExmo();
    }

    private void getTickersFromExmo() {
        Log.d(TAG, "in getTickersFromExmo");
        CurrencyManager currencyManager = new CurrencyManager();
        currencyManager.setTickerCallback(this);
        currencyManager.getExmoTicker();
    }

    @Override
    public void onTickerReceived(CurrencyExmoTicker currencyEnqueueExmoTicker) {
        Log.d(TAG, "onTickerReceived currencyExmoTicker: ");
        mTickerItems = currencyEnqueueExmoTicker.getTickerItems();
        mCurrencies = currencyEnqueueExmoTicker.createSimpleCurrencyList();
        mAdapter = new CurrencyAdapter(mCurrencies, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private TickerItem getTickerItemByName(String tickerItemName) {
        for (TickerItem eachTickerItem : mTickerItems) {
            if (eachTickerItem.getName().equals(tickerItemName)) {
                return eachTickerItem;
            }
        }
        return null;
    }

    @Override
    public void onTickerItemClick(String tickerItemName) {
        TickerItem tickerItem = getTickerItemByName(tickerItemName);
        if (tickerItem == null) {
            return;
        }
        TickerActivity.startActivity(this, tickerItem);
    }
}
