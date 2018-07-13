package com.cocosh.shmstore.widget.photoview;

import android.annotation.TargetApi;
import android.content.Context;

/**
 * Created by lmg on 2017/10/26.
 */

@TargetApi(14)
public class IcsScroller extends GingerScroller {

    public IcsScroller(Context context) {
        super(context);
    }

    @Override
    public boolean computeScrollOffset() {
        return mScroller.computeScrollOffset();
    }

}

