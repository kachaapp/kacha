package com.example.kacha;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.File;
import java.util.List;


public class ShareFileUtils {
    /**
     * 分享单张图片
     *
     * @param context
     * @param path
     */
    public static void shareImage(Context context, String path) {
        shareImage(context, path, null, null, null);
    }

    /**
     * 分享到微信好友，单图
     */
    public static void shareImageToWeChat(Context context, String path) {
        //判断是否安装微信，如果没有安装微信 又没有判断就直达微信分享是会挂掉的
        if (!isAppInstall(context, "com.tencent.mm")) {
            Toast.makeText(context,"您还没有安装微信",Toast.LENGTH_SHORT).show();
            return;
        }
        shareImage(context, path, null, "com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
    }



    /**
     * 分享到微信朋友圈，单图
     */
    public static void shareImageToWeChatFriend(Context context, String path) {
        if (!isAppInstall(context, "com.tencent.mm")) {
            Toast.makeText(context,"您还没有安装微信",Toast.LENGTH_SHORT).show();
            return;
        }
        shareImage(context, path, null, "com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
    }


    /**
     * 分享图片给QQ好友，单图
     */
    public static void shareImageToQQ(Context context, String path) {
        if (!isAppInstall(context, "com.tencent.mobileqq")) {
            Toast.makeText(context,"您还没有安装QQ",Toast.LENGTH_SHORT).show();
            return;
        }
        shareImage(context, path, null, "com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
    }


    /**
     * 分享图片到QQ空间，单图
     */
    public static void shareImageToQZone(Context context, String path) {
        if (!isAppInstall(context, "com.qzone")) {
            Toast.makeText(context,"您还没有安装QQ空间",Toast.LENGTH_SHORT).show();
            return;
        }
        shareImage(context, path, null, "com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity");
    }


    /**
     * 检测手机是否安装某个应用
     *
     * @param context
     * @param appPackageName 应用包名
     * @return true-安装，false-未安装
     */
    public static boolean isAppInstall(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 分享前必须执行本代码，主要用于兼容SDK18以上的系统
     */
    private static void checkFileUriExposure() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

    /**
     * @param context  上下文
     * @param path     不为空的时候，表示分享单张图片，会检验图片文件是否存在
     * @param pathList 不为空的时候表示分享多张图片，会检验每一张图片是否存在
     * @param pkg      分享到的指定app的包名
     * @param cls      分享到的页面（微博不需要指定页面）
     */
    private static void shareImage(Context context, String path, List<String> pathList, String pkg, String cls) {
        if (path == null && pathList == null) {
            Toast.makeText(context,"找不到您要分享的图片文件",Toast.LENGTH_SHORT).show();
            return;
        }

        checkFileUriExposure();

        try {
            if (path != null) {
                Intent intent = new Intent();
                if (pkg != null && cls != null) {
                    //指定分享到的app
                    if (pkg.equals("com.sina.weibo")) {
                        //微博分享的需要特殊处理
                        intent.setPackage(pkg);
                    } else {
                        ComponentName comp = new ComponentName(pkg, cls);
                        intent.setComponent(comp);
                    }
                }
                intent.setAction(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
                intent.setType("image/*");   //分享文件
                context.startActivity(Intent.createChooser(intent, "分享"));
            }
        } catch (Exception e) {
            Toast.makeText(context,"分享失败，未知错误",Toast.LENGTH_SHORT).show();
        }
    }

}
