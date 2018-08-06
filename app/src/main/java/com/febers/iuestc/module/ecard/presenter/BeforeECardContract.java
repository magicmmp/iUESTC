/*
 * Created by Febers 2018.
 * Copyright (c). All rights reserved.
 *
 * Last Modified 18-6-17 下午2:22
 *
 */

package com.febers.iuestc.module.ecard.presenter;

import com.febers.iuestc.base.BasePresenter;
import com.febers.iuestc.base.BaseView;
import com.febers.iuestc.entity.BeanECardPayRecord;

import java.util.List;

public interface BeforeECardContract {

    interface View extends BaseView {
        void showLoginResult(String msg);
        void showECardBalance(String balance);
        void showElecBalance(String balance);
        void showPayRecord(List<BeanECardPayRecord.data.consumes> consumesList);
    }

    abstract class Presenter extends BasePresenter<BeforeECardContract.View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loginECardRequest(String phone, String pw);
        public abstract void loginResult(String result);
        public abstract void balanceRequest();
        public abstract void eCardBalanceResult(String result);
        public abstract void elecBalanceResult(String result);
        public abstract void recordResult(List<BeanECardPayRecord.data.consumes> consumesList);
        public abstract void localDataRequest(int recordSize);
    }
}