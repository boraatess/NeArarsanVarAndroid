package com.application.android.neararsanvar.landing;

/*-------------------------------

    - Classifier -

    Created by CarbonCode Technology @2019
    All Rights reserved.

-------------------------------*/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.application.android.neararsanvar.common.activities.BaseActivity;
import com.application.android.neararsanvar.utils.Configs;
import com.application.android.neararsanvar.utils.MarshMallowPermission;
import com.application.android.neararsanvar.R;
import com.application.android.neararsanvar.home.activities.HomeActivity;

public class SignUp extends BaseActivity {

    /* Views */
    ImageView avatarImg;
    EditText usernameTxt, passwordTxt, fullnameTxt, emailTxt;


    /* Variables */
    MarshMallowPermission mmp = new MarshMallowPermission(this);
    boolean isAccepted = false;

    private int visibilityLayoutNo = 1;


    LinearLayout layout1, layout2, layout3, layout4, layout5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide Status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Init views
        avatarImg = findViewById(R.id.cadAvatarImg);
        usernameTxt = findViewById(R.id.usernameTxt2);
        passwordTxt = findViewById(R.id.passwordTxt2);
        fullnameTxt = findViewById(R.id.fullnameTxt);
        emailTxt = findViewById(R.id.emailTxt2);
        usernameTxt.setTypeface(Configs.titSemibold);
        passwordTxt.setTypeface(Configs.titSemibold);
        fullnameTxt.setTypeface(Configs.titSemibold);
        emailTxt.setTypeface(Configs.titSemibold);

        layout1 = findViewById(R.id.ll1);
        layout2 = findViewById(R.id.ll2);
        layout3 = findViewById(R.id.ll3);
        layout4 = findViewById(R.id.ll4);
        layout5 = findViewById(R.id.ll5);

