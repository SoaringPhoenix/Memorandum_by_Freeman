package com.example.memorandum.util;

import android.app.Activity;
import android.content.Intent;


/**
 * Created by huangfangyi on 2017/6/23.
 * qq 84543217
 */

public interface RegisterContract {
    public     int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public     int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public     int PHOTO_REQUEST_CUT = 3;// 结果
    public interface View extends BaseView<Presenter>{
        void showAvatar(String imagePath);
        void showDialog();
        void cancelDialog();
        void showPassword();
        void hidePassword();
        void enableButton();
        void disableButton();
        void showToast(Object msgRes);
        String getOriginImagePath();
        Activity getBaseActivity();
        void showSmsCode(String code);

        void clearCacheCode();

        void startTimeDown();

        void finishTimeDown();
    }

    public interface Presenter extends BasePresenter{

        void registerInServer(String nickName, String mobile, String password);
         void result(int requsetCode, int resultCode, Intent intent);
        void sendSmsCode(String mobile);
    }

}
