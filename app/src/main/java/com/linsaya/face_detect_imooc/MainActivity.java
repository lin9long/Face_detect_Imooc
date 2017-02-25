package com.linsaya.face_detect_imooc;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.megvii.facepp.sdk.Facepp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_PIC = 100;
    private ImageView iv_pic;
    private Button btn_detect;
    private Button btn_get;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PICK_PIC){
            if (intent!=null){
                Uri uri = intent.getData();
                System.out.println(uri);
//                String imagePath = uri.getPath();
//                System.out.println(imagePath);
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String imagePath = cursor.getString(columnIndex);
                cursor.close();
                // String imagePath = uri.getPath();
                System.out.println(imagePath);
                decodeImage(imagePath);
                iv_pic.setImageBitmap(mBitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void decodeImage(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath,options);
        double decode = Math.max(options.outHeight*1.0d/500f,options.outWidth*1.0d/500f);
        int ceil = (int) Math.ceil(decode);
        options.inSampleSize = ceil;
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(imagePath, options);

    }

    private void initEvent() {
        btn_detect.setOnClickListener(this);
        btn_get.setOnClickListener(this);
    }

    private void initView() {
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        btn_detect = (Button) findViewById(R.id.btn_detect);
        btn_get = (Button) findViewById(R.id.btn_get);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_detect:
                break;
            case R.id.btn_get:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_PIC);
                break;
        }
    }
}
