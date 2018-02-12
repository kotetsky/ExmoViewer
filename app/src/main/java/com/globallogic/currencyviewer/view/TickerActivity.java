package com.globallogic.currencyviewer.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by oleh.kotetskyi on 2/10/2018.
 */

public class TickerActivity extends Activity{

    private TextView mCurrencyNameTextView;
    private TextView mCurrentBuyPriceTextView;
    private TextView mCurrentSellPriceTextView;
    private TextView mHighestDailyPriceTextView;
    private TextView mLowestDailyPriceTextView;
    private TextView mDailyTradeVolumeTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
