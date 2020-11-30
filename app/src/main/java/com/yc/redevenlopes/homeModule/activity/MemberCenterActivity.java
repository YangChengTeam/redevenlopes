package com.yc.redevenlopes.homeModule.activity;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yc.redevenlopes.R;
import com.yc.redevenlopes.base.BaseActivity;
import com.yc.redevenlopes.homeModule.contact.MemberCenterContact;
import com.yc.redevenlopes.homeModule.fragment.ShareFragment;
import com.yc.redevenlopes.homeModule.module.bean.OtherBeans;
import com.yc.redevenlopes.homeModule.module.bean.UserInfo;
import com.yc.redevenlopes.homeModule.present.MemberCenterPresenter;
import com.yc.redevenlopes.homeModule.widget.MemberCenterView;
import com.yc.redevenlopes.utils.CacheDataUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 会员中心
 */
public class MemberCenterActivity extends BaseActivity<MemberCenterPresenter> implements MemberCenterContact.View {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.memberCenterView_wallet)
    MemberCenterView memberCenterViewWallet;
    @BindView(R.id.memberCenterView_group)
    MemberCenterView memberCenterViewGroup;
    @BindView(R.id.memberCenterView_person)
    MemberCenterView memberCenterViewPerson;
    @BindView(R.id.memberCenterView_rank)
    MemberCenterView memberCenterViewRank;
    @BindView(R.id.memberCenterView_sound)
    MemberCenterView memberCenterViewSound;
    @BindView(R.id.memberCenterView_version)
    MemberCenterView memberCenterViewVersion;
    @BindView(R.id.tv_userid)
    TextView tvUserid;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_member_center;
    }

    @Override
    public void initEventAndData() {
        money = getIntent().getStringExtra("money");
        if (!TextUtils.isEmpty(money)){
            memberCenterViewWallet.setContent("￥"+money);
        }
        memberCenterViewPerson.setContent("400人");
        memberCenterViewGroup.setContent(CacheDataUtils.getInstance().getUserInfo().getGroup_id()+"");
        mPresenter.getOtherInfo(CacheDataUtils.getInstance().getUserInfo().getGroup_id()+"",CacheDataUtils.getInstance().getUserInfo().getId()+"");
        initData();
    }

    private void initData() {
        UserInfo userInfo = CacheDataUtils.getInstance().getUserInfo();

        Glide.with(this).load(userInfo.getFace()).apply(RequestOptions.bitmapTransform(new RoundedCorners(8)))
                .error(R.mipmap.icon_default_image).into(ivAvatar);
        String nickname = userInfo.getNickname();
        if (TextUtils.isEmpty(nickname)) {
            nickname = "游客" + userInfo.getId();
        }
        tvNickname.setText(nickname);
        tvUserid.setText(String.format(getString(R.string.user_id), userInfo.getId()));

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            memberCenterViewVersion.setContent("V" + packageInfo.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    public static void memberCenterJump(Context context,String money) {
        Intent intent = new Intent(context, MemberCenterActivity.class);
        intent.putExtra("money",money);
        context.startActivity(intent);
    }


    @OnClick({R.id.memberCenterView_wallet, R.id.memberCenterView_rank, R.id.tv_share_friend, R.id.tv_logout})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.memberCenterView_wallet:
                startActivity(new Intent(MemberCenterActivity.this, WalletDetailActivity.class));
                break;
            case R.id.memberCenterView_rank:
                startActivity(new Intent(MemberCenterActivity.this, AllLeaderBoarderActivity.class));
                break;
            case R.id.tv_share_friend:
                ShareFragment shareFragment = new ShareFragment();
                shareFragment.setShareOnclickListen(new ShareFragment.ShareOnclickListen() {
                    @Override
                    public void weixinShare() {
                        startShare(SHARE_MEDIA.WEIXIN);
                    }

                    @Override
                    public void weixinCircleShare() {
                        startShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                    }
                });
                shareFragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.tv_logout:
                ActivityOptions opts = ActivityOptions.makeCustomAnimation(MemberCenterActivity.this,
                        R.anim.abc_fade_in, R.anim.abc_fade_out);

                Intent intent = new Intent(MemberCenterActivity.this, LoginActivity.class);
                startActivity(intent, opts.toBundle());
                finish();

                break;
        }
    }

    private ShareAction mShareAction;
    private UMImage mUMImage;
    private MyUMShareListener myUMShareListener;

    private void startShare(SHARE_MEDIA share_media) {
        UMWeb mUMWeb = new UMWeb("http://m.hncj.com/sjrj/34282.html");
        mUMWeb.setTitle("无线抢红包");
        mUMWeb.setDescription("无线抢红包");
        mUMImage = new UMImage(getApplicationContext(), R.mipmap.redlogo);
        mUMWeb.setThumb(mUMImage);
        mShareAction = new ShareAction(this);
        myUMShareListener = new MyUMShareListener();
        mShareAction.withMedia(mUMWeb);
        mShareAction.setPlatform(share_media);
        mShareAction.setCallback(myUMShareListener);
        mShareAction.share(); //share不带UM的面板  open带面板 如果带面板 就要setDisplayList
    }

    @Override
    protected void onDestroy() {
        if (mShareAction != null) {
            mShareAction = null;
        }
        if (mUMImage != null) {
            mUMImage = null;
        }
        if (myUMShareListener != null) {
            myUMShareListener = null;
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getOtherInfoSuccess(OtherBeans data) {
        memberCenterViewWallet.setContent("￥"+data.getCash());
    }

    public class MyUMShareListener implements UMShareListener {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    }

}