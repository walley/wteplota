package org.walley.wteplota;

import com.google.gson.JsonObject;

import java.util.Hashtable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class wt_viewmodel extends ViewModel
{

  private MutableLiveData<Hashtable<String, JsonObject>> server_data;

  public wt_viewmodel()
  {
    server_data = new MutableLiveData<Hashtable<String, JsonObject>>();
  }

  public MutableLiveData<Hashtable<String, JsonObject>> get_server_data()
  {
    return server_data;
  }

  public LiveData<Hashtable<String, JsonObject>> getText()
  {
    return server_data;
  }
}