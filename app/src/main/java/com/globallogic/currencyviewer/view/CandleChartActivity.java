package com.globallogic.currencyviewer.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.globallogic.currencyviewer.R;
import com.globallogic.currencyviewer.data_layer.controller.CurrencyManager;

import java.util.List;

public class CandleChartActivity extends BaseChartActivity
        implements SeekBar.OnSeekBarChangeListener, CurrencyManager.CandleEntriesCallback {

    private static final String TAG = CandleChartActivity.class.getSimpleName();
    private static final String EXTRA_TICKER_NAME = "extra_ticker_name";

    private CandleStickChart mChart;
    private String mCurrentTradeItemName;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_candlechart);

        tvX =  findViewById(R.id.tvXMax);
        tvY =  findViewById(R.id.tvYMax);
        mSeekBarX =  findViewById(R.id.seekBar1);
        mSeekBarX.setOnSeekBarChangeListener(this);
        mSeekBarY =  findViewById(R.id.seekBar2);
        mSeekBarY.setOnSeekBarChangeListener(this);
        mChart =  findViewById(R.id.chart1);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(100);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        // setting data
        mSeekBarX.setProgress(20);
        mSeekBarY.setProgress(100);

        mChart.getLegend().setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.candle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IDataSet set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleIcons: {
                for (IDataSet set : mChart.getData().getDataSets())
                    set.setDrawIcons(!set.isDrawIconsEnabled());

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if (mChart.getData() != null) {
                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                    mChart.invalidate();
                }
                break;
            }
            case R.id.actionTogglePinch: {
                if (mChart.isPinchZoomEnabled())
                    mChart.setPinchZoom(false);
                else
                    mChart.setPinchZoom(true);

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
                mChart.notifyDataSetChanged();
                break;
            }
            case R.id.actionToggleMakeShadowSameColorAsCandle: {
                for (ICandleDataSet set : mChart.getData().getDataSets()) {
                    //TODO: set.setShadowColorSameAsCandle(!set.getShadowColorSameAsCandle());
                }

                mChart.invalidate();
                break;
            }
            case R.id.animateX: {
                mChart.animateX(3000);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(3000);
                break;
            }
            case R.id.animateXY: {

                mChart.animateXY(3000, 3000);
                break;
            }
            case R.id.actionSave: {
                if (mChart.saveToGallery("title" + System.currentTimeMillis(), 50)) {
                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
                break;
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        CurrencyManager currencyManager = new CurrencyManager();
        currencyManager.setTradeItemsCallback(this);
        mCurrentTradeItemName = getIntent().getStringExtra(EXTRA_TICKER_NAME);
        currencyManager.getTrades(mCurrentTradeItemName);
    }

    @Override
    public void onCandleEntriesReceived(List<CandleEntry> candleEntries) {
        Log.d(TAG, "onCandleEntriesReceived");
        invalidateCandleDataSet(candleEntries);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG, "progress changed = " + progress);
        if (seekBar.getId() == mSeekBarX.getId()) {
            Log.d(TAG, "X bar progress changed");
        } else {
            Log.d(TAG, "Y bar progress changed");
        }
        changeCandles();
    }

    private void changeCandles() {
        int quantity = (mSeekBarX.getProgress() < 1)
                ?  1 : mSeekBarX.getProgress();

        tvX.setText("" + quantity);
        tvY.setText("" + (mSeekBarY.getProgress()));
    }

    private void invalidateCandleDataSet(List<CandleEntry> yVals1) {
        mChart.resetTracking();
        CandleDataSet set1 = new CandleDataSet(yVals1, "Data Set");

        set1.setDrawIcons(false);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setShadowColor(Color.DKGRAY);
        set1.setShadowWidth(0.7f);
        set1.setDecreasingColor(Color.RED);
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(Color.rgb(122, 242, 84));
        set1.setIncreasingPaintStyle(Paint.Style.STROKE);
        set1.setNeutralColor(Color.BLUE);
        set1.setHighlightLineWidth(1f);

        CandleData data = new CandleData(set1);

        mChart.setData(data);
        mChart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    public static void startActivity(Context context, String tickerItemName) {
        Intent intent = new Intent(context, CandleChartActivity.class);
        intent.putExtra(EXTRA_TICKER_NAME, tickerItemName);
        context.startActivity(intent);
    }

}
