package com.globallogic.currencyviewer.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.globallogic.currencyviewer.R;
import com.globallogic.currencyviewer.data_layer.controller.CurrencyManager;
import com.globallogic.currencyviewer.model.CryptoCurrency;
import com.globallogic.currencyviewer.model.CurrencyExmoTicker;
import com.globallogic.currencyviewer.model.TickerItem;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity
        implements CurrencyManager.TickerCallback, CurrencyAdapter.TickerItemClickCallback {

    public static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private CurrencyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TickerItem> mTickerItems;
    private TickerItem mCurrentTickerItem;
    private List<CryptoCurrency> mCurrencies;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.poloniex_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTickersFromExmo();
    }

    private void getTickersFromExmo() {
        // todo need to be overwritten with Callable
        CurrencyManager currencyManager = new CurrencyManager();
        currencyManager.setTickerCallback(this);
        currencyManager.getRxExmoTicker()
                .subscribe(this::onTickerReceived, this::onTickerReceiveError);
    }

    @Override
    public void onTickerReceived(CurrencyExmoTicker currencyEnqueueExmoTicker) {
        Log.d(TAG, "onTickerReceived currencyExmoTicker: ");
        mTickerItems = currencyEnqueueExmoTicker.getTickerItems();
        mCurrencies = currencyEnqueueExmoTicker.createSimpleCurrencyList();
        mAdapter = new CurrencyAdapter(mCurrencies, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void onTickerReceiveError (Throwable error){
        Log.d(TAG, "error: " + error.getMessage());
    }

    private TickerItem getTickerItemByName(String tickerItemName) {
        // todo need to be rewritten with rxJava 2
        /*
        for (TickerItem eachTickerItem : mTickerItems) {
            if (eachTickerItem.getName().equals(tickerItemName)) {
                return eachTickerItem;
            }
        }
        */
        return Observable.fromIterable(mTickerItems)
                .filter(tickerItem -> tickerItem.getName().equals(tickerItemName))
                .first(mCurrentTickerItem)
                .blockingGet();
    }

    @Override
    public void onTickerItemClick(String tickerItemName) {
        TickerItem tickerItem = getTickerItemByName(tickerItemName);
        if (tickerItem == null) {
            return;
        }
        TickerActivity.startActivity(this, tickerItem);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCompositeDisposable.clear();
    }
}
