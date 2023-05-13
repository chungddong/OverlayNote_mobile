package com.sophra.overlaynote_mobile;

import android.util.Log;

import com.rtfparserkit.parser.RtfListenerAdaptor;
import com.rtfparserkit.rtf.Command;

public class MyRtfListener extends RtfListenerAdaptor {

    private String textbuilder;

    public MyRtfListener() {
        super();
    }

    @Override
    public void processDocumentStart() {
        super.processDocumentStart();
    }

    @Override
    public void processDocumentEnd() {
        super.processDocumentEnd();
    }

    @Override
    public void processGroupStart() {
        super.processGroupStart();
    }

    @Override
    public void processGroupEnd() {
        super.processGroupEnd();
    }

    @Override
    public void processCharacterBytes(byte[] data) {

    }

    @Override
    public void processBinaryBytes(byte[] data) {
        super.processBinaryBytes(data);
    }

    @Override
    public void processString(String string) {
        //Log.d("log_overlay", "text : " + string);
        if(string.contains("Riched"))
        {
            textbuilder += string + "/";
        }
        else
        {
            textbuilder += string;
        }

    }

    @Override
    public void processCommand(Command command, int parameter, boolean hasParameter, boolean optional) {
        super.processCommand(command, parameter, hasParameter, optional);
    }

    public String getText()
    {
        return textbuilder;
    }
}
