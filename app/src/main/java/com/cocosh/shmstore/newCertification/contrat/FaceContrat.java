package com.cocosh.shmstore.newCertification.contrat;

import com.cocosh.shmstore.base.BaseModel;
import com.cocosh.shmstore.base.IBasePresenter;
import com.cocosh.shmstore.base.IBaseView;

/**
 * Created by wt on 2018/2/8.
 */

public interface FaceContrat {
    interface IView extends IBaseView {
        void faceResult(BaseModel result);

    }

    interface IPresenter extends IBasePresenter{
        void faceRequest();

    }
}
