package com.globallogic.currencyviewer.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by oleh.kotetskyi on 3/3/2018.
 */

public class SimpleObserver implements Observer {

    private Disposable mDisposable;

    public SimpleObserver (Disposable disposable){
        mDisposable = disposable;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Object o) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
