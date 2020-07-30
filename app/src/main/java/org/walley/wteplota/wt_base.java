package org.walley.wteplota;

import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;

public class wt_base extends AppCompatActivity
{

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
