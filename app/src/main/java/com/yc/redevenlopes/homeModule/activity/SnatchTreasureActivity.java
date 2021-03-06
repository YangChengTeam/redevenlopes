package com.yc.redevenlopes.homeModule.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.yc.adplatform.AdPlatformSDK;
import com.yc.adplatform.ad.core.AdCallback;
import com.yc.adplatform.ad.core.AdError;
import com.yc.redevenlopes.R;
import com.yc.redevenlopes.base.BaseActivity;
import com.yc.redevenlopes.base.BaseDialogFragment;
import com.yc.redevenlopes.dialog.SnatchDialog;
import com.yc.redevenlopes.homeModule.contact.SnatchTreasureContact;
import com.yc.redevenlopes.homeModule.module.bean.SnatchDetailsBeans;
import com.yc.redevenlopes.homeModule.module.bean.SnatchPostBeans;
import com.yc.redevenlopes.homeModule.present.SnatchTreasurePresenter;
import com.yc.redevenlopes.service.event.Event;
import com.yc.redevenlopes.utils.CacheDataUtils;
import com.yc.redevenlopes.utils.CommonUtils;
import com.yc.redevenlopes.utils.CountDownUtils;
import com.yc.redevenlopes.utils.CountDownUtilsTwo;
import com.yc.redevenlopes.utils.DisplayUtil;
import com.yc.redevenlopes.utils.SoundPoolUtils;
import com.yc.redevenlopes.utils.TimesUtils;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 夺宝
 */
