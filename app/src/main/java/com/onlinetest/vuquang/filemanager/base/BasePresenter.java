package com.onlinetest.vuquang.filemanager.base;

import com.onlinetest.vuquang.filemanager.data.manager.AppDataManager;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private final AppDataManager mDataManager;

    private V mMvpView;

    public BasePresenter(AppDataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public AppDataManager getDataManager() {
        return mDataManager;
    }

//    @Override
//    public void setUserAsLoggedOut() {
//        getDataManager().setAccessToken(null);
//    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
