package com.boredream.bdcodehelper.net;

import io.reactivex.Observable;

public abstract class MultiPageRequest<T> {

    public abstract Observable<T> request(int page);

}
