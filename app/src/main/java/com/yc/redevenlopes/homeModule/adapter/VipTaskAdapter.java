package com.yc.redevenlopes.homeModule.adapter;

import android.animation.ValueAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.redevenlopes.R;
import com.yc.redevenlopes.homeModule.module.bean.VipTaskInfo;
import com.yc.redevenlopes.utils.CacheDataUtils;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by suns  on 2020/11/16 18:13.
 */
public class VipTaskAdapter extends BaseQuickAdapter<VipTaskInfo, BaseViewHolder> {

    public VipTaskAdapter(@Nullable List<VipTaskInfo> data) {
        super(R.layout.item_vip_task_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipTaskInfo item) {
        int totalNum = item.num;
        int finishNum = item.finish_num;
        helper.setText(R.id.tv_reward_title, item.title)
                .setText(R.id.tv_reward_num, "(" + finishNum + "/" + totalNum + ")");
        ProgressBar progressBar = helper.getView(R.id.progressbar_reward);
        progressBar.setMax(totalNum * 10);
        progressBar.setProgress(finishNum * 10);

       // setProgress(finishNum, progressBar);

        TextView tvRewardState = helper.getView(R.id.tv_reward_state);
        int status = item.status;
        if (status == 0) {//0代表未完成当前任务，1代表已完成，但未领取奖励2代表已领取奖励
            ((ImageView) helper.getView(R.id.iv_shou)).setVisibility(View.GONE);
            tvRewardState.setBackgroundResource(R.drawable.level_reward_undone_bg);
            tvRewardState.setText("去完成");
        } else if (status == 1) {
            int adapterPosition = helper.getAdapterPosition();
            if (adapterPosition==0){
                String taskShou = CacheDataUtils.getInstance().getTaskShou();
                if (TextUtils.isEmpty(taskShou)){
                    ((ImageView) helper.getView(R.id.iv_shou)).setVisibility(View.VISIBLE);
                }
            }else {
                ((ImageView) helper.getView(R.id.iv_shou)).setVisibility(View.GONE);
            }
            tvRewardState.setBackgroundResource(R.drawable.level_reward_done_bg);
            tvRewardState.setText("领取");
        } else if (status == 2) {
            ((ImageView) helper.getView(R.id.iv_shou)).setVisibility(View.GONE);
            tvRewardState.setBackground(null);
            tvRewardState.setText("已完成");
        }
        helper.addOnClickListener(R.id.rela_re);
    }

    private void setProgress(int finishNum, ProgressBar progressBar) {
        Log.d("ccc", "------------setProgress: '"+finishNum*10);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, finishNum * 10);
        valueAnimator.setDuration(1500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            Log.d("ccc", "------------setProgress: '"+value);
            progressBar.setProgress(value);
        });
        valueAnimator.start();
    }
}
