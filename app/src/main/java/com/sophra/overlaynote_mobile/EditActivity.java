package com.sophra.overlaynote_mobile;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.onegravity.rteditor.RTEditText;
import com.onegravity.rteditor.RTManager;
import com.onegravity.rteditor.RTToolbar;
import com.onegravity.rteditor.api.RTApi;
import com.onegravity.rteditor.api.RTMediaFactoryImpl;
import com.onegravity.rteditor.api.RTProxyImpl;
import com.onegravity.rteditor.api.format.RTFormat;
import com.onegravity.rteditor.converter.ConverterHtmlToText;
import com.rtfparserkit.parser.IRtfParser;
import com.rtfparserkit.parser.IRtfSource;
import com.rtfparserkit.parser.RtfStreamSource;
import com.rtfparserkit.parser.standard.StandardRtfParser;
import com.sophra.overlaynote_mobile.rtf.RtfHtml;
import com.sophra.overlaynote_mobile.rtf.RtfParseException;
import com.sophra.overlaynote_mobile.rtf.RtfReader;

import org.bbottema.rtftohtml.RTF2HTMLConverter;
import org.bbottema.rtftohtml.impl.RTF2HTMLConverterClassic;
import org.bbottema.rtftohtml.impl.RTF2HTMLConverterJEditorPane;
import org.bbottema.rtftohtml.impl.RTF2HTMLConverterRFCCompliant;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class EditActivity extends AppCompatActivity {
    TextView edit_text;
    RelativeLayout mainlayout;
    RTEditText rtfeditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edit_text = findViewById(R.id.edit_text);
        mainlayout = findViewById(R.id.mainlayout);
        rtfeditor = findViewById(R.id.rtfeditors);

        edit_text.setText(MainActivity.filename);
        mainlayout.setBackgroundColor(Color.parseColor(MainActivity.color));

        // create RTManager
        RTApi rtApi = new RTApi(this, new RTProxyImpl(this), new RTMediaFactoryImpl(this, true));
        RTManager rtManager = new RTManager(rtApi, savedInstanceState);


        String content = "{\\rtf1\\ansi\\ansicpg949\\deff0\\nouicompat\\deflang1033\\deflangfe1042{\\fonttbl{\\f0\\fswiss\\fcharset129 \\'b8\\'bc\\'c0\\'ba \\'b0\\'ed\\'b5\\'f1;}{\\f1\\fswiss\\fcharset0 Calibri;}{\\f2\\fnil\\fcharset129 \\'b8\\'bc\\'c0\\'ba \\'b0\\'ed\\'b5\\'f1;}}\n" +
                "{\\colortbl ;\\red0\\green0\\blue255;}\n" +
                "{\\*\\generator Riched20 10.0.22621}\\viewkind4\\uc1 \n" +
                "\\pard\\f0\\fs26\\lang1042\\'bb\\'ec\\'b0\\'d4\\'c0\\'d3 \\f1\\lang1033 - LOST IN PLAY\\par\n" +
                "\\par\n" +
                "-{{\\field{\\*\\fldinst{HYPERLINK https://gh402.tistory.com/38 }}{\\fldrslt{https://gh402.tistory.com/38\\ul0\\cf0}}}}\\f1\\fs26\\par\n" +
                "\\par\n" +
                "-{{\\field{\\*\\fldinst{HYPERLINK https://tech.kakaoenterprise.com/121 }}{\\fldrslt{https://tech.kakaoenterprise.com/121\\ul0\\cf0}}}}\\f1\\fs26\\par\n" +
                "\\par\n" +
                "- RPC\\f0\\lang1042\\'b6\\'f5\\f1\\lang1033 ?\\par\n" +
                "\\par";



        rtfeditor = findViewById(R.id.rtfeditors);
        rtManager.registerEditor(rtfeditor, true);
        rtfeditor.setRichTextEditing(true, "dfsdf");
        String text = rtfeditor.getText(RTFormat.HTML);

        Log.d("log_overlay", "rtfformat : " + text);


        String rootSD = Environment.getExternalStorageDirectory().toString();
        File file = new File(rootSD + "/Download/OverlayNote/" + MainActivity.filename + ".rtf");

        StringBuilder stringBuilder;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
            stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            Log.d("log_overlay", "stringBuilder : " + stringBuilder);

            RTF2HTMLConverter converter = RTF2HTMLConverterRFCCompliant.INSTANCE;
            converter = RTF2HTMLConverterRFCCompliant.INSTANCE;


            String html = converter.rtf2html(String.valueOf(stringBuilder));
            Log.d("log_overlay", "html : " + html);


            rtfeditor.setText(html);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String rtf = "{\\rtf1\\ansi\\ansicpg1252\\cocoartf2511\n" +
                "\\cocoatextscaling0\\cocoaplatform0{\\fonttbl\\f0\\fswiss\\fcharset0 Helvetica;}\n" +
                "{\\colortbl;\\red255\\green255\\blue255;}\n" +
                "{\\*\\expandedcolortbl;;}\n" +
                "\\pard\\tx560\\tx1120\\tx1680\\tx2240\\tx2800\\tx3360\\tx3920\\tx4480\\tx5040\\tx5600\\tx6160\\tx6720\\pardirnatural\\partightenfactor0\n" +
                "\n" +
                "\\f0\\fs24 \\cf0 This is an example RTF string.}";




    }
}
