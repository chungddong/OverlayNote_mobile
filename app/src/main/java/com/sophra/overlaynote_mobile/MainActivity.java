package com.sophra.overlaynote_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.os.Debug;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.rtfparserkit.parser.IRtfParser;
import com.rtfparserkit.parser.IRtfSource;
import com.rtfparserkit.parser.RtfStreamSource;
import com.rtfparserkit.parser.standard.StandardRtfParser;

import org.apache.commons.io.FileUtils;
import org.jodconverter.LocalConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private File file;
    private List mylist;
    private final int PERMISSIONS_REQUEST_RESULT = 1;

    public static String filename;
    public static String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        filename = "";
        color = "#FFFFFF";

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


                String[] firstRow = csvDataList.get(0); //0번째 라인
                //String line = String.join(", ", firstRow);

                Log.d("log_overlay", "data(name) : " + firstRow[0]); //이름
                //Log.d("log_overlay", "data : " + firstRow[1]); //경로 - 컴퓨터경로라 그냥 나중에 옮길때만 사용
                //Log.d("log_overlay", "data : " + firstRow[2]); //색상

                file = new File(rootSD + "/Download/OverlayNote/" + "깃 명령어" + ".rtf");
                Log.d("log_overlay", "rootSD : " + file.getPath());
                Log.d("log_overlay", "길이 : " + csvDataList.size());

                RecyclerView recyclerView = findViewById(R.id.recyclerview_t);

                ArrayList<String> data = new ArrayList<>();
                ArrayList<String> colordata = new ArrayList<>();


                InputStream is = null;
                try {
                    is = new FileInputStream(file.getPath());
                    IRtfSource source = new RtfStreamSource(is);
                    IRtfParser parser = new StandardRtfParser();
                    MyRtfListener listener = new MyRtfListener();
                    parser.parse(source, listener);

                    Log.d("log_overlay", "text : " + listener.getText());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                for(int i = 0; i < csvDataList.size(); i++)
                {
                    data.add(csvDataList.get(i)[0]);
                    colordata.add(csvDataList.get(i)[2]);
                }
                //data.add("일식");

                CustomAdapter customAdapter = new CustomAdapter(data, colordata);
                recyclerView.setAdapter(customAdapter); // 어댑터 설정

                GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);


                recyclerView.setLayoutManager(gridLayoutManager);
                customAdapter.notifyDataSetChanged();

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

    private String convertRtfToText(String rtfFilePath) {
        try {
            FileInputStream inputStream = new FileInputStream(rtfFilePath);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();

            String rtfText = stringBuilder.toString();
            return Html.fromHtml(rtfText).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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