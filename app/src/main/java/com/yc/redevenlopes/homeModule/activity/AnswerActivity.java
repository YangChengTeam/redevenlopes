package com.yc.redevenlopes.homeModule.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.adplatform.AdPlatformSDK;
import com.yc.adplatform.ad.core.AdCallback;
import com.yc.adplatform.ad.core.AdError;
import com.yc.redevenlopes.R;
import com.yc.redevenlopes.base.BaseActivity;
import com.yc.redevenlopes.dialog.RedDialog;
import com.yc.redevenlopes.homeModule.adapter.AnswserAdapter;
import com.yc.redevenlopes.homeModule.contact.AnswerContact;
import com.yc.redevenlopes.homeModule.module.bean.AnswerBeans;
import com.yc.redevenlopes.homeModule.present.AnswerPresenter;
import com.yc.redevenlopes.homeModule.widget.ScrollWithRecyclerView;
import com.yc.redevenlopes.homeModule.widget.ToastShowViews;
import com.yc.redevenlopes.utils.CacheDataUtils;
import com.yc.redevenlopes.utils.ClickListenNameTwo;
import com.yc.redevenlopes.utils.CommonUtils;
import com.yc.redevenlopes.utils.DisplayUtil;
import com.yc.redevenlopes.utils.SoundPoolUtils;
import com.yc.redevenlopes.utils.ToastUtilsViews;
import com.yc.redevenlopes.utils.VUiKit;
import java.util.List;
import butterknife.BindView;

/**
 * 答题任务
 */
public class AnswerActivity extends BaseActivity<AnswerPresenter> implements AnswerContact.View {
    @BindView(R.id.recyclerView)
    ScrollWithRecyclerView recyclerView;
    private AnswserAdapter answserAdapter;
    private int index;
    private FrameLayout fl_ad_containe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isNeedNewTitle(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_answer;
    }

    @Override
    public void initEventAndData() {
        fl_ad_containe=findViewById(R.id.fl_ad_containe);
        setTitle("答题任务");
        initRecyclerView();
        mPresenter.getAnswerQuestionList(CacheDataUtils.getInstance().getUserInfo().getGroup_id()+"");
        loadVideo();
        showExpress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVideo();
    }

    private void showExpress() {
        loadExpressVideo();
        final AdPlatformSDK adPlatformSDK = AdPlatformSDK.getInstance(this);
        adPlatformSDK.setUserId(CacheDataUtils.getInstance().getUserInfo().getId() + "");
        isShow= adPlatformSDK.showExpressAd();
    }

