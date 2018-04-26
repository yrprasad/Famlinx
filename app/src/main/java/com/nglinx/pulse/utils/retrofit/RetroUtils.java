package com.nglinx.pulse.utils.retrofit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;

import com.nglinx.pulse.activity.AuthenticateIntentService;
import com.nglinx.pulse.constants.ApplicationConstants;
import com.nglinx.pulse.session.DataSession;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Header;
import retrofit.client.OkClient;

public class RetroUtils {
    private static boolean sslOverride;
    public static final String URL_HIT = ApplicationConstants.SERVER_URL;

    public static ApiEndpointInterface getApiEndPoint(final Context context)
    {
        return getHostAdapterForAuthenticate(context).create(ApiEndpointInterface.class);
    }

    public static RestAdapter getHostAdapterForAuthenticate(final Context context) {
        return getHostAdapterForAuthenticate(context, URL_HIT);
    }

    public static RestAdapter getHostAdapterForAuthenticate(final Context context, String baseHost) {

        class LoggingInterceptor implements Interceptor {

            @Override
            public Response intercept(final Interceptor.Chain chain) throws IOException {
                final Request initialRequest = chain.request();

                Request modifiedRequest = chain.request();
                if ((DataSession.getInstance().getjSessionId() != null)
                        && (DataSession.getInstance().getRememberMeId() != null)
                        && (DataSession.getInstance().getjSessionId().trim().length() != 0)
                        && (DataSession.getInstance().getRememberMeId().trim().length() != 0)) {
                    modifiedRequest = modifiedRequest.newBuilder()
                            .addHeader("Cookie", ApplicationConstants.JSESSION_ID_HEADER + "=" +
                                    DataSession.getInstance().getjSessionId() + ";" + ApplicationConstants.REMEMBER_ME_HEADER + "=" +
                                    DataSession.getInstance().getRememberMeId())
                            .build();
                }
                Response response1 = chain.proceed(modifiedRequest);
                boolean unauthorized = response1.code() == 401;
                if (!unauthorized)
                    return response1;

                return handleAuthError(context, initialRequest, chain);
            }
        }

        if (sslOverride) {
            OkHttpClient client = getUnsafeOkHttpClient();
            client.interceptors().add(new LoggingInterceptor());
            RestAdapter restAdapter = new RestAdapter.Builder().setClient(new OkClient(client)).setEndpoint(baseHost).build();
            return restAdapter;
        } else {
            OkHttpClient client = new OkHttpClient();
            client.interceptors().add(new LoggingInterceptor());
            RestAdapter restAdapter = new RestAdapter.Builder().setClient(new OkClient(client)).setEndpoint(baseHost).setLogLevel(RestAdapter.LogLevel.FULL).build();
            return restAdapter;
        }
    }


    private static Response handleAuthError(final Context context, Request request, Interceptor.Chain chain) throws IOException {
        class ResponseTmp {

            Response rsp;
            int status;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public Response getRsp() {
                return rsp;
            }

            public void setRsp(Response rsp) {
                this.rsp = rsp;
            }
        }

        final ResponseTmp responseTmp = new ResponseTmp();
        responseTmp.setStatus(ApplicationConstants.STATUS_RUNNING);

        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, AuthenticateIntentService.class);
        intent.putExtra("receiver", new ResultReceiver(new Handler(Looper.getMainLooper())) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle bundle) {
                switch (resultCode) {
                    case ApplicationConstants.STATUS_FINISHED:
                        responseTmp.setStatus(ApplicationConstants.STATUS_FINISHED);
                        break;
                    case ApplicationConstants.STATUS_ERROR:
                        responseTmp.setStatus(ApplicationConstants.STATUS_ERROR);
                }
            }
        });
        context.startService(intent);

        while (responseTmp.getStatus() == ApplicationConstants.STATUS_RUNNING) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }


        if (responseTmp.getStatus() == ApplicationConstants.STATUS_FINISHED) {
            Request initialRequest1 = request.newBuilder()
                    .addHeader("Cookie", ApplicationConstants.JSESSION_ID_HEADER + "=" +
                            DataSession.getInstance().getjSessionId() + ";" + ApplicationConstants.REMEMBER_ME_HEADER + "=" +
                            DataSession.getInstance().getRememberMeId())
                    .build();
            Response rsp = chain.proceed(initialRequest1);
            responseTmp.setRsp(rsp);
            return responseTmp.getRsp();
        } else {
            //Failed to perform reAuth
            throw new IOException("Auth Failure");
        }
    }


    public static RestAdapter getHostAdapterForAuthenticate(String baseHost) {

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade arg0) {
            }
        };

        if (sslOverride) {
            RestAdapter restAdapter = new RestAdapter.Builder().setClient(new OkClient(getUnsafeOkHttpClient())).setEndpoint(baseHost).setRequestInterceptor(requestInterceptor).build();
            return restAdapter;
        } else {
            RestAdapter restAdapter = new RestAdapter.Builder().setClient(new OkClient(new OkHttpClient())).setEndpoint(baseHost).setRequestInterceptor(requestInterceptor).setLogLevel(RestAdapter.LogLevel.FULL).build();
            return restAdapter;
        }
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Header getHeader(List<Header> headers, String headerStr) {
        Header header = null;

        for (Header tmp : headers) {
            if (tmp.getName().equalsIgnoreCase(ApplicationConstants.COOKIE_HEADER)) {
                if (tmp.getValue().contains(headerStr)) {
                    header = tmp;
                    break;
                }
            }
        }

        return header;
    }

    private static String getCookieValue(Header header, String cookieKey) {
        String[] cookies = header.getValue().split(ApplicationConstants.RESPONSE_HEADER_SEPARATOR);

        if (cookies.length == 0)
            return null;

        for (String cookie :
                cookies) {
            if (cookie.contains(cookieKey)) {
                String[] tmp = cookie.split(ApplicationConstants.COOKIE_KEY_VALUE_SEPARATOR);
                if (tmp.length != 2) {
                    return null;
                }
                return tmp[1];
            }
        }

        return null;
    }


    public static String getJSessionId(List<Header> headers) {

        Header jSessHeader = getHeader(headers, ApplicationConstants.JSESSION_ID_HEADER);

        if (jSessHeader == null) {
            return null;
        }

        return getCookieValue(jSessHeader, ApplicationConstants.JSESSION_ID_HEADER);
    }


    public static String getRememberMe(List<Header> headers) {

        Header rememberMeHeader = getHeader(headers, ApplicationConstants.REMEMBER_ME_HEADER);

        if (rememberMeHeader == null) {
            return null;
        }

        return getCookieValue(rememberMeHeader, ApplicationConstants.REMEMBER_ME_HEADER);
    }
}