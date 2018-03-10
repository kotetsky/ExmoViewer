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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity
        implements CurrencyAdapter.TickerItemClickCallback {

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.poloniex_view)
    RecyclerView mRecyclerView;

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
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        getTickersFromExmo();
    }

    private void getTickersFromExmo() {
        new CurrencyManager()
                .getRxExmoTicker()
                .subscribe(this::onTickerReceived, this::onTickerReceiveError);
    }

    public void onTickerReceived(CurrencyExmoTicker currencyExmoTicker) {
        Log.d(TAG, "onTickerReceived currencyExmoTicker: ");
        mTickerItems = currencyExmoTicker.getTickerItems();
        mCurrencies = currencyExmoTicker.createSimpleCurrencyList();
        mAdapter = new CurrencyAdapter(mCurrencies, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void onTickerReceiveError(Throwable error) {
        Log.d(TAG, "error: " + error.getMessage());
    }

    private TickerItem getTickerItemByName(String tickerItemName) {

        // todo do we need to dispose this code ???
        Observable.fromIterable(mTickerItems)
                .filter(tickerItem -> tickerItem.getName().equals(tickerItemName))
                .take(1).subscribe(tickerItem -> mCurrentTickerItem = tickerItem
        );

        return mCurrentTickerItem;
    }

    public void onTickerItemClick(String tickerItemName) {
        TickerItem tickerItem = getTickerItemByName(tickerItemName);

        // todo do we need to dispose this code ???
        Observable.just(tickerItem)
                .filter(ticker1 -> ticker1 != null)
                .subscribe(notNullTickerItem -> {
                    TickerActivity.startActivity(this, notNullTickerItem);
                });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // todo cn we dispose many disposables at once in this compositeDisposable ???
        mCompositeDisposable.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

}
