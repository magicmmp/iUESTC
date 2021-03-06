package com.febers.iuestc.module.login.view;

import android.webkit.WebView;

import com.febers.iuestc.R;
import com.febers.iuestc.base.BaseCode;
import com.febers.iuestc.base.BaseEvent;
import com.febers.iuestc.base.BaseSwipeActivity;
import com.febers.iuestc.base.Constants;
import com.febers.iuestc.entity.BeanUserStatus;
import com.febers.iuestc.module.login.presenter.LoginContract;
import com.febers.iuestc.module.login.presenter.LoginJSInterface;
import com.febers.iuestc.net.WebViewConfigure;
import com.febers.iuestc.util.SPUtil;
import com.febers.iuestc.util.WebViewUtil;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends BaseSwipeActivity implements LoginContract.View {

    private static final String TAG = "LoginActivity";
    private WebView webView;

    @Override
    protected int setView() {
        return R.layout.activity_login;
    }

    @Override
    protected int setToolbar() {
        return R.id.tb_login;
    }

    @Override
    protected String setToolbarTitle() {
        return "登录";
    }

    @Override
    protected void initView() {
        webView = findViewById(R.id.web_login);
        dataRequest(true);
    }

    @Override
    public void dataRequest(Boolean isRefresh) {
        LoginJSInterface loginJSInterface = new LoginJSInterface(this);
        new WebViewConfigure.Builder(this, webView)
                .setOpenUrlOut(false)
                .setProcessHtml(true, loginJSInterface, "HTMLOUT")
                .builder();
        webView.loadUrl("http://portal.uestc.edu.cn");
    }

    @Override
    public void loadIdAndPwFunc(BaseEvent<String> event) {
        runOnUiThread( () -> {
            webView.loadUrl(event.getDate());
            webView.loadUrl("javascript:fun();");
        });
    }

    @Override
    public void loginResult(BaseEvent<String> event) {
        if (event.getCode() == BaseCode.UPDATE) {
            SPUtil.INSTANCE().put(Constants.IS_LOGIN, true);
//            Intent intent = new Intent();
//            intent.putExtra("status", true);
//            this.setResult(BaseCode.STATUS, intent);
            EventBus.getDefault().post(new BaseEvent<>(BaseCode.UPDATE, new BeanUserStatus(BaseCode.UPDATE)));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebViewUtil.destroyWebView(webView);
    }
}
