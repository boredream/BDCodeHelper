package com.boredream.bdcodehelper.function;

import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.BaseHttpRequest;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.net.SimpleSubscriber;
import com.boredream.bdcodehelper.utils.StringUtils;

import io.reactivex.Observable;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            view.showTip("用户名不能为空");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            view.showTip("密码不能为空");
            return;
        }

        Observable<User> observable = BaseHttpRequest.login(username, password);
        ObservableDecorator.decorate(observable).subscribe(new SimpleSubscriber<User>(view) {
            @Override
            public void onNext(User user) {
                if (!view.isActive()) {
                    return;
                }

                view.loginSuccess(user);
            }
        });
    }

}