    private boolean isShow;
    private void loadExpressVideo() {
        int screenWidth = CommonUtils.getScreenWidth(this);
        int w = (int) (screenWidth);
        int h = w * 2 / 3;
        final AdPlatformSDK adPlatformSDK = AdPlatformSDK.getInstance(this);
        int dpw = DisplayUtil.px2dip(AnswerActivity.this, w);
        int dph = DisplayUtil.px2dip(AnswerActivity.this, h);
        adPlatformSDK.loadExpressAd(this, "ad_expredd_answer", dpw, dph, new AdCallback() {
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
        }, fl_ad_containe);
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    public static void answerJump(Context context) {
        Intent intent = new Intent(context, AnswerActivity.class);
        context.startActivity(intent);
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        answserAdapter = new AnswserAdapter(null);
        recyclerView.setAdapter(answserAdapter);
        answserAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (ClickListenNameTwo.isFastClick()) {
                    SoundPoolUtils instance = SoundPoolUtils.getInstance();
                    instance.initSound();
                    List<AnswerBeans> lists = adapter.getData();
                    if ( lists.get(position).getIs_continue()==1){
                        index=position;
                        showRedDialog(lists.get(position).getMoney(),lists.get(position).getQuestion_num());
                    }
                }
            }
        });
    }
    private RedDialog redDialog;
    public void showRedDialog(String money,int questionNums) {
        redDialog = new RedDialog(this);
        View builder = redDialog.builder(R.layout.red_answer_dialog_item);
        ImageView iv_close = builder.findViewById(R.id.iv_close);
        TextView tv_money = builder.findViewById(R.id.tv_money);
        ImageView iv_open = builder.findViewById(R.id.iv_open);
        TextView tv_answerDes=builder.findViewById(R.id.tv_answerDes);
        tv_answerDes.setVisibility(View.VISIBLE);
        tv_answerDes.setText("答完"+questionNums+"题，即可获得升级奖励");
        iv_open.setImageDrawable(getResources().getDrawable(R.drawable.red_ans));
        tv_money.setText(money);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPoolUtils instance = SoundPoolUtils.getInstance();
                instance.initSound();
                if (!CommonUtils.isDestory(AnswerActivity.this)){
                    redDialog.setDismiss();
                }
            }
        });
        iv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answerVideo = CacheDataUtils.getInstance().getAnswerVideo();
                if (TextUtils.isEmpty(answerVideo)){//第一次不看视频
                    List<AnswerBeans> lists = answserAdapter.getData();
                    AnswerBeans answerBeans = lists.get(index);
                    answerBeans.setIs_continue(0);
                    answserAdapter.notifyItemChanged(index);
                    CacheDataUtils.getInstance().setAnswerVideo();
                    AnswerDetailsActivity.AnswerDetailsJump(AnswerActivity.this,answerBeans.getId()+"",answerBeans.getTotal(),answerBeans.getMoney(),answerBeans.getId()+"");
                    if (redDialog!=null){
                        redDialog.setDismiss();
                    }
                }else {
                    showVideo();
                }
            }
        });
        VUiKit.postDelayed(2000, () -> {
            iv_close.setVisibility(View.VISIBLE);
        });
        if (!CommonUtils.isDestory(AnswerActivity.this)){
            redDialog.setShow();
        }
    }

    @Override
    protected void onDestroy() {
        if (redDialog!=null){
            redDialog.setDismiss();
        }
        super.onDestroy();
    }

    @Override
    public void getAnswerQuestionListSuccess(List<AnswerBeans> data) {
         answserAdapter.setNewData(data);
         answserAdapter.notifyDataSetChanged();
    }

    private void showVideo() {
        final AdPlatformSDK adPlatformSDK = AdPlatformSDK.getInstance(this);
        adPlatformSDK.setUserId(CacheDataUtils.getInstance().getUserInfo().getId()+"");
        adPlatformSDK.showRewardVideoAd();
        loadVideo();
    }

    private void loadVideo(){
        Log.d("ccc", "---------2----showVideo: ");
        final AdPlatformSDK adPlatformSDK = AdPlatformSDK.getInstance(this);
        adPlatformSDK.loadRewardVideoVerticalAd(this, "ad_wenda",new AdCallback() {
            @Override
            public void onDismissed() {
                if (!CommonUtils.isDestory(AnswerActivity.this)){
                    ToastUtilsViews.showCenterToast("1","");
                }
                List<AnswerBeans> lists = answserAdapter.getData();
                if (index<lists.size()){
                    AnswerBeans answerBeans = lists.get(index);
                    answerBeans.setIs_continue(0);
                    answserAdapter.notifyItemChanged(index);
                    AnswerDetailsActivity.AnswerDetailsJump(AnswerActivity.this,answerBeans.getId()+"",answerBeans.getTotal(),answerBeans.getMoney(),answerBeans.getId()+"");
                }
                if (redDialog!=null){
                    redDialog.setDismiss();
                }
                if (!CommonUtils.isDestory(AnswerActivity.this)){
                    ToastShowViews.getInstance().cancleToast();
                }
            }

            @Override
            public void onNoAd(AdError adError) {
                Log.d("ccc", "---------2----showVideo: "+adError.getCode()+"---"+adError.getMessage());
            }

            @Override
            public void onComplete() {
                if (!CommonUtils.isDestory(AnswerActivity.this)){
                    ToastShowViews.getInstance().cancleToast();
                }
                mPresenter.updtreasure(CacheDataUtils.getInstance().getUserInfo().getGroup_id() + "");//更新券
            }

            @Override
            public void onPresent() {
                if (!CommonUtils.isDestory(AnswerActivity.this)){
                    ToastShowViews.getInstance().showMyToast();
                }
            }

            @Override
            public void onClick() {

            }

            @Override
            public void onLoaded() {

            }
        });
    }
}