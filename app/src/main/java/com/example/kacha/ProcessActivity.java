package com.example.kacha;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class ProcessActivity extends AppCompatActivity {
    public static final int CHOOSE_PHOTO=2;

    private ImageView picture;
    private Bitmap image;
    private String imagePath;
    private AlertDialog.Builder builder;
    private stack s=new stack();

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        Intent it=getIntent();
        imagePath=it.getStringExtra("image_path");
        picture=(ImageView) findViewById(R.id.picture);

        displayImage(imagePath);

        picture=(ImageView) findViewById(R.id.picture);
        Button fun1=(Button) findViewById(R.id.fun1);
        Button fun2=(Button) findViewById(R.id.fun2);
        Button fun3=(Button) findViewById(R.id.fun3);
        Button fun4=(Button) findViewById(R.id.fun4);
        Button fun5=(Button) findViewById(R.id.fun5);
        Button fun6=(Button) findViewById(R.id.fun6);
        Button compare=(Button) findViewById(R.id.compare);
        Button revoke=(Button) findViewById(R.id.revoke);
        Button download=(Button) findViewById(R.id.download);
        Button share=(Button) findViewById(R.id.share);
        Button openalbum=(Button) findViewById(R.id.openalbum);
        final Button dropout=(Button) findViewById(R.id.drop_out);
        final LinearLayout filter=(LinearLayout) findViewById(R.id.filter);
        final LinearLayout fun=(LinearLayout) findViewById(R.id.fun);
        filter.setVisibility(View.GONE);


        openalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimpleDialog(v);
            }
        });

        fun1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Button fliter1=(Button) findViewById(R.id.filter1);
                Button fliter2=(Button) findViewById(R.id.filter2);
                Button fliter3=(Button) findViewById(R.id.filter3);
                Button fliter4=(Button) findViewById(R.id.filter4);
                //动态设置图片
                fliter1.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(setImage(1,image)), null, null);
                fliter2.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(setImage(2,image)), null, null);
                fliter3.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(setImage(3,image)), null, null);
                fliter4.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(setImage(4,image)), null, null);

                fun.setVisibility(View.GONE);
                filter.setVisibility(View.VISIBLE);

                fliter1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Filter myFilter = new Filter();
                        myFilter.addSubFilter(new ColorOverlaySubFilter(100, .5f, .5f, .0f));
                        Bitmap outputImage = myFilter.processFilter(image);
                        picture.setImageBitmap(outputImage);
                        s.push(outputImage);
                    }
                });

                fliter2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Filter myFilter = new Filter();
                        myFilter.addSubFilter(new SaturationSubFilter(1.3f));
                        Bitmap outputImage = myFilter.processFilter(image);
                        picture.setImageBitmap(outputImage);
                        s.push(outputImage);
                    }
                });

                fliter3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Filter myFilter = new Filter();
                        myFilter.addSubFilter(new ContrastSubFilter(1.6f));
                        Bitmap outputImage = myFilter.processFilter(image);
                        picture.setImageBitmap(outputImage);
                        s.push(outputImage);
                    }
                });

                fliter4.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Filter myFilter = new Filter();
                        myFilter.addSubFilter(new BrightnessSubFilter(60));
                        Bitmap outputImage = myFilter.processFilter(image);
                        picture.setImageBitmap(outputImage);
                        s.push(outputImage);
                    }
                });

                dropout.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        filter.setVisibility(View.GONE);
                        fun.setVisibility(View.VISIBLE);
                    }
                });


            }
        });

        fun2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {

                    Client client = new Client();// 启动客户端连接
                    //client.sendFile(imagePath); // 传输文件
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        fun3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        fun4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        fun5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        fun6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        compare.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: //手指按下
                        if(s.top<1){
                            Context context =getApplicationContext();
                            Toast ts=Toast.makeText(context,"无法对比，请先进行操作", Toast.LENGTH_LONG);
                            ts.show();
                            break;
                        }
                        Bitmap sTop = (Bitmap) s.pop();
                        picture.setImageBitmap(s.topBitmap());
                        s.push(sTop);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(s.top<1){
                            break;
                        }
                        picture.setImageBitmap(s.topBitmap());
                        break;
                    default:

                }
                return true;
            }
        });

        revoke.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(s.top>0) {
                    s.pop();
                    picture.setImageBitmap(s.topBitmap());
                }
                else{
                    Context context =getApplicationContext();
                    Toast ts=Toast.makeText(context,"无法撤销，请先进行操作", Toast.LENGTH_LONG);
                    ts.show();
                }
            }
        });

        dropout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Context context =getApplicationContext();
                Toast ts=Toast.makeText(context,"确认退出APP", Toast.LENGTH_LONG);
                ts.show();
            }
        });

        share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Context context =getApplicationContext();
                PopupMenu popup = new PopupMenu(context, v);//第二个参数是绑定的那个view
                //获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.sharemenu, popup.getMenu());
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Context context =getApplicationContext();
                        switch (item.getItemId()) {
                            case R.id.weChat:
                                ShareFileUtils.shareImageToWeChat(context, imagePath);
                                break;
                            case R.id.qq:
                                ShareFileUtils.shareImageToQQ(context, imagePath);
                                break;
                            case R.id.pyq:
                                ShareFileUtils.shareImageToWeChatFriend(context, imagePath);
                                break;
                            case R.id.qqZone:
                                ShareFileUtils.shareImageToQZone(context, imagePath);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                //显示(这一行代码不要忘记了)
                popup.show();
            }
        });

        download.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String state = Environment.getExternalStorageState();
                //如果状态不是mounted，无法读写
                if (!state.equals(Environment.MEDIA_MOUNTED)) {
                    Context context =getApplicationContext();
                    Toast ts=Toast.makeText(context,"无法保存", Toast.LENGTH_LONG);
                    ts.show();
                    return;
                }

                Calendar now = new GregorianCalendar();
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                String fileName = simpleDate.format(now.getTime());

                File f = new File(imagePath + fileName + ".jpg");
                if (f.exists()) {
                    f.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    s.topBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    Uri uri = Uri.fromFile(f);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    Context context =getApplicationContext();
                    Toast ts=Toast.makeText(context,"保存成功", Toast.LENGTH_LONG);
                    ts.show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){

        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);

        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }

        Intent it=new Intent(getApplicationContext(),ProcessActivity.class);//启动MainActivity
        it.putExtra("image_path",imagePath);
        startActivity(it);
        finish();//关闭当前活动
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        imagePath=getImagePath(uri,null);

        Intent it=new Intent(getApplicationContext(),ProcessActivity.class);//启动MainActivity
        it.putExtra("image_path",imagePath);
        startActivity(it);
        finish();//关闭当前活动
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath!=null){
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            options.inJustDecodeBounds = false;
            image=BitmapFactory.decodeFile(imagePath,options);
            s.push(image);
            picture.setImageBitmap(image);
        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    private void showSimpleDialog(View view) {
        builder=new AlertDialog.Builder(this);
        builder.setMessage("确认打开新的图片？您将丢失之前的编辑");
        //监听下方button点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            //打开相册
            public void onClick(DialogInterface dialogInterface, int i) {
                if(ContextCompat.checkSelfPermission(ProcessActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProcessActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public Bitmap setImage(int i,Bitmap image){
        float width = image.getWidth();
        float height = image.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = (float)0.2;
        float scaleHeight= (float)0.2;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap outputImage = Bitmap.createBitmap(image, 0, 0, (int) width,(int) height, matrix, true);

        Filter myFilter = new Filter();
        switch(i){
            case 1:
                myFilter.addSubFilter(new ColorOverlaySubFilter(100, .5f, .5f, .0f));
                outputImage = myFilter.processFilter(outputImage);
                break;
            case 2:
                myFilter.addSubFilter(new SaturationSubFilter(1.3f));
                outputImage = myFilter.processFilter(outputImage);
                break;
            case 3:
                myFilter.addSubFilter(new ContrastSubFilter(1.6f));
                outputImage = myFilter.processFilter(outputImage);
                break;
            case 4:
                myFilter.addSubFilter(new BrightnessSubFilter(60));
                outputImage = myFilter.processFilter(outputImage);
                break;
            default:
        }
        return outputImage;
    }

    private void sendImage(Bitmap bm)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("img", img);
        /*
        client.post("http://192.168.0.192", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Toast.makeText(ProcessActivity.this, "Upload Success!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(ProcessActivity.this, "Upload Fail!", Toast.LENGTH_LONG).show();
            }
        });
        */
    }
}
