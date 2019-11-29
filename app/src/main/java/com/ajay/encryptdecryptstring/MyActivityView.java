package com.ajay.encryptdecryptstring;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;


public interface MyActivityView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError(int resId);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setEncryptedString(String encryptedString);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDecryptedString(String decryptedString);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void clear();
}
