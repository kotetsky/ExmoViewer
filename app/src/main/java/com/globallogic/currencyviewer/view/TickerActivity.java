package com.globallogic.currencyviewer.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.globallogic.currencyviewer.R;
import com.globallogic.currencyviewer.model.TickerItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TickerActivity
 */

public class TickerActivity extends Activity {

    private static final String EXTRA_TICKER_ITEM = "ticker_item_name";

    public @BindView(R.id.ticer_item_name_value)
    TextView mCurrencyNameTextView;

    public @BindView(R.id.current_buy_price_value)
    TextView mCurrentBuyPriceTextView;

    public @BindView(R.id.current_sell_price_value)
    TextView mCurrentSellPriceTextView;

    public @BindView(R.id.highest_price_value)
    TextView mHighestDailyPriceTextView;

    public @BindView(R.id.lowest_price_value)
    TextView mLowestDailyPriceTextView;

    public @BindView(R.id.volume_trade_value)
    TextView mDailyTradeVolumeTextView;

    public @BindView(R.id.exmo_fetching_button)
    Button mChartButton;

    private TickerItem mCurrentTickerItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate_ticker);
        ButterKnife.bind(this);
    }

    public void setSeparateTickerValues(Intent intent) {
        Bundle extra = intent.getExtras();
        mCurrentTickerItem = (TickerItem) extra.getSerializable(EXTRA_TICKER_ITEM);
        mCurrencyNameTextView.setText(mCurrentTickerItem.getName());
        mCurrentBuyPriceTextView.setText(String.valueOf(mCurrentTickerItem.buy_price));
        mCurrentSellPriceTextView.setText(String.valueOf(mCurrentTickerItem.sell_price));
        mHighestDailyPriceTextView.setText(String.valueOf(mCurrentTickerItem.high));
        mLowestDailyPriceTextView.setText(String.valueOf(mCurrentTickerItem.low));
        mDailyTradeVolumeTextView.setText(String.valueOf(mCurrentTickerItem.vol));
    }

    @OnClick(R.id.exmo_fetching_button)
    public void showCandles() {
        CandleChartActivity.startActivity(this, mCurrentTickerItem.getName());
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

}
