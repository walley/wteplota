package org.walley.wteplota;

import android.app.Application;

import com.google.gson.JsonObject;

import java.util.Hashtable;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class wt_viewmodel extends AndroidViewModel
{

  private MutableLiveData<Hashtable<String, JsonObject>> server_data;

  public wt_viewmodel(Application app)
  {
    super(app);
    server_data = new MutableLiveData<Hashtable<String, JsonObject>>();
  }

  public MutableLiveData<Hashtable<String, JsonObject>> xget_server_data()
  {
    return server_data;
  }

  public LiveData<Hashtable<String, JsonObject>> get_server_data()
  {
    return server_data;
  }
}
