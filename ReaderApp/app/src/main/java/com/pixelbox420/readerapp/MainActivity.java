package com.pixelbox420.readerapp;

import static java.lang.Integer.parseInt;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    //(Dear reader: Jobs would prolly ask "Was this (not so inclined with simplicity) code molested by the devil!!???)
    String result="";
    String resultnow="";
    String[] parts;
    int indexer=0;
    int ii=0;
    int nn=0;
    int playbackSpeed=800;
    PdfReader reader;
    private TextView extractedTV;
    private ImageButton extractButton;
    private ImageButton backButton;
    public ImageButton settingsButton;
    public TextView pdfPickText;
    public boolean changeSwitch=false;
    EditText tvProgressLabel;
    SeekBar seekBar;
    SeekBar seekBar2;
    int barProgress=50;
    int barProgress2=50;
    int pagetextlength=1;
    public Button saveSettingsButton;
    ImageView inapplogo01;
    ImageView backslides01;

    public ImageView verticalBarCenter;

    public TextView settingsUIFontText;
    public EditText settingsUIFontInput;

    int speedBarNegSkew=0; //we want to enable speed travel backwards

    public String settingsFontSetting;
    public String settingsColorSetting;
    public String settingsColorSetting2;


    public boolean settingsPressed=false;

    boolean paused=false;
    public int defaultColorR=138;
    public int defaultColorG=138;
    public int defaultColorB=138;
    public int selectedColorR=138;
    public int selectedColorG=138;
    public int selectedColorB=138;
    public int selectedColorRGB=-1979711488;

    public int defaultColorR2=255;
    public int defaultColorG2=255;
    public int defaultColorB2=255;
    public int selectedColorR2=255;
    public int selectedColorG2=255;
    public int selectedColorB2=255;
    public int selectedColorRGB2=-1979711488;

    View someView;
    View rootViewNow;

    public EditText speedInputField;
    public EditText pageInputField;
    public ImageButton runInputFields;
    public ImageView runInputFields2;
    public ImageButton pauseAndStart;

    public View canvas1;

    public TextView settingsUIBackColorText;
    public TextView settingsUIReadColorText;
    public TextView settingsUIColButtonBack;
    public TextView settingsUIColButtonText;
    public TextView settingsUIHeader;

    public TextView settingsAppDev;
    public ImageButton settingsUIShowInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set a change listener on the SeekBar
        seekBar = findViewById(R.id.seekBar);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar2.setMax(4000);
        seekBar2.setProgress(3500);

        settingsAppDev=findViewById(R.id.SettingsAppDev);
        settingsUIShowInfo=findViewById(R.id.SettingsUIShowInfo);
        settingsAppDev.setVisibility(View.INVISIBLE);
        settingsUIShowInfo.setVisibility(View.INVISIBLE);



        settingsUIBackColorText=findViewById(R.id.SettingsUIBackColorText);
        settingsUIReadColorText=findViewById(R.id.SettingsUIReadColorText);
        settingsUIColButtonBack=findViewById(R.id.SettingsUIColButtonBack);
        settingsUIColButtonText=findViewById(R.id.SettingsUIColButtonText);
        settingsUIBackColorText.setVisibility(View.INVISIBLE);
        settingsUIReadColorText.setVisibility(View.INVISIBLE);
        settingsUIColButtonBack.setVisibility(View.INVISIBLE);
        settingsUIColButtonText.setVisibility(View.INVISIBLE);




        saveSettingsButton=findViewById(R.id.saveSettingsEdits);

        barProgress = seekBar.getProgress();
        tvProgressLabel = findViewById(R.id.pageInputBox);
        tvProgressLabel.setText("");
        extractedTV=findViewById(R.id.idTVText);
        extractButton=findViewById(R.id.idBtnExtractText);
        backButton=findViewById(R.id.idBtnBack);
        settingsButton=findViewById(R.id.idBtnSettings);
        pdfPickText=findViewById(R.id.pdfButtonText);
        backButton.setVisibility(View.INVISIBLE);
        extractedTV.setVisibility(View.INVISIBLE);
        verticalBarCenter=findViewById(R.id.VerticalBarCenter);
        settingsUIFontText=findViewById(R.id.SettingsUIFontText);
        settingsUIFontInput=findViewById(R.id.SettingsUIFontInput);

        canvas1=findViewById(R.id.Canvas);

        inapplogo01=findViewById(R.id.inapplogo01);
        backslides01=findViewById(R.id.backslides01);

        speedInputField=findViewById(R.id.speedInputBox);
        //Maximize simplicity for the user there are many things he doesn't need to know !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        speedInputField.setVisibility(View.INVISIBLE);

        //pageInputField=findViewById(R.id.pageInput);
        runInputFields=findViewById(R.id.runInput);
        runInputFields2=findViewById(R.id.runInput2);
        pauseAndStart=findViewById(R.id.pauseAndStart);
        pdfPickText.setVisibility(View.VISIBLE);

        settingsUIHeader=findViewById(R.id.SettingsUIHeader);
        settingsUIHeader.setVisibility(View.INVISIBLE);

        final ColorPicker cp = new ColorPicker(MainActivity.this, defaultColorR, defaultColorG, defaultColorB);
        final ColorPicker cp2 = new ColorPicker(MainActivity.this, defaultColorR2, defaultColorG2, defaultColorB2);

        //default text color argb 138testnow0testnow0testnow0
        //default back color argb 255testnow255testnow255testnow255
        //ie. we could have a function that resets values back to defaults

        verticalBarCenter.setVisibility(View.INVISIBLE);
        saveSettingsButton.setVisibility(View.INVISIBLE);
        settingsUIFontText.setVisibility(View.INVISIBLE);
        settingsUIFontInput.setVisibility(View.INVISIBLE);

        settingsUIShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settingsAppDev.getVisibility()==View.INVISIBLE){
                    settingsAppDev.setVisibility(View.VISIBLE);
                }
                else{
                    settingsAppDev.setVisibility(View.INVISIBLE);
                }
            }
        });

        pauseAndStart.setBackgroundResource(R.drawable.uipasuebutton01);

        settingsUIColButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Show color picker dialog */
                cp.show();

                /* On Click listener for the dialog, when the user select the color */
                Button okColor = (Button)cp.findViewById(R.id.okColorButton);

                okColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /* You can get single channel (value 0-255) */
                        selectedColorR = cp.getRed();
                        selectedColorG = cp.getGreen();
                        selectedColorB = cp.getBlue();

                        /* Or the android RGB Color (see the android Color class reference) */
                        //selectedColorRGB = cp.getColor();
                        selectedColorRGB = android.graphics.Color.argb(255, selectedColorR, selectedColorG, selectedColorB);
                        //System.out.print("color: "+selectedColorRGB);

                        cp.dismiss();
                    }
                });
            }
        });

        settingsUIColButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Show color picker dialog */
                cp2.show();

                /* On Click listener for the dialog, when the user select the color */
                Button okColor = (Button)cp2.findViewById(R.id.okColorButton);

                okColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /* You can get single channel (value 0-255) */
                        selectedColorR2 = cp2.getRed();
                        selectedColorG2 = cp2.getGreen();
                        selectedColorB2 = cp2.getBlue();

                        /* Or the android RGB Color (see the android Color class reference) */
                        //selectedColorRGB2 = cp2.getColor();
                        selectedColorRGB2 = android.graphics.Color.argb(255, selectedColorR2, selectedColorG2, selectedColorB2);
                        //System.out.print("color2: "+selectedColorRGB2);
                        cp2.dismiss();
                    }
                });
            }
        });

        pauseAndStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!paused) {
                    paused=true;
                    changeSwitch=true;
                    pauseAndStart.setBackgroundResource(R.drawable.uistartbutton01);
                    //playbackSpeed = 1000000000; //Fun way to pause applications part 1.
                }

                else{
                    paused=false;
                    changeSwitch=false;
                    pauseAndStart.setBackgroundResource(R.drawable.uipasuebutton01);
                    //playbackSpeed=300; //Fun peasant way to pause applications part 2.
                    getwords();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int barProgress, boolean fromUser) {
                // updated continuously as the user slides the thumb

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // called after the user finishes moving the SeekBar
                barProgress = seekBar.getProgress();


                changeSwitch=true;
                //handler.removeCallbacksAndMessages(null); could be helpful for removing callbackLoops;
                //int speedFun = Integer.parseInt(speedInputField.getText().toString());
                //int pageFun = barProgress;
                //playbackSpeed=speedFun;
                ii=barProgress; // haha this ii thing here is page number !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!;
                runPageChange2();
                tvProgressLabel.setText(Integer.toString(ii+1));

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int barProgress, boolean fromUser) {
                // updated continuously as the user slides the thumb

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // called after the user finishes moving the SeekBar
                barProgress2 = seekBar2.getProgress();

                speedInputField.setText(Integer.toString(bar2converter(barProgress2)));

                playbackSpeed=bar2converter(barProgress2);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsPressed=true;
                saveSettingsButton.setVisibility(View.VISIBLE);
                settingsUIFontInput.setVisibility(View.VISIBLE);
                settingsUIFontText.setVisibility(View.VISIBLE);

                backButton.setVisibility(View.VISIBLE);//PDF selected now hide buttons for amazing user experience !!!!!!!!!!
                extractButton.setVisibility(View.INVISIBLE);
                settingsButton.setVisibility(View.INVISIBLE);
                extractedTV.setVisibility(View.INVISIBLE);
                pdfPickText.setVisibility(View.INVISIBLE);

                pauseAndStart.setVisibility(View.INVISIBLE);

                seekBar.setVisibility(View.INVISIBLE);
                tvProgressLabel.setVisibility(View.INVISIBLE);
                runInputFields.setVisibility(View.INVISIBLE);

                seekBar2.setVisibility(View.INVISIBLE);
                speedInputField.setVisibility(View.INVISIBLE);
                runInputFields2.setVisibility(View.INVISIBLE);

                inapplogo01.setVisibility(View.INVISIBLE);
                backslides01.setVisibility(View.INVISIBLE);

                settingsUIBackColorText.setVisibility(View.VISIBLE);
                settingsUIReadColorText.setVisibility(View.VISIBLE);
                settingsUIColButtonBack.setVisibility(View.VISIBLE);
                settingsUIColButtonText.setVisibility(View.VISIBLE);
                settingsUIHeader.setVisibility(View.VISIBLE);

                settingsAppDev.setVisibility(View.INVISIBLE);
                settingsUIShowInfo.setVisibility(View.VISIBLE);
            }
        });
        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settingsUIFontInput!=null) {
                    try {
                        int SettingsUIfontInput = parseInt(settingsUIFontInput.getText().toString());
                        settingsScreen(SettingsUIfontInput, "font");
                    } catch (Exception e) {}
                }
                    try{
                        int SettingsUIColorInput = selectedColorRGB;
                        settingsScreen(SettingsUIColorInput, "color");
                    } catch (Exception e) {}
                    try{
                        int SettingsUIColorInput2 = selectedColorRGB2;
                        settingsScreen(SettingsUIColorInput2, "color2");
                    } catch (Exception e) {}

                try{
                    backButton.performClick();
                }
                catch (Exception e){

                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSwitch=true;
                if(settingsPressed){
                    settingsPressed=false;
                }
                verticalBarCenter.setVisibility(View.INVISIBLE);
                saveSettingsButton.setVisibility(View.INVISIBLE);
                settingsUIFontInput.setVisibility(View.INVISIBLE);
                settingsUIFontText.setVisibility(View.INVISIBLE);
                settingsAppDev.setVisibility(View.INVISIBLE);
                settingsUIShowInfo.setVisibility(View.INVISIBLE);

                backButton.setVisibility(View.INVISIBLE);//PDF selected now hide buttons for amazing user experience !!!!!!!!!!
                extractedTV.setVisibility(View.INVISIBLE);
                pdfPickText.setVisibility(View.VISIBLE);
                extractButton.setVisibility(View.VISIBLE);
                settingsButton.setVisibility(View.VISIBLE);
                if(settingsPressed) {
                    pdfPickText.setVisibility(View.INVISIBLE);
                    extractButton.setVisibility(View.INVISIBLE);
                    settingsButton.setVisibility(View.INVISIBLE);
                }
                pauseAndStart.setVisibility(View.INVISIBLE);

                seekBar.setVisibility(View.INVISIBLE);
                tvProgressLabel.setVisibility(View.INVISIBLE);
                runInputFields.setVisibility(View.INVISIBLE);

                seekBar2.setVisibility(View.INVISIBLE);
                speedInputField.setVisibility(View.INVISIBLE);
                runInputFields2.setVisibility(View.INVISIBLE);

                inapplogo01.setVisibility(View.VISIBLE);
                backslides01.setVisibility(View.VISIBLE);

                settingsUIBackColorText.setVisibility(View.INVISIBLE);
                settingsUIReadColorText.setVisibility(View.INVISIBLE);
                settingsUIColButtonBack.setVisibility(View.INVISIBLE);
                settingsUIColButtonText.setVisibility(View.INVISIBLE);
                settingsUIHeader.setVisibility(View.INVISIBLE);





            }
        });

        runInputFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSwitch=true;
                //handler.removeCallbacksAndMessages(null); could be helpful for removing callbackLoops;
                //int speedFun = Integer.parseInt(speedInputField.getText().toString());
                int pageFun = parseInt(tvProgressLabel.getText().toString())-1;
                //playbackSpeed=speedFun;
                ii=pageFun; // haha this ii thing here is page number !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!;
                runPageChange2();
            }
        });
        //user wants to use enter instead press button programatically
        tvProgressLabel.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try{
                        runInputFields.performClick();
                    }
                    catch (Exception e){}
                    // Perform action on key press
                    //Toast.makeText(HelloFormStuff.this, edittext.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        runInputFields2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeSwitch=true;
                //handler.removeCallbacksAndMessages(null); could be helpful for removing callbackLoops;
                int speedFun = parseInt(speedInputField.getText().toString());
                //int pageFun = Integer.parseInt(tvProgressLabel.getText().toString());
                //playbackSpeed=speedFun;
                //ii=pageFun-1; // haha this ii thing here is page number !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!;
                //runPageChange();
                //playbackSpeed=bar2counterconverter(speedFun);
                //System.out.println("Result out: "+playbackSpeed);
                //barProgress2 = playbackSpeed;
                //bar2converter();
                //speedInputField.setText(Integer.toString(bar2converter(barProgress2)));
                //playbackSpeed=bar2counterconverter(speedFun);
                //speedInputField.setText("");
                speedInputField.setText(Integer.toString(bar2counterconverter(speedFun)));
                setProgress(bar2counterconverter(speedFun));
                playbackSpeed = bar2converter(seekBar2.getProgress());
            }
        });

        extractButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openFileDialog(view);
            }
        });



        pauseAndStart.setVisibility(View.INVISIBLE);

        seekBar.setVisibility(View.INVISIBLE);
        tvProgressLabel.setVisibility(View.INVISIBLE);
        runInputFields.setVisibility(View.INVISIBLE);

        seekBar2.setVisibility(View.INVISIBLE);
        speedInputField.setVisibility(View.INVISIBLE);
        runInputFields2.setVisibility(View.INVISIBLE);
    }
    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        //Context contextnow = getApplicationContext();
                        System.out.println(uri.getPath()+"lol");
                        //extractTextFromPDF(uri);
                        //String pathstring = createCopyAndReturnRealPath(contextnow,uri);
                        extractTextFromPDF(uri);
                    }
                }
            }
    );
    @Nullable
    public static String createCopyAndReturnRealPath(
            @NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        // Create file path inside app's data dir
        String filePath = context.getApplicationInfo().dataDir + File.separator
                + System.currentTimeMillis();

        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;

            OutputStream outputStream = new FileOutputStream(file); //might need reimport output stream wich one is it ? Google is very very gay for this !!!
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);

            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }

        return file.getAbsolutePath();
    }
    public void openFileDialog(View view){
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.setType("application/pdf"); //"*/*" for all
        data = Intent.createChooser(data, "choose a file");
        sActivityResultLauncher.launch(data);

    }

    public int bar2converter(int funGotValue){ // try to covert speed so that bar middle is slow edge is fast but cut before 0ms: y=-x^2+2000
        int newBarSpeedResult2=funGotValue-2000;
        if(newBarSpeedResult2>=0){
             newBarSpeedResult2=((newBarSpeedResult2*newBarSpeedResult2)/2000)-2*newBarSpeedResult2+2000;//(x^2)/2000-2x+2000 //Math is beautiful!!!!!!!!!!!
        }//when text moves slow the slide effect is fast when text moves fast the slider effect is slow this is super important for good user experience hopefully steeper than (x^2)/2000+-2x+2000 loops should go backwards if detected minus sign !!!!!!!!!!!
        else{
             newBarSpeedResult2=(newBarSpeedResult2*newBarSpeedResult2)/2000+2*newBarSpeedResult2+2000;
             newBarSpeedResult2=-newBarSpeedResult2;
        }
        return Math.round(newBarSpeedResult2);
    }
    public int bar2counterconverter(int funGotValue){ // try to covert speed so that bar middle is slow edge is fast but cut before 0ms: y=-x^2+2000
        int returnValue = (int) (-Math.sqrt(2000*funGotValue)+2000);
        return returnValue;
    }

    public void settingsScreen(int SettingsUIfontInput, String type){

        // Ohh ooohhhhhhh stack overflow cool save method for simple string and type pair YYYYAAAASSSSS  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        /*SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("homeScore", YOUR_HOME_SCORE);

// Apply the edits!
        editor.apply();

// Get from the SharedPreferences
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        int homeScore = settings.getInt("homeScore", 0);*/

        ///////////////////
if(type=="font") {
    SharedPreferences settings = getApplicationContext().getSharedPreferences(settingsFontSetting, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putInt("FontSize", SettingsUIfontInput);

// Apply the edits!
    editor.apply();
}
        else if(type=="color") {
            SharedPreferences settings2 = getApplicationContext().getSharedPreferences(settingsColorSetting, 0);
            SharedPreferences.Editor editor2 = settings2.edit();
            editor2.putInt("ColorText", SettingsUIfontInput);

// Apply the edits!
            editor2.apply();
        }
else if(type=="color2") {
    SharedPreferences settings3 = getApplicationContext().getSharedPreferences(settingsColorSetting2, 0);
    SharedPreferences.Editor editor3 = settings3.edit();
    editor3.putInt("ColorTheme", SettingsUIfontInput);

// Apply the edits!
    editor3.apply();
}
// Get from the SharedPreferences
        //SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        //int homeScore = settings.getInt("homeScore", 0);*/
    }


public void switchBlocker(){ //function to block multiple loops when user wants a new page or speedCount //Ie. change detected old loop runs to switch and flips it !!!!!!!!!!!!!!!!!!!
        if(changeSwitch){
            changeSwitch=false;
        }
        else {
            getwords();
        }
}

    public void getwords() {
        try {
            resultnow = PdfTextExtractor.getTextFromPage(reader, ii).trim() + "\n";
            parts = resultnow.split("\\s+");
            pagetextlength = parts.length;
        }
        catch(Exception E){

        }
        //last word last page -> book complete
        if(indexer>=pagetextlength && ii==nn){
            reader.close();
        }
        //first word page >0 and going backwards
        else if(indexer==0 && playbackSpeed<0 &&ii>0){
            ii--;
            try {


                //indexer = 0;
                resultnow = PdfTextExtractor.getTextFromPage(reader, ii).trim() + "\n";
                parts = resultnow.split("\\s+");
                indexer = parts.length;
                seekBar.setProgress(ii);
                barProgress=seekBar.getProgress();
                tvProgressLabel.setText(Integer.toString(barProgress+1));

                    indexer--;


                (new Handler()).postDelayed(this::switchBlocker, -playbackSpeed);
            }
            catch(Exception e){
                e.printStackTrace();
                extractedTV.setText("gg PDF");
            }
        }
        //last word page >0 and going forwards
        else if(indexer>=pagetextlength && ii>=0 &&playbackSpeed>0){
            ii++;
            runPageChange();
        }                                //else if pages max indexer max
        //move forward/backwards/forwards
        else{
            if(playbackSpeed>=0) {
                parts = resultnow.split("\\s+");
                if(tvProgressLabel.getText().toString().equals("2") && changeSwitch){
                    runInputFields.performClick();
                }

                if(indexer < parts.length) {
                    result = parts[indexer];
                }
                else{
                    ii++;
                    indexer=0;
                    runPageChange();
                }
                extractedTV.setText(result);
                (new Handler()).postDelayed(this::switchBlocker, Math.round(playbackSpeed * (1.0 + 0.1 * result.length())));
                indexer++;
            }

                /*try {
                    resultnow = PdfTextExtractor.getTextFromPage(reader, ii).trim() + "\n";
                } catch (IOException e) {
                    e.printStackTrace();
                }
                parts = resultnow.split("\\s+");
                indexer = parts.length;
                result = parts[indexer];
                extractedTV.setText(result);
                (new Handler()).postDelayed(this::switchBlocker, Math.round(-playbackSpeed * (1.0 + 0.1 * result.length())));
*/


         else {
             try {
                 result = parts[indexer];
             }
             catch (Exception E){
                 if(playbackSpeed<0) {
                     ii--;
                 }
                 else{
                     ii++;
                 }
                 runPageChange();
                 indexer=parts.length-1;
                 result = parts[indexer];
                 if(playbackSpeed>=0 && (indexer==parts.length || indexer==parts.length-1)){
                     //changeSwitch=true;
                     //playbackSpeed=200;
                     //ii++;


                     //runPageChange();
                 }
             }
                    extractedTV.setText(result);
                    (new Handler()).postDelayed(this::switchBlocker, Math.round(-playbackSpeed * (1.0 + 0.1 * result.length())));
                    if(indexer==0){
                        ii--;

                        runPageChange();
                        indexer= parts.length;
                    }
                    else {
                        indexer--;
                    }

            }
        }
    }

    public void runPageChange(){
        try {
            indexer = 0;
            resultnow = PdfTextExtractor.getTextFromPage(reader, ii ).trim() + "\n";
            parts = resultnow.split("\\s+");

            seekBar.setProgress(ii);
            barProgress=seekBar.getProgress();
            tvProgressLabel.setText(Integer.toString(barProgress+1));

            (new Handler()).postDelayed(this::switchBlocker, playbackSpeed);
        }
        catch(Exception e){
            e.printStackTrace();
            extractedTV.setText("Faulty PDF");
        }
    }
    public void runPageChange2(){
        try {
            indexer = 0;
            resultnow = PdfTextExtractor.getTextFromPage(reader, ii).trim() + "\n";
            parts = resultnow.split("\\s+");

            seekBar.setProgress(ii);
            barProgress=seekBar.getProgress();
            tvProgressLabel.setText(Integer.toString(barProgress+1));

            (new Handler()).postDelayed(this::switchBlocker, playbackSpeed);
        }
        catch(Exception e){
            e.printStackTrace();
            extractedTV.setText("Faulty PDF");
        }
    }
    /*public void verifStartPauseState(){
        if(changeSwitch=false){
            pauseAndStart.setBackgroundResource(R.drawable.uistartbutton01);
        }
        else{
            pauseAndStart.setBackgroundResource(R.drawable.uipasuebutton01);

        }
        (new Handler()).postDelayed(this::verifStartPauseState, 1000);
    }*/
    private void extractTextFromPDF(Uri sss){

        //System.out.println(Color.alpha(pdfPickText.getCurrentTextColor())+"testnow"+Color.red(pdfPickText.getCurrentTextColor())+"testnow"+Color.green(pdfPickText.getCurrentTextColor())+"testnow"+Color.blue(pdfPickText.getCurrentTextColor()));
        //System.out.println("testnow2: "+pdfPickText.getCurrentTextColor());
        //System.out.println("testnow3: "+this.getWindow().getDecorView().getSolidColor();

        verticalBarCenter.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);//PDF selected now hide buttons for amazing user experience !!!!!!!!!!
        extractButton.setVisibility(View.INVISIBLE);
        settingsButton.setVisibility(View.INVISIBLE);
        extractedTV.setVisibility(View.VISIBLE);
        pdfPickText.setVisibility(View.INVISIBLE);
        settingsUIFontInput.setVisibility(View.INVISIBLE);
        settingsUIFontText.setVisibility(View.INVISIBLE);
        settingsAppDev.setVisibility(View.INVISIBLE);
        settingsUIShowInfo.setVisibility(View.INVISIBLE);

        try {
            // Ohh ooohhhhhhh stack overflow cool save method for simple string and type pair YYYYAAAASSSSS  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // Get from the SharedPreferences
            SharedPreferences settings = getApplicationContext().getSharedPreferences(settingsFontSetting, 0);
            int fontSettingInt = settings.getInt("FontSize", 0);
            //Setting font size according to user saved setting if it was indeed found
            extractedTV.setTextSize(fontSettingInt);
        }
        catch(Exception e){
            extractedTV.setTextSize(30);
        }
        try {
            // Get from the SharedPreferences
            SharedPreferences settings2 = getApplicationContext().getSharedPreferences(settingsColorSetting, 0);
            int ColorSettingInt = settings2.getInt("ColorText", 0);
            //Setting font size according to user saved setting if it was indeed found
            extractedTV.setTextColor(ColorSettingInt);
            System.out.println("ColorSettingNow: " + "#" + ColorSettingInt);
        }
        catch(Exception e){

        }

        try{
            // Get from the SharedPreferences
            SharedPreferences settings3 = getApplicationContext().getSharedPreferences(settingsColorSetting2, 0);
            int ColorSettingInt2 = settings3.getInt("ColorTheme", 0);
            //Setting font size according to user saved setting if it was indeed found
            this.getWindow().getDecorView().setBackgroundColor(ColorSettingInt2);
        }
        catch(Exception e){

        }


        pauseAndStart.setVisibility(View.VISIBLE);

        seekBar.setVisibility(View.VISIBLE);
        tvProgressLabel.setVisibility(View.VISIBLE);
        runInputFields.setVisibility(View.VISIBLE);

        seekBar2.setVisibility(View.VISIBLE);
        speedInputField.setVisibility(View.INVISIBLE);
        runInputFields2.setVisibility(View.VISIBLE);

        inapplogo01.setVisibility(View.INVISIBLE);
        backslides01.setVisibility(View.INVISIBLE);
        try{
            Context contextnow = getApplicationContext();

            String pathstring = createCopyAndReturnRealPath(contextnow,sss);


            //File f = new File(/Download/dummy.pdf");
            reader=new PdfReader(pathstring);
            System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath()+" lol");
                     nn = reader.getNumberOfPages();
            //for(ii=0;ii<nn;ii++) {
                //result = result + PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n";
                //extractedTV.setText(result);
                //reader.close();
                resultnow = PdfTextExtractor.getTextFromPage(reader, ii + 1).trim() + "\n";
                parts = resultnow.split("\\s+");
                pagetextlength = parts.length;
                seekBar.setMax(nn-1);

                (new Handler()).postDelayed(this::switchBlocker, playbackSpeed);

                //Another for verifying start/pause button state every second
            //(new Handler()).postDelayed(this::verifStartPauseState, 1000);
                //}


            }
            catch(Exception e){
                e.printStackTrace();
                extractedTV.setText("Many people have experienced the same error because of incorrect pdf."+e.getMessage()+Environment.getExternalStorageDirectory().getAbsolutePath());
           //Robert Cialdini principles secured at (used to be) line 420 funding secured !!!!!!!!!  Funding secured, Obv !!!!!!!!!!!!!!!!!!!!
           }
        }
    }