        // MARK: - CAMERA BUTTON ----------------------------------------
        Button camButt = findViewById(R.id.signupCamButt);
        camButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mmp.checkPermissionForCamera()) {
                    mmp.requestPermissionForCamera();
                } else {
                    openCamera();
                }
            }
        });


        // MARK: - GALLERY BUTTON ----------------------------------------
        Button galButt = findViewById(R.id.signupGalleryButt);
        galButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mmp.checkPermissionForReadExternalStorage()) {
                    mmp.requestPermissionForReadExternalStorage();
                } else {
                    openGallery();
                }
            }
        });


        // MARK: - CHECKBOX BUTTON ------------------------------------
        final Button checkboxButt = findViewById(R.id.supCheckBoxButt);
        checkboxButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAccepted = true;
                checkboxButt.setBackgroundResource(R.drawable.checkbox_on);
            }
        });


        // SIGN UP BUTTON ------------------------------------------------------------------------
        final Button signupButt = findViewById(R.id.signUpButt2);
        signupButt.setTypeface(Configs.titSemibold);
        signupButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (visibilityLayoutNo == 1) {
                    if (showAlertDialog(usernameTxt)) {
                        visibilityGone();
                        layout2.setVisibility(View.VISIBLE);
                        signupButt.setBackgroundColor(Color.parseColor("#81D4FA"));
                        visibilityLayoutNo++;
                    }

                } else if (visibilityLayoutNo == 2) {
                    if (showAlertDialog(passwordTxt)) {
                        visibilityGone();
                        layout3.setVisibility(View.VISIBLE);
                        signupButt.setBackgroundColor(Color.parseColor("#2962FF"));
                        visibilityLayoutNo++;
                    }

                } else if (visibilityLayoutNo == 3) {
                    if (showAlertDialog(emailTxt)) {
                        visibilityGone();
                        layout4.setVisibility(View.VISIBLE);
                        layout5.setVisibility(View.VISIBLE);
                        signupButt.setBackgroundColor(Color.parseColor("#FFD54F"));
                        signupButt.setText("Sign up");
                        signupButt.setTextColor(Color.parseColor("#000000"));
                        visibilityLayoutNo++;
                    }

                } else if (visibilityLayoutNo == 4) {

                    if (isAccepted) {
                        if (showAlertDialog(fullnameTxt)) {

                            showLoading(SignUp.this);
                            //Configs.showPD(getString(R.string.progress_dialog_loading), SignUp.this);
                            dismisskeyboard();

                            ParseUser user = new ParseUser();
                            user.setUsername(usernameTxt.getText().toString());
                            user.setPassword(passwordTxt.getText().toString());
                            user.setEmail(emailTxt.getText().toString());
                            user.put(Configs.USER_FULLNAME, fullnameTxt.getText().toString());
                            user.put(Configs.USER_IS_REPORTED, false);
                            //user.put(Configs.USER_ABOUT_ME, "");
                            List<String> hasBlocked = new ArrayList<String>();
                            user.put(Configs.USER_HAS_BLOCKED, hasBlocked);

                            // Saving block
                            user.signUpInBackground(new SignUpCallback() {
                                public void done(ParseException error) {
                                    if (error == null) {

                                        // Now save avatar
                                        ParseUser user = ParseUser.getCurrentUser();
                                        Configs.saveParseImage(avatarImg, user, Configs.USER_AVATAR);

                                        user.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                //Configs.hidePD();
                                                Intent homeIntent = new Intent(SignUp.this, HomeActivity.class);
                                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(homeIntent);
                                            }
                                        });
                                    } else {
                                        hideLoading();
                                        //Configs.hidePD();
                                        Configs.simpleAlert(error.getMessage(), SignUp.this);
                                    }
                                }
                            });
                        }
                    } else {
                        Configs.simpleAlert(getString(R.string.sign_up_terms_warning), SignUp.this);
                    }
                }

               /* // USER HAS ACCEPTED THE TERMS OF SERVICE
                if (isAccepted) {

                    if (usernameTxt.getText().toString().matches("") || passwordTxt.getText().toString().matches("") ||
                            emailTxt.getText().toString().matches("") || fullnameTxt.getText().toString().matches("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setMessage(R.string.sign_up_validation_error)
                                .setTitle(R.string.app_name)
                                .setPositiveButton(R.string.alert_ok_button, null);
                        AlertDialog dialog = builder.create();
                        dialog.setIcon(R.drawable.ic_logo);
                        dialog.show();


                    } else {
                        Configs.showPD(getString(R.string.progress_dialog_loading), SignUp.this);
                        dismisskeyboard();

                        ParseUser user = new ParseUser();
                        user.setUsername(usernameTxt.getText().toString());
                        user.setPassword(passwordTxt.getText().toString());
                        user.setEmail(emailTxt.getText().toString());
                        user.put(Configs.USER_FULLNAME, fullnameTxt.getText().toString());
                        user.put(Configs.USER_IS_REPORTED, false);
                        List<String> hasBlocked = new ArrayList<String>();
                        user.put(Configs.USER_HAS_BLOCKED, hasBlocked);


                        // Saving block
                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException error) {
                                if (error == null) {

                                    // Now save avatar
                                    ParseUser user = ParseUser.getCurrentUser();
                                    Configs.saveParseImage(avatarImg, user, Configs.USER_AVATAR);

                                    user.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Configs.hidePD();
                                            Intent homeIntent = new Intent(SignUp.this, HomeActivity.class);
                                            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(homeIntent);
                                        }
                                    });


                                } else {
                                    Configs.hidePD();
                                    Configs.simpleAlert(error.getMessage(), SignUp.this);
                                }
                            }
                        });
                    }
*/

                // USER HAS NOT ACCEPTED THE TERMS OF SERVICE
                /*} else {
                    Configs.simpleAlert(getString(R.string.sign_up_terms_warning), SignUp.this);
                }*/

            }
        });


        // MARK: - TERMS OF SERVICE BUTTON ----------------------------------------------------------
        Button tosButt = findViewById(R.id.touButt);
        tosButt.setTypeface(Configs.titSemibold);
        tosButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, TermsOfUse.class));
            }
        });


        // MARK: - DISMISS BUTTON ---------------------------------------------------------------
        Button dismissButt = findViewById(R.id.signupDismissButt);
        dismissButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }// end onCreate()


    // IMAGE HANDLING METHODS ------------------------------------------------------------------------
    int CAMERA = 0;
    int GALLERY = 1;
    Uri imageURI;
    File file;


    // OPEN CAMERA
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        imageURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
        startActivityForResult(intent, CAMERA);
    }


    // OPEN GALLERY
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.gallery_chooser_title)), GALLERY);
    }


    // IMAGE PICKED DELEGATE -----------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Bitmap bm = null;

            // Image from Camera
            if (requestCode == CAMERA) {

                try {
                    File f = file;
                    ExifInterface exif = new ExifInterface(f.getPath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    int angle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        angle = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        angle = 180;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        angle = 270;
                    }
                    Log.i("log-", "ORIENTATION: " + orientation);

                    Matrix mat = new Matrix();
                    mat.postRotate(angle);

                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
                    bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                } catch (IOException | OutOfMemoryError e) {
                    Log.i("log-", e.getMessage());
                }


                // Image from Gallery
            } else if (requestCode == GALLERY) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            // Set image
            Bitmap scaledBm = Configs.scaleBitmapToMaxSize(300, bm);
            avatarImg.setImageBitmap(scaledBm);
        }
    }
    //---------------------------------------------------------------------------------------------


    // DISMISS KEYBOARD
    public void dismisskeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(fullnameTxt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(emailTxt.getWindowToken(), 0);
    }


    public void visibilityGone() {

        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
        layout4.setVisibility(View.GONE);
        layout5.setVisibility(View.GONE);
    }


    public boolean showAlertDialog(EditText view) {
        if (view.getText().toString().matches("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
            builder.setMessage(R.string.sign_up_validation_error)
                    .setTitle(R.string.app_name)
                    .setPositiveButton(R.string.alert_ok_button, null);
            AlertDialog dialog = builder.create();
            dialog.setIcon(R.drawable.neararsanvar);
            dialog.show();
            return false;
        } else {
            return true;
        }
    }
}//@end
