package com.yc.redevenlopes.homeModule.activity;



import android.os.Bundle;
import com.yc.redevenlopes.R;
import com.yc.redevenlopes.base.BaseActivity;
import com.yc.redevenlopes.homeModule.contact.WithdrawLeadborderContact;
import com.yc.redevenlopes.homeModule.present.WithdrawLeadborderPresenter;
/**
 * 提现排行榜
 */
public class WithdrawLeadborderActivity extends BaseActivity<WithdrawLeadborderPresenter> implements WithdrawLeadborderContact.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_withdraw_leadborder;
    }

    @Override
    public void initEventAndData() {

    }

    @Override
    public void initInject() {
         getActivityComponent().inject(this);
    }
}