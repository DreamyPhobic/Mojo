package com.mojostudios.mojopay;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private int RC_SIGN_IN = 123;
    void showSnackbar(int id){
        Snackbar.make(findViewById(R.id.sign_in_container), getResources().getString(id), Snackbar.LENGTH_LONG).show();
    }
    public void openMainActivity(){
        Intent intentMain=new Intent(this,MainActivity.class);
        startActivity(intentMain);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        if (auth.getCurrentUser() != null) { //If user is signed in
            openMainActivity();
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.app_logo)
                            .setTheme(R.style.AppTheme)
                            .setTosUrl("link to app terms and service")
                            .setPrivacyPolicyUrl("link to app privacy policy")
                            .build(),
                    RC_SIGN_IN);
            finish();
        }

}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            /*
                this checks if the activity result we are getting is for the sign in
                as we can have more than activity to be started in our Activity.
             */
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == Activity.RESULT_OK){
                showSnackbar(R.string.signed_in);
                openMainActivity();
                return;
            }
            else {
                if(response == null){
                    //If no response from the Server
                    showSnackbar(R.string.sign_in_cancelled);
                    finish();
                    return;
                }
                if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                    //If there was a network problem the user's phone
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }
                if(response.getError().getErrorCode()== ErrorCodes.UNKNOWN_ERROR){
                    //If the error cause was unknown
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }
        }
        showSnackbar(R.string.unknown_sign_in_response);//if the sign in response was unknown
    }
}
