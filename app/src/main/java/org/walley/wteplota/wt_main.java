package org.walley.wteplota;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class wt_main extends AppCompatActivity
{
  public static final String TAG = "WT";
  Context context = this;

  private static final int RESULT_SETTINGS = 2;

  private AppBarConfiguration mAppBarConfiguration;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
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
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    mAppBarConfiguration = new AppBarConfiguration.Builder(
            R.id.nav_start, R.id.nav_home)
            .setDrawerLayout(drawer)
            .build();
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);

    Ion.getDefault(context).configure().setLogging(TAG, Log.INFO);

  }

  @Override
  public void onStart()
  {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override
  public void onStop()
  {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
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

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      Intent i_preferences = new Intent();
      i_preferences.setClass(context, wt_preferences.class);
      startActivityForResult(i_preferences, RESULT_SETTINGS);
    }

    return super.onOptionsItemSelected(item);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(MessageEvent event)
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

  public boolean is_dark_theme()
  {
    switch (
            getResources().
                    getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
      case Configuration.UI_MODE_NIGHT_YES:
        return true;
      case Configuration.UI_MODE_NIGHT_NO:
        return false;
    }
    return true;
  }

}
