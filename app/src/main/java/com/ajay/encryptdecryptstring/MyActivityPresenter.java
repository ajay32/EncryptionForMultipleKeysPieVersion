package com.ajay.encryptdecryptstring;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


@InjectViewState
public class MyActivityPresenter extends MvpPresenter<MyActivityView> {




    private String encryptedString;
    private String password;
    private CompositeDisposable compositeDisp;


    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        compositeDisp = new CompositeDisposable();
    }


   public void encryptedData(final String text, final String password ,final  Context cotext ,final String key) {
       compositeDisp = new CompositeDisposable();
        if(TextUtils.isEmpty(text) || TextUtils.isEmpty(password)) {
            getViewState().showError(R.string.error_encrypted);
        } else {
            this.password = password;
            compositeDisp.add(
                    Single.just(Crypto.encrypt(text, password))
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<String>() {
                                @Override
                                public void onSuccess(@NonNull String s) {
                                    encryptedString = s;
                                    getViewState().setEncryptedString(encryptedString);
                                    Log.d("ajaymehtass","encerypted  "+ encryptedString);
                                    SharedPreferences pref = cotext.getSharedPreferences("MyPref", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(key, encryptedString); // Storing string
                                    editor.commit(); // commit changes

                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    e.printStackTrace();
                                }
                            })

            );

        }

    }

    public String getEncryptedString() {
        return encryptedString;
    }


   public void decryptedData(final String password , String encryptedString ,final String key ) {
        if(TextUtils.isEmpty(encryptedString) || TextUtils.isEmpty(password)) {
            getViewState().showError(R.string.error_decrypted);
        } else if(!this.password.equals(password)) {
            getViewState().showError(R.string.error_password);
        } else {
            compositeDisp.add(
                    Single.just(Crypto.decrypt(encryptedString, password))
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<String>() {
                                @Override
                                public void onSuccess(@NonNull String s) {
                                    getViewState().setDecryptedString(s);
                                    Log.d("ajaymehtass","decrypted  "+ s + " :"+key);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    e.printStackTrace();
                                }
                            })
            );
        }
    }


    void clear() {
        encryptedString = null;
        password        = null;
        compositeDisp.clear();
        getViewState().clear();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisp.dispose();
    }


}
