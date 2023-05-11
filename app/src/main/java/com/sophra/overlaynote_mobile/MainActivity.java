package com.sophra.overlaynote_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

                Log.d("log_overlay", "권한 허용됨");
                mylist = new ArrayList();
                String rootSD = Environment.getExternalStorageDirectory().toString();

                Log.d("log_overlay", "rootSD : " + rootSD);
                file = new File(rootSD + "/Download/OverlayNote/notedata.csv");


                List<String[]> csvDataList = new ArrayList<>();

                CSVReader reader = null;
                try {
                    reader = new CSVReader(new FileReader(file.getPath()));
                    String[] line;

                    while ((line = reader.readNext()) != null) {
                        // CSV 데이터 처리
                        // line 배열에 CSV 한 줄의 데이터가 저장됩니다.
                        // 리스트에 데이터 추가
                        csvDataList.add(line);
                    }
                    reader.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CsvException e) {
                    e.printStackTrace();
                }


                String[] firstRow = csvDataList.get(0);
                //String line = String.join(", ", firstRow);

                Log.d("log_overlay", "data : " + firstRow[1]);

                /*for (String value : csvDataList.get(0)) {
                    System.out.print(value + " ");
                    Log.d("log_overlay", "data : " + value);
                }*/


            } else { //권한이 허용 안되어있을 때

                Log.d("log_overlay", "권한 비허용");

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

    public void isdirectory() //경로상에 csv 파일이 있는지 확인하는 코드
    {

    }

    protected void saveState(){ // 데이터를 저장한다.
        SharedPreferences pref = getSharedPreferences("ovno", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //editor.putString("text", text);

        editor.commit();


    }

    protected void restoreState(){  // 데이터를 복구한다.
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if((pref!=null) && (pref.contains("text"))){
            //text = pref.getString("text", "");
        }

    }


}