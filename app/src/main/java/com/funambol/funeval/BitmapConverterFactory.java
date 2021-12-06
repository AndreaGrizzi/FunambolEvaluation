package com.funambol.funeval;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class BitmapConverterFactory extends Converter.Factory {

    public static BitmapConverterFactory create() {
        return new BitmapConverterFactory();
    }

    private BitmapConverterFactory() {
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        if (type != Bitmap.class) return null;
        return (Converter<ResponseBody, Object>) value -> BitmapFactory.decodeStream(value.byteStream());
    }

}