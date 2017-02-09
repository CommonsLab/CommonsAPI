package com.example.valdio.commonsapitest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import apiwrapper.commons.wikimedia.org.Commons;
import apiwrapper.commons.wikimedia.org.Enums.ContributionType;
import apiwrapper.commons.wikimedia.org.Interfaces.CaptchaCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.ContributionsCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.CreateAccountCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.LoginCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.LogoutCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.RSS_FeedCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.ResetPasswordCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.ThumbnailCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.UploadCallback;
import apiwrapper.commons.wikimedia.org.Models.Captcha;
import apiwrapper.commons.wikimedia.org.Models.Contribution;
import apiwrapper.commons.wikimedia.org.Models.FeedItem;
import apiwrapper.commons.wikimedia.org.Models.Licenses;
import apiwrapper.commons.wikimedia.org.Models.Thumbnail;
import apiwrapper.commons.wikimedia.org.Models.User;
import apiwrapper.commons.wikimedia.org.Network.API;
import apiwrapper.commons.wikimedia.org.Network.RequestBuilder;
import apiwrapper.commons.wikimedia.org.Network.Tasks.UploadContributionTask;
import apiwrapper.commons.wikimedia.org.Utils.UriToAbsolutePath;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    Commons commons;

    private static final int IMAGE_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        commons = new Commons(getApplicationContext());
        //get edit token for user to upload to  WIKIMEDIA_COMMONS
//
//        commons.resetPassword("email@email.com", new ResetPasswordCallback() {
//            @Override
//            public void onRequestSuccess() {
//                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
//                Log.d("errorMessage", errorMessage);
//
//            }
//
//
//        });

//        commons.getMediaOfTheDay(new RSS_FeedCallback() {
//            @Override
//            public void onFeedReceived(ArrayList<FeedItem> items) {
//                for (FeedItem feedItem : items) {
//                    Log.d("RSS_Feed", feedItem.getFileName());
//                    Log.d("RSS_Feed", feedItem.getMediaLink());
//                    Log.d("RSS_Feed", feedItem.getStreamingURL());
//                }
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });


//        commons.getCaptcha(new CaptchaCallback() {
//            @Override
//            public void onCaptchaReceived(Captcha captcha) {
//                Log.wtf("Captcha", captcha.getCaptchaId());
//                Log.wtf("Captcha", captcha.getCaptchaURL());
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()

                               {
                                   @Override
                                   public void onClick(View view) {


//                Upload local image
                                       Intent intent = new Intent();
                                       intent.setType("image/*");
                                       intent.setAction(Intent.ACTION_GET_CONTENT);
                                       intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                       startActivityForResult(Intent.createChooser(intent,
                                               "Upload to Commons"), IMAGE_REQUEST_CODE);


//                commons.searchInCommons("Tirana", "max", new ContributionsCallback() {
//                    @Override
//                    public void onContributionsReceived(ArrayList<Contribution> contributions) {
//                        Log.d("searchInCommons", String.valueOf(contributions.size()));
//                        Log.d("searchInCommons", contributions.get(0).getUrl());
//                        Log.d("searchInCommons", contributions.get(0).getTitle());
//
//                    }
//
//                    @Override
//                    public void onFailure() {
//
//                    }
//                });


//                commons.getMediaOfTheDay(new RSS_FeedCallback() {
//                    @Override
//                    public void onFeedReceived(ArrayList<FeedItem> items) {
//                        Log.d("onFeedReceived", String.valueOf(items.size()));
//                        Log.d("onFeedReceived", items.get(2).getFileName());
//                        Log.d("onFeedReceived", items.get(2).getTitle());
//                        Log.d("onFeedReceived", items.get(2).getMediaLink());
//                        Log.d("onFeedReceived", items.get(2).getStreamingURL());
//                    }
//
//                    @Override
//                    public void onError(Exception error) {
//
//                    }
//                });

//                commons.loadThumbnail("File:Kungsgatan i Stockholm.webm", 200, 200, new ThumbnailCallback() {
//                    @Override
//                    public void onThumbnailAvailable(String thumbnailURL, String thumbwidth, String thumbheight) {
//                        Log.wtf("Thumbnail", thumbnailURL);
//                    }
//
//                    @Override
//                    public void onError() {
//                        Log.wtf("Thumbnail","error");
//                    }
//                });


//                EditText editText = (EditText) findViewById(R.id.captchatext);
//
//                String captchatext = editText.getText().toString();
////
//                commons.createAccount(
//                        "testUsername2",
//                        "passwordtest1",
//                        "passwordtest1",
//                        "email@email.com",
//                        captchatext,
//                        captchaId,
//                        new CreateAccountCallback() {
//                            @Override
//                            public void onAccountCreatedSuccessful() {
//                                Toast.makeText(getApplicationContext(), "Account created :)", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFailure(String errorMessage) {
//                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        }
//                );

                                   }
                               }

        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        File file = new File(UriToAbsolutePath.getPath(getApplication(), data.getData()));
        // Make sure the request was successful
        if (resultCode == RESULT_OK && file.exists()) {

            // Check which request we're responding to
            if (requestCode == IMAGE_REQUEST_CODE) {

                User user = new User();
                user.setUsername("valdioveliu");// all the upload needs the username
                commons.uploadContribution(
                        file,
                        user,
                        "title",
                        "comment",
                        "description",
                        ContributionType.IMAGE,
                        Licenses.CreativeCommonsAttributionShareAlike30,
                        R.drawable.upload_icon,
                        new UploadCallback() {
                            @Override
                            public void onMediaUploadedSuccessfully() {
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        });

            }
        }
    }



}
