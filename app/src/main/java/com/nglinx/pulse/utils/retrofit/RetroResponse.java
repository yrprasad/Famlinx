package com.nglinx.pulse.utils.retrofit;

import android.content.Context;

import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.models.ResponseDto;

import java.util.HashSet;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class RetroResponse<T> implements Callback<ResponseDto<T>> {

    protected String errorMsg = null;

    protected T model = null;

    protected HashSet<T> models = null;

    protected Response response = null;

    private Context context;

    public RetroResponse(Context context) {
        errorMsg = null;
        model = null;
        this.context=context;
    }

    public RetroResponse() {
        errorMsg = null;
        model = null;
    }

    @Override
    public void success(ResponseDto<T> rspBean, Response response) {
        if (rspBean.getMessage().equalsIgnoreCase(ApplicationConstants.REST_RESPONSE_SUCCESS)) {
            model = (T) rspBean.getModel();
            models = (HashSet<T>) rspBean.getModels();
            this.response = response;
            onSuccess();
        } else {
            errorMsg = rspBean.getMessage();
            if ((errorMsg == null) || (errorMsg.trim().isEmpty())) {
                errorMsg = "Server Error";
            };
            onFailure();
        }
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        ResponseDto body = null;

        try {
            if (retrofitError.getResponse() != null) {
                body = (ResponseDto) retrofitError.getBodyAs(ResponseDto.class);
            }
        } catch (Exception e) {
            System.out.println("Unexpected Exception");
        }

        if (body != null)
            errorMsg = body.getMessage();

        if ((errorMsg == null) || (errorMsg.trim().isEmpty())) {
            errorMsg = "Server Error";
        }

        onFailure();
    }

    public abstract void onSuccess();

    public abstract void onFailure();
}
