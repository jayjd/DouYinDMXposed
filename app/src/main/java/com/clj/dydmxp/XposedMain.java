package com.clj.dydmxp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.clj.dydmxp.message.HttpSendMessage;

import org.json.JSONObject;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedMain implements IXposedHookLoadPackage {
    boolean isFrist = true;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals("com.ss.android.ugc.aweme") && lpparam.isFirstApplication) {
            startHook(lpparam.classLoader);
        }
    }

    public String getVersion(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startHook(ClassLoader classLoader) {
        Logger("进入抖音进程。。。");
        //WebcastSocialMessage 关注
        Class<?> aClass3 = XposedHelpers.findClass("com.bytedance.android.livesdk.message.model.qv", classLoader);
        XposedHelpers.findAndHookMethod(aClass3, "decode", "com.bytedance.android.tools.a.a.g", new XC_MethodHook() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object result = param.getResult();
//                    Object commonDataStr = XposedHelpers.getObjectField(result, "baseMessage");
//                    String content = XposedHelpers.getObjectField(result, "LIZLLL").toString();
                Object lizlll = XposedHelpers.getObjectField(result, "LIZJ");
                Object getRealNickName = XposedHelpers.callMethod(lizlll, "getRealNickName");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("method", "WebcastSocialMessage");
                jsonObject.put("RealNickName", getRealNickName);
                HttpSendMessage.PostHttpSendMessage(jsonObject.toString());
                Logger("WebcastSocialMessage..." + getRealNickName + "...关注了主播...");
            }
        });
        //WebcastMemberMessage 进直播间
        Class<?> aClass = XposedHelpers.findClass("com.bytedance.android.livesdk.message.model.nz", classLoader);
        XposedHelpers.findAndHookMethod(aClass, "decode", "com.bytedance.android.tools.a.a.g", new XC_MethodHook() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object result = param.getResult();
//                    Object commonDataStr = XposedHelpers.getObjectField(result, "baseMessage");
                Object lizj = XposedHelpers.getObjectField(result, "LIZJ");
                Object getRealNickName = XposedHelpers.callMethod(lizj, "getRealNickName");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("method", "WebcastMemberMessage");
                jsonObject.put("RealNickName", getRealNickName);
                HttpSendMessage.PostHttpSendMessage(jsonObject.toString());
                Logger("WebcastMemberMessage..." + getRealNickName + "...进入直播间");
            }
        });
        //WebcastRoomMessage 直播间公告  直播间等级显示
        Class<?> aClass4 = XposedHelpers.findClass("com.bytedance.android.livesdk.message.model.qg", classLoader);
        XposedHelpers.findAndHookMethod(aClass4, "decode", "com.bytedance.android.tools.a.a.g", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object result = param.getResult();
//                    Object commonDataStr = XposedHelpers.getObjectField(result, "baseMessage");
                String content = XposedHelpers.getObjectField(result, "LIZIZ").toString();
                Logger("WebcastRoomMessage..." + content);
            }
        });
        //WebcastChatMessage 直播间弹幕
        Class<?> aClass1 = XposedHelpers.findClass("com.bytedance.android.livesdk.message.model.jf", classLoader);
        XposedHelpers.findAndHookMethod(aClass1, "decode", "com.bytedance.android.tools.a.a.g", new XC_MethodHook() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object result = param.getResult();
                String content = XposedHelpers.getObjectField(result, "LIZJ").toString();
                Object lizlll = XposedHelpers.getObjectField(result, "LIZLLL");
                Object getRealNickName = XposedHelpers.callMethod(lizlll, "getRealNickName");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("method", "WebcastChatMessage");
                jsonObject.put("RealNickName", getRealNickName);
                jsonObject.put("Content", content);
                HttpSendMessage.PostHttpSendMessage(jsonObject.toString());
//                Logger("WebcastChatMessage..." + getRealNickName + "..." + content + "...");
            }
        });
        //WebcastGiftMessage 礼物
        Class<?> aClass2 = XposedHelpers.findClass("com.bytedance.android.livesdkapi.message._CommonMessageData_ProtoDecoder", classLoader);
        XposedHelpers.findAndHookMethod(aClass2, "decodeStatic", "com.bytedance.android.tools.a.a.g", new XC_MethodHook() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String[] commonMessageData = param.getResult().toString().replace("CommonMessageData", "").replace("{", "").replace("}", "").split(",");
                for (int i = 0; i < commonMessageData.length; i++) {
                    String[] split = commonMessageData[i].split("=");
                    if (split[0].trim().equals("method") && split[1].trim().contains("WebcastGiftMessage")) {
                        Logger("WebcastGiftMessage..."
                                + commonMessageData[i + 1].split("=")[1].trim() + "...");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("method", "WebcastGiftMessage");
                        jsonObject.put("Content", commonMessageData[i + 1].split("=")[1].trim());
                        HttpSendMessage.PostHttpSendMessage(jsonObject.toString());
                        break;
                    }
                }
            }
        });
    }

    public void Logger(String content) {
        Log.i("抖音", content);
    }

}
