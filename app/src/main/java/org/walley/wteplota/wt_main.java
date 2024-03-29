package org.walley.wteplota;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class wt_main extends wt_base
{
  public static final String TAG = "WT-MAIN";
  private static final int RESULT_SETTINGS = 2;
  Context context = this;
  private AppBarConfiguration mAppBarConfiguration;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    String t_appkey = "agayut8g5hm4xagxh3u9";
    String t_appsecret = "qav7rhpdcf77t7whf45pyvr5snndpujv";

//    TuyaHomeSdk.init(getApplication(), t_appkey, t_appsecret);
//    TuyaHomeSdk.setDebugMode(true);
//    TuyaHomeSdk.getRequestInstance();

//ryRoomList(callback: ITuyaGetRoomListCallback)

    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    FloatingActionButton fab = findViewById(R.id.fab);

    fab.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        Snackbar.make(view, "Yo! this does notin", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
      }
    });

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.nav_view);
    mAppBarConfiguration = new AppBarConfiguration.Builder(
            R.id.nav_start,
            R.id.nav_home,
            R.id.nav_login,
            R.id.nav_sms,
            R.id.nav_about,
            R.id.nav_prefs
    ).setOpenableLayout(drawer).build();
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);

    Ion.getDefault(context).configure().setLogging(TAG, Log.INFO);

  }

  @Override
  public void onStart()
  {
    super.onStart();
    org.greenrobot.eventbus.EventBus.getDefault().register(this);
  }

  @Override
  public void onStop()
  {
    EventBus.getDefault().unregister(this);
    //TUYA
    // TuyaHomeSdk.onDestroy();

    super.onStop();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    MenuCompat.setGroupDividerEnabled(menu, true);
    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case RESULT_SETTINGS:
        //validate_settings();
        showUserSettings();
        break;
    }

  }

  @Override
  public boolean onSupportNavigateUp()
  {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
            || super.onSupportNavigateUp();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      Intent i_preferences = new Intent();
      i_preferences.setClass(context, wt_preferences.class);
      startActivityForResult(i_preferences, RESULT_SETTINGS);
    }

    if (id == R.id.action_about) {
      Fragment x = new wt_f_about();
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.nav_host_fragment, x);
      ft.commit();
      ft.show(x);
    }

    if (id == R.id.action_login) {
    }

    return super.onOptionsItemSelected(item);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(message_event event)
  {
    Log.i(TAG, event.message);
  }


  private void showUserSettings()
  {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

    StringBuilder builder = new StringBuilder();

    builder.append("\n http username: ");
    builder.append(sharedPrefs.getString("http_username", "nic"));
    builder.append("\n http password:").append(sharedPrefs.getString("http_password", "nic"));
    builder.append("\n app username: ").append(sharedPrefs.getString("app_username", "nic"));
    builder.append("\n app password:").append(sharedPrefs.getString("app_password", "nic"));
    builder.append("\n url: ").append(sharedPrefs.getString("url", "nic"));

    Toast.makeText(getApplicationContext(), builder, Toast.LENGTH_LONG).show();

  }

}
