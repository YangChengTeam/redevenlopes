package com.yc.redevenlopes.application;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.google.gson.Gson;
import com.kk.share.UMShareImpl;
import com.lq.lianjibusiness.base_libary.App.App;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.yc.redevenlopes.di.component.AppComponent;
import com.yc.redevenlopes.di.component.DaggerAppComponent;
import com.yc.redevenlopes.di.module.AppModule;
import com.yc.redevenlopes.homeModule.module.bean.ChannelInfo;
import com.yc.redevenlopes.utils.FileUtil;
import com.yc.redevenlopes.utils.UserManger;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import androidx.multidex.MultiDex;


/**
 * Created by ccc on 2020/9/15.
 */

public class MyApplication extends App {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //bugly异常上报
        CrashReport.initCrashReport(getApplicationContext(), "511db8dc12", true);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.

        //  Glide.get(getApplicationContext()).setMemoryCategory(MemoryCategory.LOW);
        MMKV.initialize(this);
        //切换至商业版服务
        //  HeConfig.switchToBizService();
        // 渠道  主要是获取agentId
        initChannel();
        initUM();
        UserManger.reglog();
    }

    public String getAgentId() {
        return agentId;
    }

    public String agentId = "1";

    private void initChannel() {
        ApplicationInfo appinfo = getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zf = null;
        try {
            zf = new ZipFile(sourceDir);
            ZipEntry ze = zf.getEntry("META-INF/gamechannel.json");
            InputStream in = zf.getInputStream(ze);
            String result = FileUtil.readString(in);
            ChannelInfo channelInfo = new Gson().fromJson(result, ChannelInfo.class);
            agentId = channelInfo.agent_id;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        }
    }

    private void initUM() {
        UMConfigure.init(this, "5f9157a94d7bf81a2ea8ed4c", agentId, UMConfigure.DEVICE_TYPE_PHONE, "");
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMConfigure.setLogEnabled(true);
        UMShareAPI.get(getApplicationContext()).setShareConfig(config);


        //初始化友盟SDK
        UMShareAPI.get(this); //初始化sdk
        UMShareImpl.Builder builder = new UMShareImpl.Builder();
        builder.setWeixin("wxe224386e89afc8c1", "a6ce8283ca3524ff2d75dad0791a0101")
                .setQQ("101811246", "8310b6974f5f712f827fc8eff8228822")
                .build(this);
    }


    public static AppComponent getAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .build();
    }

}
