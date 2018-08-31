package com.mojostudios.mojopay;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.*;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.mojostudios.mojopay.R;

import static java.lang.Integer.parseInt;

public class MainActivity extends Activity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private PersonsListDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        dbHelper = new PersonsListDBHelper(this);

        // hide the action bar
        //getActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    String input = result.get(0);
                    if (Pattern.matches("show list", input)) {
                        Intent intent = new Intent(this, PersonsList.class);
                        startActivity(intent);
                    } else if (Pattern.matches("add [0-9]+ [a-zA-Z]+", input)) {
                        String delimiter =  "\\s";
                        Pattern pattern = Pattern.compile(delimiter, Pattern.CASE_INSENSITIVE);
                        String[] array = pattern.split(input);
                        List<Person> list=dbHelper.personList("");
                        int flag=0;
                        for(Person temp:list){
                            if(array[2].equals(temp.getName())){
                                int currentCredit=temp.getCredit();
                                temp.setCredit(currentCredit+parseInt(array[1]));
                                temp.setTotal(temp.getCredit()-temp.getDebit());
                                dbHelper.updatePersonRecord(temp.getID(),this,temp);
                                flag=1;
                            }
                        }
                        if(flag==0){
                            Person temp = new Person();
                            temp.setName(array[2]);
                            temp.setCredit(parseInt(array[1]));
                            temp.setDebit(0);
                            temp.setTotal(temp.getCredit()+temp.getDebit());
                            temp.setImage("a");
                            Log.d("saveNewPerson","outside true");
                            dbHelper.saveNewPerson(temp);
                        }
                        Toast.makeText(this, "add func Working", Toast.LENGTH_SHORT).show();

                    } else if (Pattern.matches("subtract [0-9]+ [a-zA-Z]+", input)) {
                        String delimiter =  "\\s";
                        Pattern pattern = Pattern.compile(delimiter, Pattern.CASE_INSENSITIVE);
                        String[] array = pattern.split(input);
                        List<Person> list=dbHelper.personList("");
                        int flag=0;
                        for(Person temp:list){
                            if(array[2].equals(temp.getName())){
                                int currentDebit=temp.getDebit();
                                temp.setDebit(currentDebit+parseInt(array[1]));
                                temp.setTotal(temp.getCredit()-temp.getDebit());
                                dbHelper.updatePersonRecord(temp.getID(),this,temp);
                                flag=1;
                            }
                        }
                        if(flag==0){
                            Person temp=new Person();
                            temp.setName(array[2]);
                            temp.setDebit(parseInt(array[1]));
                            temp.setCredit(0);
                            temp.setTotal(temp.getCredit()-temp.getDebit());
                            temp.setImage("a");
                            dbHelper.saveNewPerson(temp);
                        }
                        Toast.makeText(this, "subtract func Working", Toast.LENGTH_SHORT).show();
                    } else if (Pattern.matches("show [a-zA-Z]+", input)) {
                        Toast.makeText(this, "show func Working", Toast.LENGTH_SHORT).show();
                    } else if (Pattern.matches("delete [a-zA-Z]+", input)) {

                        String delimiter =  "\\s";
                        Pattern pattern = Pattern.compile(delimiter, Pattern.CASE_INSENSITIVE);
                        String[] array = pattern.split(input);
                        List<Person> list=dbHelper.personList("");
                        int flag=0;
                        for(Person temp:list){
                            if(array[1].equals(temp.getName())){
                                dbHelper.deletePersonRecord(temp.getID(),this);
                                flag=1;
                            }
                        }
                        if(flag==0){
                            Toast.makeText(this,"This account does't exist",Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(this, "delete func Working", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}