public class SnatchTreasureActivity extends BaseActivity<SnatchTreasurePresenter> implements SnatchTreasureContact.View {

    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.line_ruleDetails)
    LinearLayout lineRuleDetails;
    @BindView(R.id.line_snatchsOne)
    LinearLayout lineSnatchsOne;
    @BindView(R.id.line_snatchsTwo)
    LinearLayout lineSnatchsTwo;
    @BindView(R.id.tv_prizePeriods)
    TextView tvPrizePeriods;
    @BindView(R.id.tv_snatchCurrNums)
    TextView tvSnatchCurrNums;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_firstSantchTimes)
    TextView tvFirstSantchTimes;
    @BindView(R.id.tv_SysCurrTimesTimes)
    TextView tvSysCurrTimesTimes;
    @BindView(R.id.tv_yuOpenPrizeTimes)
    TextView tvYuOpenPrizeTimes;
    @BindView(R.id.tv_mySnatchNums)
    TextView tvMySnatchNums;
    @BindView(R.id.tv_quanNums)
    TextView tvQuanNums;
    @BindView(R.id.tv_lianxuQuanNums)
    TextView tvLianxuQuanNums;
    @BindView(R.id.tv_onceNumsQuan)
    TextView tvOnceNumsQuan;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_snatchsTwoTitle)
    TextView tvSnatchsTwoTitle;
    @BindView(R.id.iv_quan2)
    ImageView ivQuan2;
    @BindView(R.id.tv_snatchsOneTitle)
    TextView tvSnatchsOneTitle;
    @BindView(R.id.iv_quan1)
    ImageView ivQuan1;
    @BindView(R.id.iv_snatch)
    ImageView ivSnatch;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.line_mySnatch)
    LinearLayout lineMySnatch;
    private View nodata_view;
    private SnatchDetailsBeans snatchDetailsBeans;
    private int snatchNums;
    private int infoId;
    private String allSnatchStr;
    private FrameLayout fl_ad_containesss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isNeedNewTitle(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_snatch_treasure;
    }

    @Override
    public void initEventAndData() {
        fl_ad_containesss=findViewById(R.id.fl_ad_containessss);
        nodata_view = findViewById(R.id.view_nodata);
        showExpress();
        mPresenter.getSnatchinfoDetails(CacheDataUtils.getInstance().getUserInfo().getGroup_id() + "");
        progressbar.setMax(1000);
        countDownUtils = new CountDownUtils();
        countDownUtils.setOnCountDownListen(new CountDownUtils.OnCountDownListen() {
            @Override
            public void count(long mHour, long mMin, long mSecond) {
                tvYuOpenPrizeTimes.setText(getTv(mHour) + ":" + getTv(mMin) + ":" + getTv(mSecond));
            }

            @Override
            public void countFinsh() {
                mPresenter.getSnatchinfoDetails(CacheDataUtils.getInstance().getUserInfo().getGroup_id() + "");
            }
        });

        sysCountDownUtils = new CountDownUtilsTwo();
        sysCountDownUtils.setOnCountDownListen(new CountDownUtilsTwo.OnCountDownListen() {
            @Override
            public void count(long mHour, long mMin, long mSecond) {
                tvSysCurrTimesTimes.setText(getTv(mHour) + ":" + getTv(mMin) + ":" + getTv(mSecond));
            }
        });
        initDialogs();
    }


    private void showExpress() {
        loadExpressVideo();
        final AdPlatformSDK adPlatformSDK = AdPlatformSDK.getInstance(this);
        adPlatformSDK.setUserId(CacheDataUtils.getInstance().getUserInfo().getId() + "");
        adPlatformSDK.showExpressAd();
    }
    private void loadExpressVideo() {
        final AdPlatformSDK adPlatformSDK = AdPlatformSDK.getInstance(this);
        adPlatformSDK.loadExpressAd(this, "ad_expredd_snatch", 300, 200, new AdCallback() {
            @Override
            public void onDismissed() {

            }

            @Override
            public void onNoAd(AdError adError) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onPresent() {

            }

            @Override
            public void onClick() {

            }

            @Override
            public void onLoaded() {
//                if (!isShow) {
//                    adPlatformSDK.showExpressAd();
//                }
            }
        }, fl_ad_containesss);
    }



    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }


    public static void snatchTreasureJump(Context context) {
        Intent intent = new Intent(context, SnatchTreasureActivity.class);
        context.startActivity(intent);
    }
    private FrameLayout fl_ad_container;
    private  SnatchDialog snatchDialog;
    private  TextView tv_snatchNo;
    private void initDialogs(){
        snatchDialog = new SnatchDialog(this);
        View builder = snatchDialog.builder(R.layout.snatch_item);
        tv_snatchNo = builder.findViewById(R.id.tv_prizeNums);
        fl_ad_container = builder.findViewById(R.id.fl_ad_containerss);
        TextView tv_sure = builder.findViewById(R.id.tv_sure);
        lineMySnatch.setVisibility(View.VISIBLE);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snatchDialog.setDismiss();
            }
        });
        loadVideo();
    }

    private boolean isshow;
    private void showDialogs(String user_nums) {
        if (snatchDialog!=null){
            if (!TextUtils.isEmpty(user_nums)) {
                tv_snatchNo.setText(user_nums);
                if (TextUtils.isEmpty(allSnatchStr)){
                    allSnatchStr=user_nums.replaceAll(",", "   ");
                }else {
                    allSnatchStr=allSnatchStr+" "+user_nums.replaceAll(",", "   ");
                }
                tvMySnatchNums.setText(allSnatchStr);
                lineMySnatch.setVisibility(View.VISIBLE);
            }
            final AdPlatformSDK adPlatformSDK = AdPlatformSDK.getInstance(this);
            loadVideo();
            adPlatformSDK.setUserId(CacheDataUtils.getInstance().getUserInfo().getId()+"");
            snatchDialog.setDismissListen(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            isshow = adPlatformSDK.showExpressAd();
            snatchDialog.setShow();
        }
    }

    private void loadVideo(){
            final AdPlatformSDK adPlatformSDK = AdPlatformSDK.getInstance(this);
            adPlatformSDK.loadExpressAd(this,"ad_duobao", 300,200,new AdCallback() {
                @Override
                public void onDismissed() {

                }

                @Override
                public void onNoAd(AdError adError) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void onPresent() {

                }

                @Override
                public void onClick() {

                }

                @Override
                public void onLoaded() {
                    if(!isshow){
                        adPlatformSDK.showExpressAd();
                    }
                }
            }, fl_ad_container);
    }


    private void showDialogsTwo(String user_nums) {
        SnatchDialog snatchDialog = new SnatchDialog(this);
        View builder = snatchDialog.builder(R.layout.snatch_item_two);
        TextView tv_snatchNo = builder.findViewById(R.id.tv_prizeNums);
        TextView tv_sure = builder.findViewById(R.id.tv_sure);
        if (!TextUtils.isEmpty(user_nums)) {
            tv_snatchNo.setText(user_nums);
        }
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolUtils instance = SoundPoolUtils.getInstance();
                instance.initSound();
                snatchDialog.setDismiss();
            }
        });
        snatchDialog.setShow();
    }


    @OnClick({R.id.iv_back, R.id.tv_history, R.id.line_ruleDetails, R.id.line_snatchsOne, R.id.line_snatchsTwo,R.id.tv_all})
    public void onViewClicked(View view) {
        SoundPoolUtils instance = SoundPoolUtils.getInstance();
        instance.initSound();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_history:
                SnatchTreasureHistoryActivity.snatchtreasurehistoryJump(this);
                break;
            case R.id.line_ruleDetails:
                if (snatchDetailsBeans != null) {
                    String strContents = "";
                    if (!TextUtils.isEmpty(snatchDetailsBeans.getContent())) {
                        strContents = snatchDetailsBeans.getContent();
                    }
                    SnatchTreasureRuleActivity.snatchTreasureRuleJump(this, strContents);
                }
                break;
            case R.id.line_snatchsOne:
                if (snatchDetailsBeans != null) {
                    if (snatchNums >= 5) {
                        mPresenter.getSnatchPost(CacheDataUtils.getInstance().getUserInfo().getGroup_id() + "", "5", snatchDetailsBeans.getId() + "");
                    }
                }
                break;
            case R.id.line_snatchsTwo:
                if (snatchDetailsBeans != null) {
                    if (snatchNums > 0) {
                        mPresenter.getSnatchPost(CacheDataUtils.getInstance().getUserInfo().getGroup_id() + "", "1", snatchDetailsBeans.getId() + "");
                    }
                }
                break;
            case R.id.tv_all:
                if (!TextUtils.isEmpty(allSnatchStr)){
                    showDialogsTwo(allSnatchStr);
                }
                break;
        }
    }

    private CountDownUtils countDownUtils;
    private CountDownUtilsTwo sysCountDownUtils;

    @Override
    public void getSnatchinfoDetailsSuccess(SnatchDetailsBeans data) {
        nodata_view.setVisibility(View.GONE);
        this.snatchDetailsBeans = data;
        if (!TextUtils.isEmpty(data.getExcerpt())) {
            Glide.with(this).load(data.getExcerpt()).into(ivSnatch);
        }
        tvSnatchCurrNums.setText(data.getNum() + "/1000");
        progressbar.setProgress(data.getNum());
        if (infoId == 0) {
            infoId = data.getId();
            setStatus(data);
        } else {
            if (infoId == data.getId()) {//没开奖
                countDownUtils.setHours(0, 0, 10);
            } else {
                infoId = data.getId();
                setStatus(data);
            }
        }
    }

    public void setStatus(SnatchDetailsBeans data) {
        snatchNums = data.getUser_other().getTreasure_num();
        tvPrizePeriods.setText("第" + data.getAdd_num() + "期");
        int i = data.getMoney().indexOf(".");
        if (i > 0) {
            tvMoney.setText(data.getMoney().substring(0, i) + "元");
        } else {
            tvMoney.setText(data.getMoney() + "元");
        }
        tvFirstSantchTimes.setText(data.getStart());
        long currTimes = System.currentTimeMillis();
        long yuTimes = data.getEnd_time() * 1000 - data.getSys_time() * 1000;
        if (!TextUtils.isEmpty(data.getUser_num())) {
            String user_num = data.getUser_num();
            allSnatchStr=user_num.replaceAll(",", "   ");
            tvMySnatchNums.setText(user_num.replaceAll(",", "   "));
            lineMySnatch.setVisibility(View.VISIBLE);
        }else {
            lineMySnatch.setVisibility(View.GONE);
        }
        if (data.getUser_other() != null) {
            tvQuanNums.setText(data.getUser_other().getTreasure_num() + "");
            setViewsStatus(data.getUser_other().getTreasure_num());
        }
        countDownUtils.setHours(TimesUtils.getHourDiff(yuTimes), TimesUtils.getMinDiff(yuTimes), TimesUtils.getSecondDiff(yuTimes));
        if (sysCountDownUtils.getTimes() == null) {
            sysCountDownUtils.setHours(TimesUtils.getHour(currTimes), TimesUtils.getMinute(currTimes), TimesUtils.getSecond(currTimes));
        }
    }

    @Override
    public void getSnatchPostSuccess(SnatchPostBeans data) {
        snatchNums = data.getTreasure_num();
        if (data.getNew_level() > 0) {//升级了
            EventBus.getDefault().post(new Event.CashEvent());
        }
        tvQuanNums.setText(data.getTreasure_num() + "");
        showDialogs(data.getUser_num());
        setViewsStatus(data.getTreasure_num());
    }

    @Override
    public void getSnatchinfoDetailsError() {
        nodata_view.setVisibility(View.VISIBLE);
    }

    private void setViewsStatus(int nums) {
        if (nums == 0) {
            lineSnatchsTwo.setBackground(getResources().getDrawable(R.drawable.tv_bg_gray2));
            tvSnatchsTwoTitle.setTextColor(getResources().getColor(R.color.A1_656565));
            tvOnceNumsQuan.setTextColor(getResources().getColor(R.color.A1_656565));
            ivQuan2.setImageDrawable(getResources().getDrawable(R.drawable.quan1));
        } else {
            lineSnatchsTwo.setBackground(getResources().getDrawable(R.drawable.line_bg_yellow4));
            tvSnatchsTwoTitle.setTextColor(getResources().getColor(R.color.white));
            tvOnceNumsQuan.setTextColor(getResources().getColor(R.color.white));
            ivQuan2.setImageDrawable(getResources().getDrawable(R.drawable.quan2));
        }

        if (nums >= 5) {
            lineSnatchsOne.setBackground(getResources().getDrawable(R.drawable.line_bg_yellow4));
            tvSnatchsOneTitle.setTextColor(getResources().getColor(R.color.white));
            tvLianxuQuanNums.setTextColor(getResources().getColor(R.color.white));
            ivQuan1.setImageDrawable(getResources().getDrawable(R.drawable.quan2));
        } else {
            lineSnatchsOne.setBackground(getResources().getDrawable(R.drawable.tv_bg_gray2));
            tvSnatchsOneTitle.setTextColor(getResources().getColor(R.color.A1_656565));
            tvLianxuQuanNums.setTextColor(getResources().getColor(R.color.A1_656565));
            ivQuan1.setImageDrawable(getResources().getDrawable(R.drawable.quan1));
        }
    }

    private String getTv(long l) {
        if (l >= 10) {
            return l + "";
        } else {
            return "0" + l;//小于10,,前面补位一个"0"
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownUtils != null) {
            countDownUtils.clean();
        }
        if (sysCountDownUtils != null) {
            sysCountDownUtils.clean();
        }
    }

}