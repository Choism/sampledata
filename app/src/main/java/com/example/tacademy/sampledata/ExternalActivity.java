package com.example.tacademy.sampledata;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExternalActivity extends AppCompatActivity {

    View layout;
    ImageView captureView;
    private static final String FILE_NAME = "my_image.png";

    public File getImageFile(){//Enironment 는 사진이 저장해야되는 디렉토리 파일이 있는데 그 사진파일을 저장할 디렉토리 파일을 찾아 주는 놈
        File picture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File parent = new File(picture, "my_image"); // picture 자리에 my_image파일을 저장함
        if(!parent.exists()){ // -.existsd -. 존재하는지 ?
            parent.mkdirs(); //parent 생성!
        }
        return new File(parent, FILE_NAME);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external);
        layout = findViewById(R.id.layout_capture);
        captureView = (ImageView)findViewById(R.id.image_capture);

        Button btn = (Button)findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bm = getViewBitmap(layout);
                File file = getImageFile();
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    // compress 저장할 형식을 지정?
                    // 이미지 저장 : Bitmap PNG형태로 100% 로 압축하여 fos에 지정
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn = (Button)findViewById(R.id.btn_load);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = getImageFile(); // 이미지 파일을 얻어와 file에 셋팅
                try {
                    FileInputStream fis = new FileInputStream(file);
                    Bitmap bm = BitmapFactory.decodeStream(fis); //파일에서 이미지파일 읽어오는 법

                    fis.close(); // 처리하고 닫아줌
                    captureView.setImageBitmap(bm); // captureView 에 불러온 이미지 파일 셋팅
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        checkPermission();
    }

    // 퍼미션 체크 확인하여 기능을 실행시 권한 을 주고, 사용할 수 있게 하는 방법
    private static final int RC_PERMISSION = 100;

    private void checkPermission() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (permissions.size() > 0) {
            boolean isShowUI = false;
            for (String perm : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                    isShowUI = true;
                    break;
                }
            }

            final String[] perms = permissions.toArray(new String[permissions.size()]);

            if (isShowUI) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permission");
                builder.setMessage("External Storage...");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(ExternalActivity.this, perms, RC_PERMISSION);
                    }
                });
                builder.create().show();
                return;
            }

            ActivityCompat.requestPermissions(this, perms, RC_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (permissions != null) {
                boolean granted = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        granted = false;
                    }
                }
                if (!granted) {
                    Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

}
