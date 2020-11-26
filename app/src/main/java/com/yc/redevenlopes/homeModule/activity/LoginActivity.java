package com.yc.redevenlopes.homeModule.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lq.lianjibusiness.base_libary.ui.base.SimpleActivity;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.redevenlopes.R;
import com.yc.redevenlopes.application.MyApplication;
import com.yc.redevenlopes.base.BaseActivity;
import com.yc.redevenlopes.homeModule.contact.LoginContract;
import com.yc.redevenlopes.homeModule.fragment.UserPolicyFragment;
import com.yc.redevenlopes.homeModule.module.bean.UserAccreditInfo;
import com.yc.redevenlopes.homeModule.present.LoginPresenter;
import com.yc.redevenlopes.listener.ThirdLoginListener;
import com.yc.redevenlopes.service.event.Event;
import com.yc.redevenlopes.utils.UserLoginManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by suns  on 2020/11/19 08:44.
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.tv_tourist_login)
    TextView tvTouristLogin;
    @BindView(R.id.ll_qq_login)
    LinearLayout llQqLogin;
    @BindView(R.id.ll_wx_login)
    LinearLayout llWxLogin;
    private UserLoginManager userLoginManager;
    private int appType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isNeedNewTitle(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initEventAndData() {
        tvTouristLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        userLoginManager = UserLoginManager.getInstance();
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }


    @OnClick({R.id.tv_user_policy, R.id.ll_wx_login, R.id.ll_qq_login, R.id.tv_tourist_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_user_policy:
                UserPolicyFragment userPolicyFragment = new UserPolicyFragment();
                userPolicyFragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.ll_wx_login:
                appType = 2;
                userLoginManager.setCallBack(LoginActivity.this, loginListener).oauthAndLogin(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_qq_login:
                appType = 3;
                userLoginManager.setCallBack(LoginActivity.this, loginListener).oauthAndLogin(SHARE_MEDIA.QQ);
                break;
            case R.id.tv_tourist_login:
                appType = 1;
                login(null, null, null, null, 2, null);
                break;
        }
    }

    private ThirdLoginListener loginListener = new ThirdLoginListener() {
        @Override
        public void onLoginResult(UserAccreditInfo userDataInfo) {
            appType = 2;
            login(userDataInfo.openid, null, null, userDataInfo.nickname, 2, userDataInfo.face);
        }
    };

    private void login(String wx_openid, String qq_openid,
                       String age, String nickname, int sex, String face) {
        Log.d("ccc", "---------login: ");
        mPresenter.login(appType, wx_openid, qq_openid, age, nickname, sex, face, ((MyApplication) MyApplication.getInstance()).getAgentId());
    }

    @Override
    public void showLoginSuccess() {
        EventBus.getDefault().post(new Event.LoginEvent());
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // QQ授权回调需要配置这里
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}
