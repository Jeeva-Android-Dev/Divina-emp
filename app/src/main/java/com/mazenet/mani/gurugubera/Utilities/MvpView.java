package com.mazenet.mani.gurugubera.Utilities;



public interface MvpView {

    void showProgressDialog();

    void hideProgressDialog();

    void showDialogWithError(int errorCode, String error);

    void showNetWorkError();


}
