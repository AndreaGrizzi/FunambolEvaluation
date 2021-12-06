package com.funambol.funeval;

import android.graphics.Bitmap;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageService {


    @GET("{imgUrl}")
    Observable<Bitmap> getBitmap(@Path("imgUrl") String imgUrl);

}
