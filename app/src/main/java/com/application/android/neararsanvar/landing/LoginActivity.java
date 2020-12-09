package com.application.android.neararsanvar.landing;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import com.application.android.neararsanvar.common.activities.BaseActivity;
import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.home.activities.HomeActivity;

public class LoginActivity extends BaseActivity {

    /* Views */
    EditText usernameTxt;
    EditText passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide Status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Init views
        usernameTxt = findViewById(R.id.usernameTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        usernameTxt.setTypeface(Configs.titSemibold);
        passwordTxt.setTypeface(Configs.titSemibold);

        TextView lTitleTxt = findViewById(R.id.loginTitleTxt);
        lTitleTxt.setTypeface(Configs.qsBold);

        // MARK: - LOGIN BUTTON ------------------------------------------------------------------
        Button loginButt = findViewById(R.id.loginButt);
        loginButt.setTypeface(Configs.titSemibold);
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoading(LoginActivity.this);
                //Configs.showPD(getString(R.string.progress_dialog_loading), LoginActivity.this);

                ParseUser.logInInBackground(usernameTxt.getText().toString(), passwordTxt.getText().toString(),
                        new LogInCallback() {
                            public void done(ParseUser user, ParseException error) {
                                if (user != null) {
                                    hideLoading();
                                    //Configs.hidePD();
                                    Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(homeIntent);
                                } else {
                                    hideLoading();
                                    //Configs.hidePD();
                                    Configs.simpleAlert(error.getMessage(), LoginActivity.this);
                                }
                            }
                        });
            }
        });

        // MARK: - SIGN UP BUTTON ------------------------------------------------------------------
        TextView signupButt = findViewById(R.id.signUpButt);
        signupButt.setTypeface(Configs.titSemibold);
        signupButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUp.class));
            }
        });

        // MARK: - FORGOT PASSWORD BUTTON ------------------------------------------------------------------
        TextView fpButt = findViewById(R.id.forgotPassButt);
        fpButt.setTypeface(Configs.titSemibold);
        fpButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle(R.string.app_name);
                alert.setMessage(R.string.login_forgot_password_alert);

                // Add an EditTxt
                final EditText editTxt = new EditText(LoginActivity.this);
                editTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                alert.setView(editTxt)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                // Reset password
                                ParseUser.requestPasswordResetInBackground(editTxt.getText().toString(), new RequestPasswordResetCallback() {
                                    public void done(ParseException error) {
                                        if (error == null) {
                                            Configs.simpleAlert(getString(R.string.login_forgot_password_success), LoginActivity.this);
                                        } else {
                                            Configs.simpleAlert(error.getMessage(), LoginActivity.this);
                                        }
                                    }
                                });
                            }
                        });
                alert.show();

            }
        });

        // MARK: - DISMISS BUTTON ------------------------------------
        Button dismButt = findViewById(R.id.loginDismissButt);
        dismButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

