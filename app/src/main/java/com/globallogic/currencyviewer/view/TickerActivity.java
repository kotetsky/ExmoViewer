package com.globallogic.currencyviewer.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.data.CandleEntry;
import com.globallogic.currencyviewer.R;
import com.globallogic.currencyviewer.data_layer.controller.CurrencyManager;
import com.globallogic.currencyviewer.model.TickerItem;

import java.util.List;

/**
 * TickerActivity
 */

public class TickerActivity extends Activity implements CurrencyManager.CandleEntriesCallback {

    private static final String EXTRA_TICKER_ITEM = "ticker_item_name";

    private TextView mCurrencyNameTextView;
    private TextView mCurrentBuyPriceTextView;
    private TextView mCurrentSellPriceTextView;
    private TextView mHighestDailyPriceTextView;
    private TextView mLowestDailyPriceTextView;
    private TextView mDailyTradeVolumeTextView;

    private Button mChartButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate_ticker);
        mCurrencyNameTextView = findViewById(R.id.ticer_item_name_value);
        mCurrentBuyPriceTextView = findViewById(R.id.current_buy_price_value);
        mCurrentSellPriceTextView = findViewById(R.id.current_sell_price_value);
        mHighestDailyPriceTextView = findViewById(R.id.highest_price_value);
        mLowestDailyPriceTextView = findViewById(R.id.lowest_price_value);
        mDailyTradeVolumeTextView = findViewById(R.id.volume_trade_value);
        mChartButton = findViewById(R.id.exmo_fetching_button);
    }

    public void setSeparateTickerValues(Intent intent) {
        Bundle extra = intent.getExtras();
        TickerItem tickerItem = (TickerItem) extra.getSerializable(EXTRA_TICKER_ITEM);
        mCurrencyNameTextView.setText(tickerItem.getName());
        mCurrentBuyPriceTextView.setText(String.valueOf(tickerItem.buy_price));
        mCurrentSellPriceTextView.setText(String.valueOf(tickerItem.sell_price));
        mHighestDailyPriceTextView.setText(String.valueOf(tickerItem.high));
        mLowestDailyPriceTextView.setText(String.valueOf(tickerItem.low));
        mDailyTradeVolumeTextView.setText(String.valueOf(tickerItem.vol));
        mChartButton.setOnClickListener((view) -> {
            CandleChartActivity.startActivity(this, tickerItem.getName());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSeparateTickerValues(getIntent());
    }

    public static void startActivity(Context context, TickerItem tickerItem) {
        Intent intent = new Intent(context, TickerActivity.class);
        Bundle extra = new Bundle();
        extra.putSerializable(EXTRA_TICKER_ITEM, tickerItem);
        intent.putExtras(extra);

        context.startActivity(intent);
    }

    @Override
    public void onCandleEntriesReceived(List<CandleEntry> candleEntries) {

    }
}
