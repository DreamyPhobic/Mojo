package com.mojostudios.mojopay;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private int RC_SIGN_IN = 123;
    void showSnackbar(int id){
        Snackbar.make(findViewById(R.id.splash_activity_container), getResources().getString(id), Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView = (ImageView) findViewById(R.id.imgLogo);
        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.blink);
        imageView.startAnimation(myAnim);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                // This method will be executed once the timer is over
                // Start your app main activity
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build());

                if (auth.getCurrentUser() != null) { //If user is signed in
                    Intent intentMain=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intentMain);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                                    .setAvailableProviders(providers)
                                    .setLogo(R.drawable.app_name)
                                    .setTheme(R.style.AppTheme_NoActionBar)
                                    .setTosUrl("link to app terms and service")
                                    .setPrivacyPolicyUrl("link to app privacy policy")
                                    .build(),
                            RC_SIGN_IN);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    
                }
                // close this activity
            }
        }, SPLASH_TIME_OUT);
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
                Intent intentMain=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intentMain);
                finish();
                return;
            }
            else {
                if(response == null){
                    //If no response from the Server
                    showSnackbar(R.string.sign_in_cancelled);
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
