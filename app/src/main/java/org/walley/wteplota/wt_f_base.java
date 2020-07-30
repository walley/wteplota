package org.walley.wteplota;

import android.content.res.Configuration;

import androidx.fragment.app.Fragment;

public class wt_f_base extends Fragment
{

  private boolean is_dark_theme()
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
