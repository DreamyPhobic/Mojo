package com.mojostudios.mojopay;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static java.lang.Integer.parseInt;


public class Page1Fragment extends Fragment {
    private Button add;
    private Button speechName;
    private Button speechAmount;
    private EditText name;
    private EditText amount;
    private PersonsListDBHelper dbHelper;

    private final int REQ_CODE_SPEECH_NAME = 100;
    private final int REQ_CODE_SPEECH_AMOUNT =200;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_page1, container, false);
            add=(Button)rootView.findViewById(R.id.b_add);
            speechName=(Button) rootView.findViewById(R.id.b_speech_name);
            speechAmount=(Button)rootView.findViewById(R.id.b_speech_amount);
            name=(EditText)rootView.findViewById(R.id.et_name);
            amount=(EditText)rootView.findViewById(R.id.et_amount);
            dbHelper = new PersonsListDBHelper(getActivity());
            add.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String nameString=name.getText().toString().trim();
                    String amountString=amount.getText().toString();
                    try{
                        int resInt=Integer.valueOf(amountString);
                    }catch (Exception e){
                        Toast.makeText(getActivity(),"Please put a valid input.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Person> list=dbHelper.personList("");
                    int flag=0;
                    for(Person temp:list){
                        if(nameString.equals(temp.getName())){
                            int currentCredit=temp.getCredit();
                            temp.setCredit(currentCredit+parseInt(amountString));
                            temp.setTotal(temp.getCredit()-temp.getDebit());
                            dbHelper.updatePersonRecord(temp.getID(),getActivity(),temp);
                            flag=1;
                        }
                    }
                    if(flag==0){
                        Person temp = new Person();
                        temp.setName(nameString);
                        temp.setCredit(parseInt(amountString));
                        temp.setDebit(0);
                        temp.setTotal(temp.getCredit()+temp.getDebit());
                        temp.setImage("a");
                        Log.d("saveNewPerson","outside true");
                        dbHelper.saveNewPerson(temp);
                        Toast.makeText(getActivity(),"Added Successfully", Toast.LENGTH_SHORT).show();
                    }

                    name.setText("");
                    amount.setText("");
                }
            });
            speechName.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    promptSpeechInput("name");
                }
            });
            speechAmount.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    promptSpeechInput("amount");
                }
            });

            return rootView;
        }
    private void promptSpeechInput(String requestButton) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            if(requestButton.equals("name")){
                startActivityForResult(intent, REQ_CODE_SPEECH_NAME);
            }
            else if(requestButton.equals("amount")){
                startActivityForResult(intent, REQ_CODE_SPEECH_AMOUNT);
            }
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_NAME: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    name.setText(res.get(0));
                }
                break;
            }

            case REQ_CODE_SPEECH_AMOUNT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    try{
                        int resInt=Integer.valueOf(res.get(0));

                    } catch(Exception e){
                        Toast.makeText(getActivity(),"Please put a valid input.", Toast.LENGTH_SHORT).show();
                    }
                    amount.setText(res.get(0));
                }
                break;
            }


        }
    }

}

