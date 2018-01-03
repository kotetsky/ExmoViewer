package com.globallogic.poloniexviewer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by oleh.kotetskyi on 1/3/2018.
 */

public class CurrencyAdapter extends RecyclerView.Adapter {

    private List<CryptoCurrency> mCurrencies;

    public CurrencyAdapter(List<CryptoCurrency> myDataset) {
        mCurrencies = myDataset;
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
        holder.CurrencyNameTextView.setText(mCurrencies.get(position).getName());
        holder.CurrencyNameTextView.setText(String.valueOf(mCurrencies.get(position).getValue()));
    }

    @Override
    public int getItemCount() {
        return mCurrencies.size();
    }

    public static class CurrencyHolder extends RecyclerView.ViewHolder {

        public TextView CurrencyNameTextView;
        public TextView CurrencyValueTextView;

        public CurrencyHolder(View root) {
            super(root);
            CurrencyNameTextView = root.findViewById(R.id.name);
            CurrencyValueTextView = root.findViewById(R.id.value);
        }
    }

}
