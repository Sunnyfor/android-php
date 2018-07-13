package com.cocosh.shmstore.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.cocosh.shmstore.R;
import com.cocosh.shmstore.widget.photoview.PhotoView;


import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.Glide.with;

/**
 * Created by asus on 2017/1/5.
 */
public class GlideUtils {

    private static final String HEAD = "head";
    private static final String FULLSCREEN = "full_screen";
    private static final String LONG = "long";
    private static final String CONTENT = "content";
    private static final String NODEFAULT = "nodefault";
    private static final String RECT = "rect";

    public static void load(Context context, String url, ImageView target) {
        String targetUrl = wrapUrl(url);
        loadByDefault(context, targetUrl, target, CONTENT, ImageView.ScaleType.CENTER_CROP);
    }

    public static void loadRound(int type, Context context, String url, ImageView target) {
        String targetUrl = wrapUrl(url);
        String typeDefault = "";
        if (type == 1) {
            typeDefault = "D";
        }
        loadByDefault(context, targetUrl, target, typeDefault, ImageView.ScaleType.CENTER_CROP);
    }

    public static void loadRect(Context context, String url, ImageView target) {
        String targetUrl = wrapUrl(url);
        loadByDefault(context, targetUrl, target, "", ImageView.ScaleType.CENTER_CROP);
    }


    public static void noCacheLoad(Context context, String url, ImageView target) {
        Glide.with(context).load(url).dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(target);
    }

    public static void loadHead(Context context, String url, ImageView target) {
        String targetUrl = wrapUrl(url);
        loadByDefault(context, targetUrl, target, HEAD, ImageView.ScaleType.CENTER_CROP);
    }

    public static void loadHead(Context context, String url, ImageView target, ImageView.ScaleType scaleType) {
        String targetUrl = wrapUrl(url);
        loadByDefault(context, targetUrl, target, HEAD, scaleType);
    }

    public static void loadFullScreen(Context context, String url, ImageView target) {
        String targetUrl = wrapUrl(url);
        loadByDefault(context, targetUrl, target, FULLSCREEN, ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * 加载原始宽高比 imageView须固定宽度
     *
     * @param context
     * @param url
     * @param target
     */
    public static void loadDefault(final Context context, String url, final ImageView target) {
        String targetUrl = wrapUrl(url);
        Glide.with(context).load(targetUrl).asBitmap().dontAnimate().placeholder(R.drawable.default_content).error(R.drawable.default_content).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int width = target.getWidth();//固定宽度
                //宽度固定,然后根据原始宽高比得到此固定宽度需要的高度
                int height = width * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = target.getLayoutParams();
                para.height = height;
                para.width = width;
                target.setImageBitmap(resource);
            }
        });
    }


    public static void loadLong(Context context, String url, ImageView target) {
        String targetUrl = wrapUrl(url);
        loadByDefault(context, targetUrl, target, LONG, ImageView.ScaleType.CENTER_CROP);
    }

    public static void loadAuthor(Context context, String url, ImageView target) {
        String targetUrl = wrapUrl(url);
        loadByDefault(context, targetUrl, target, NODEFAULT, ImageView.ScaleType.FIT_XY);
    }

    private static void loadByDefault(Context context, String targetUrl, ImageView target, String type, ImageView.ScaleType scaleType) {
        int resId;
        switch (type) {
            case HEAD:
                resId = R.drawable.default_head;
                break;

            case CONTENT:
                resId = R.drawable.default_long;
                break;

            case FULLSCREEN:
                resId = R.drawable.default_full;
                break;
            case "D":
                resId = R.drawable.loadlogo;
                break;

            case NODEFAULT:
                resId = -1;
                break;
            case RECT:
            default:
                resId = R.drawable.default_content;
                break;
        }

        DrawableRequestBuilder builder = Glide.with(context)
                .load(targetUrl)
                .placeholder(resId)
                .dontAnimate()
                .error(resId);

        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            builder.centerCrop();
        } else {
            builder.fitCenter();
        }

        if (type.equals(HEAD)) {
            builder.bitmapTransform(new RoundedCornersTransformation(context, DensityUtil.dip2px(context, 6f), 0));
        }


        builder.into(target);
    }

    public static void loadFullSize(Context context, String url, final ImageView iv, final ImageLoaderCallback callback) {
        String targetUrl = wrapUrl(url);

        with(context)
                .load(targetUrl)
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        if (callback != null) {
                            callback.onFail(e);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (resource != null && callback != null) {
                            iv.setImageBitmap(resource);
                            callback.onSuccess();
                            return true;
                        }
                        return false;
                    }
                })
                .into(iv);
    }

    public static void loadDefaultSize(Context context, String url, final PhotoView imageView) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
//                        if (imageView.getScaleType() != ImageView.ScaleType.CENTER_INSIDE) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                        }
//                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
//                        params.width = 1000;
//                        int vw = imageView.getWidth();
//                        float scale = (float) 1000 / (float) resource.getIntrinsicWidth();
//                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
//                        params.height = vh;
//                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .into(imageView);
    }

    private static String wrapUrl(String url) {

        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        if (url.contains("/data/")) {
            return url;
        } else if (url.contains(".com") && !url.contains("http://")) {
            return String.format("%s%s", "http://", url);
        }
//        else if(!url.contains("/data/") && !url.contains("http://")){
//            return String.format("%s%s" , ApiManager.getImageUrl() , url);
//        }
        return url.replace(" ", "%20");
    }


    public interface ImageLoaderCallback {

        /**
         * Successfully handle a task;
         */
        void onSuccess();

        /**
         * Error happened when running a task;
         */
        void onFail(Throwable t);
    }


    public static void load(Context context, String url, final ImageView iv, final ImageLoaderCallback callback) {
        String targetUrl = wrapUrl(url);

        with(context)
                .load(targetUrl)
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        if (callback != null) {
                            callback.onFail(e);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (resource != null && callback != null) {
                            iv.setImageBitmap(resource);
                            callback.onSuccess();
                            return true;
                        }
                        return false;
                    }
                })
                .into(iv);
    }

    public static void loadPhoto(Context context, String url, ImageView imageView, int res) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(res)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    public static void noCacheload(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    public static void loadBitmap(Context mContext, String url, SimpleTarget<byte[]> tag) {
        Glide.with(mContext).load(url).asBitmap().toBytes().into(tag);
    }
}
