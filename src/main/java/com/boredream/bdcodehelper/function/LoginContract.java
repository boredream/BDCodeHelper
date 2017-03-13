package com.boredream.bdcodehelper.function;


import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.entity.IUser;

public interface LoginContract {

    interface View extends BaseView {

        void loginSuccess(IUser user);

    }

    interface Presenter extends BasePresenter {

        void login(String username, String password);

    }
}
