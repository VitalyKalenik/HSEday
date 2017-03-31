package com.example.hseday;


import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.example.hseday.DialogFragments.DialogQuest;
import com.example.hseday.NavigationFragments.FragmentAboutHSE;
import com.example.hseday.NavigationFragments.FragmentDedication;
import com.example.hseday.NavigationFragments.FragmentFaculties;
import com.example.hseday.NavigationFragments.FragmentMap;
import com.example.hseday.NavigationFragments.FragmentOrganisations;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.util.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.view.KeyCharacterMap.load;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FragmentFaculties FragmentFaculties;
    FragmentOrganisations FragmentOrganisations;
    FragmentAboutHSE FragmentAboutHSE;
    FragmentDedication FragmentDedication;
    FragmentMap FragmentMap;
    NavigationView navigationView;
    TextView UserName;
    ImageView UserImage;
    VKRequest.VKRequestListener mRequestListener;
    SharedPreferences sPref;
    private Handler mHandler = new Handler();
    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS};
    private final static String FIELDS = "photo, photo_50, photo_100, photo_200";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Карта");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_window);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        View hView = navigationView.getHeaderView(0);

        UserName = (TextView) hView.findViewById(R.id.user_name);
        UserImage = (ImageView) hView.findViewById(R.id.user_image);

        FragmentFaculties = new FragmentFaculties();
        FragmentOrganisations = new FragmentOrganisations();
        FragmentAboutHSE = new FragmentAboutHSE();
        FragmentDedication = new FragmentDedication();
        FragmentMap = new FragmentMap();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_map, FragmentMap);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                SharedPreferences sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
                if (VKSdk.isLoggedIn()) {
                    if(sharedPref.getString("VKname", "").equals("")){
                        Toast.makeText(getApplicationContext(), "fillNameIfLoggedIn", Toast.LENGTH_SHORT).show();
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                fillNameIfLoggedIn();
                            }
                        }, 2300);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "sharedPreferences", Toast.LENGTH_SHORT).show();
                        UserName.setText(sharedPref.getString("VKname", ""));
                        Glide.with(getApplicationContext()).load(sharedPref.getString("VKavatar", "")).into(UserImage);
                        navigationView.getMenu().findItem(R.id.nav_login_vk).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                    }
                }
                else if(isLoggedIn()){
                    if(sharedPref.getString("FBname", "").equals("")){
                        Toast.makeText(getApplicationContext(), "fillNameIfLoggedIn", Toast.LENGTH_SHORT).show();
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                fillNameIfLoggedIn();
                            }
                        }, 1500);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "sharedPreferences", Toast.LENGTH_SHORT).show();
                        UserName.setText(sharedPref.getString("FBname", ""));
                        Glide.with(getApplicationContext()).load(sharedPref.getString("FBavatar", "")).into(UserImage);
                        navigationView.getMenu().findItem(R.id.nav_login_vk).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                    }
                }
            }
        }, 1);
    }

    public void fillNameIfLoggedIn() {
        if (VKSdk.isLoggedIn()) {
            navigationView.getMenu().findItem(R.id.nav_login_vk).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            if (VKSdk.isLoggedIn()) {
                VKRequest profileInfo = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_50, photo_100, photo_200"));
                profileInfo.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList<VKApiUser> userList = (VKList<VKApiUser>) response.parsedModel;
                        VKApiUser user = userList.get(0);
                        UserName.setText(user.first_name + " " + user.last_name);

                        String photo = null;
                        if (!user.photo_200.equals("http://vk.com/images/camera_a.gif"))
                            photo = user.photo_200;
                        else if (!user.photo_100.equals("http://vk.com/images/camera_b.gif"))
                            photo = user.photo_100;
                        else
                            photo = user.photo_50;

                        Glide.with(getApplicationContext()).load(photo).into(UserImage);

                        SharedPreferences sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("VKname", user.first_name + " " +  user.last_name);
                        editor.putString("VKavatar", photo);
                        editor.apply();
                    }
                });
            }

        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.map_quest_mark && item.isChecked() == true)
            Toast.makeText(this, "True", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "False", Toast.LENGTH_SHORT).show();
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_map) {
            transaction.replace(R.id.content_map, FragmentMap);
        } else if (id == R.id.nav_faculties) {
            transaction.replace(R.id.content_map, FragmentFaculties);
        } else if (id == R.id.nav_organisations) {
            transaction.replace(R.id.content_map, FragmentOrganisations);
        } else if (id == R.id.nav_aboutHSE) {
            transaction.replace(R.id.content_map, FragmentAboutHSE);
        } else if (id == R.id.nav_posvyashenie) {
            transaction.replace(R.id.content_map, FragmentDedication);
        } else if (id == R.id.nav_login_vk) {
            Intent intent = new Intent(this, ActivityLogin.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_logout) {
            VKSdk.logout();
            LoginManager.getInstance().logOut();
            if (!isLoggedIn() && !VKSdk.isLoggedIn()) {
                Toast.makeText(this, "Logged out succesfully", Toast.LENGTH_SHORT).show();
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_login_vk).setVisible(true);
                UserName.setText("День вышки");
                UserImage.setImageResource(0);
            }
            SharedPreferences sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.commit();
        }


        transaction.addToBackStack(null);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
