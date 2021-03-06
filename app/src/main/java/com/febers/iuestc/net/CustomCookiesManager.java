package com.febers.iuestc.net;

import android.webkit.CookieManager;

import com.febers.iuestc.MyApp;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CustomCookiesManager implements CookieJar {

    private static final String TAG = "CustomCookiesManager";
    private final PersistentCookieStore cookieStore = new PersistentCookieStore(MyApp.getContext());
    private CookieManager cookieManager = CookieManager.getInstance();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
                cookieManager.setCookie(url.toString(), item.toString());
//                String cookieStr = item.name() + item.value()+";domain="+item.domain();
//                cookieManager.setCookie(url.toString(), cookieStr); //为webview添加
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        String cookiesStr = cookieManager.getCookie(url.toString());
        if (cookiesStr!=null && !cookiesStr.isEmpty()) {
            String[] cookieHeaders = cookiesStr.split(";");
            cookies = new ArrayList<>(cookieHeaders.length);
            for (String header : cookieHeaders) {
                cookies.add(Cookie.parse(url, header));
            }
        }
        return cookies;
    }
}
