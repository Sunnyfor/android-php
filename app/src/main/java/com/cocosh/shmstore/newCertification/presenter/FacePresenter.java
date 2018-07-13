package com.cocosh.shmstore.newCertification.presenter;

import com.cocosh.shmstore.base.BaseActivity;
import com.cocosh.shmstore.newCertification.contrat.FaceContrat;
import com.cocosh.shmstore.newCertification.data.FaceLoader;

/**
 * Created by wt on 2018/2/8.
 */

public class FacePresenter implements FaceContrat.IPresenter {

    private final FaceLoader loader;

    public FacePresenter(BaseActivity activity, FaceContrat.IView faceView) {
        loader = new FaceLoader(activity, faceView);
    }

    @Override
    public void start() {

    }

    @Override
    public void faceRequest() {
        loader.requestFace();
    }

}
