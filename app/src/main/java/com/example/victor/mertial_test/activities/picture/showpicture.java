package com.example.victor.mertial_test.activities.picture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.activities.setting.SettingActivity;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class showpicture extends ActionBarActivity  implements View.OnClickListener{
    ProgressDialog dialog;
    Boolean flag;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String SERVER_ADDRESS="http://wecarememberpic.comze.com/";
    CircleImageView imageToUpload;
    ImageButton cancel,confirm;

//    String name="newtest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpicture);


        cancel=(ImageButton)findViewById(R.id.cancel);
        confirm=(ImageButton)findViewById(R.id.confirm);

        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);

        imageToUpload = (CircleImageView) findViewById(R.id.imageToUpload);
        cancel = (ImageButton) findViewById(R.id.cancel);
        confirm = (ImageButton) findViewById(R.id.confirm);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.cancel:
                Intent intent = new Intent();
                intent.setClass(showpicture.this, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.confirm:
                SharedPreferences account = getSharedPreferences("wecare_member", MODE_PRIVATE);
                final String name=account.getString("account", null);
                imageToUpload.buildDrawingCache();
                Bitmap bmp=imageToUpload.getDrawingCache();
//                轉換為圖片指定大小
//                獲得圖片的寬高
                int width = bmp.getWidth();
                int height = bmp.getHeight();
                // 設置想要的大小
                if(width<=height) {
                    float scaleWidth = (float)0.4;
                    float scaleHeight = (float)(0.4* width / height);
                    // 取得想要缩放的matrix參數
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的圖片
                    Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
                    imageToUpload.setImageBitmap(newbm);
                    Bitmap image=((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                    dialog = ProgressDialog.show(showpicture.this, "系統訊息", "正在上傳，請稍後", true);
                    new UploadImage(image,name).execute();
                }else{
                    float scaleWidth = (float)(0.4*height/width);
                    float scaleHeight = (float)0.4;
                    // 取得想要缩放的matrix參數
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的圖片
                    Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
                    imageToUpload.setImageBitmap(newbm);
                    Bitmap image=((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                    dialog = ProgressDialog.show(showpicture.this, "系統訊息", "正在上傳，請稍後", true);
                    new UploadImage(image,name).execute();
                }

//                imageToUpload.buildDrawingCache();
//                Bitmap bmp1=imageToUpload.getDrawingCache();
//                int width1 = newbm.getWidth();
//                int height1 = newbm.getHeight();
//
//                int r = 0;
//                //取最短边做边长
//                if(width1 > height1) {
//                    r = height1;
//                } else {
//                    r = width1;
//                }
//                //构建一个bitmap
//                Bitmap backgroundBmp = Bitmap.createBitmap(width1, height1, Bitmap.Config.ARGB_8888);
//                //new一个Canvas，在backgroundBmp上画图
//                Canvas canvas = new Canvas(backgroundBmp);
//                Paint paint = new Paint();
//                //设置边缘光滑，去掉锯齿
//                paint.setAntiAlias(true);
//                //宽高相等，即正方形
//                RectF rect = new RectF(0, 0, r, r);
//                //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
//                //且都等于r/2时，画出来的圆角矩形就是圆形
//                canvas.drawRoundRect(rect, r/2, r/2, paint);
//                //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
//                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//                //canvas将bitmap画在backgroundBmp上
//                canvas.drawBitmap(newbm, null, rect, paint);
//                imageToUpload.setImageBitmap(backgroundBmp);
//                Bitmap image=((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
//                dialog = ProgressDialog.show(showpicture.this, "系統訊息", "正在上傳，請稍後", true);
//                new UploadImage(image,name).execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
        }
    }
    private class  UploadImage extends AsyncTask<Void,Void,Void> {

        Bitmap image;
        String name;

        public UploadImage(Bitmap image,String name){
            this.image=image;
            this.name=name;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image",encodedImage));
            dataToSend.add(new BasicNameValuePair("name",name));

            HttpParams httpRequestParams =getHttpRequestParams();

            HttpClient client =new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+"SavePicture.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            Intent intent2 = new Intent();
            intent2.setClass(showpicture.this, SettingActivity.class);
            startActivity(intent2);
        }

    }
    private HttpParams getHttpRequestParams(){
        HttpParams httpRequestParams=new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 10000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams,10000*30);
        return httpRequestParams;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

}
