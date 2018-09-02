package com.mojostudios.mojopay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    private TextView name;
    private TextView email;
    private TextView mobile;
    FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name=findViewById(R.id.tv_profile_name);
        email=findViewById(R.id.tv_profile_email);
        mobile=findViewById(R.id.tv_profile_mn);
        name.setText(user.getDisplayName());
        if(user.getEmail()!=null){
            email.setText(user.getEmail());
        }
        if(user.getPhoneNumber()!=null){
            mobile.setText(user.getPhoneNumber());
        }


    }

}
