package com.globallogic.currencyviewer.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globallogic.currencyviewer.R;
import com.globallogic.currencyviewer.model.CryptoCurrency;

import java.util.List;

/**
 * Created by oleh.kotetskyi on 1/3/2018.
 */

public class CurrencyAdapter extends RecyclerView.Adapter {

    public static final String TAG = CurrencyAdapter.class.getSimpleName();

    private static final String VERY_HIGHT_NUMBER_PATTERN_STRING = "%7.3f";
    private static final String HIGHT_NUMBER_PATTERN_STRING = "%5.3f";
    private static final String TMA_NUMBER_PATTERN_STRING = "%4.4f";
    private static final String THOUTH_NUMBER_PATTERN_STRING = "%3.5f";
    private static final String DEC_NUMBER_PATTERN_STRING = "%2.6f";
    private static final String LOW_NUMBER_PATTERN_STRING = "%1.7f";

    private List<CryptoCurrency> mCryptoCurrencies;

    public CurrencyAdapter(List<CryptoCurrency> myDataSet) {
        mCryptoCurrencies = myDataSet;
    }

    @Override
    public CurrencyAdapter.CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new CurrencyHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CurrencyHolder holder = (CurrencyHolder) viewHolder;
        holder.CurrencyNameTextView.setText(mCryptoCurrencies.get(position).getName());
        String valueString = getFormattedValueString(mCryptoCurrencies.get(position).getValue());
        holder.CurrencyValueTextView.setText(valueString);
    }

    private String getFormattedValueString(double value) {
        if (value < 10) {
            return String.format(LOW_NUMBER_PATTERN_STRING, value);
        } else if (value < 100) {
            return String.format(DEC_NUMBER_PATTERN_STRING, value);
        } else if (value < 1000) {
            return String.format(THOUTH_NUMBER_PATTERN_STRING, value);
        } else if (value < 10000) {
            return String.format(TMA_NUMBER_PATTERN_STRING, value);
        } else if (value < 100000) {
            return String.format(VERY_HIGHT_NUMBER_PATTERN_STRING, value);
        } else {
            return String.format(HIGHT_NUMBER_PATTERN_STRING, value);
        }
    }

    @Override
    public int getItemCount() {
        return mCryptoCurrencies.size();
    }

    public void addCurrencies(List<CryptoCurrency> currencies) {
        mCryptoCurrencies = currencies;
        notifyDataSetChanged();
    }

    public static class CurrencyHolder extends RecyclerView.ViewHolder {

        public TextView CurrencyNameTextView;
        public TextView CurrencyValueTextView;

        public CurrencyHolder(View root) {
            super(root);
            CurrencyNameTextView = root.findViewById(R.id.currency_name);
            CurrencyValueTextView = root.findViewById(R.id.currency_value);
        }
    }

}