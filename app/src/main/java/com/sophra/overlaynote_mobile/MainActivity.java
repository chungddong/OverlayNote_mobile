package com.sophra.overlaynote_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private File file;
    private List mylist;
    private final int PERMISSIONS_REQUEST_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { //안드로이드 11이 경우
            if (Environment.isExternalStorageManager()) { //권한이 허용되어 있을 경우

                Log.d("overlaynote", "권한 허용됨");
                mylist = new ArrayList();
                String rootSD = Environment.getExternalStorageDirectory().toString();

                Log.d("overlaynote", "rootSD : " + rootSD);
                file = new File(rootSD + "/Download/OverlayNote/notedata.csv");

                Log.d("overlaynote", "file : " + file);


                /*File list[] = file.listFiles();

                for (int i = 0; i < list.length; i++) {
                    Log.d("overlaynote", "name : " + list[i].getName());
                 }*/



            } else { //권한이 허용 안되어있을 때

                Log.d("overlaynote", "권한 비허용");

                Uri uri = Uri.fromParts("package", this.getPackageName(), null);

                Dialog dialog;

                dialog = new Dialog(MainActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.back_press);

                dialog.show();

                TextView dialog_title = dialog.findViewById(R.id.dialog_title);
                TextView dialog_text = dialog.findViewById(R.id.dialog_text);

                Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
                Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

                dialog_title.setText("권한 요청");
                dialog_text.setText("앱을 사용하기 위해서는 파일 관리 권한을 허용해야 합니다");
                btn_confirm.setText("설정");

                dialog_title.setText("권한 요청");
                dialog_text.setText("앱을 사용하기 위해서는 파일 관리 권한을 허용해야 합니다");
                btn_confirm.setText("설정");

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finish();
                    }
                });

                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });


            }
        }

    }
}