package org.walley.wteplota.ui.home;

import com.google.gson.JsonObject;

import java.util.Hashtable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel
{

  private MutableLiveData<Hashtable<String, JsonObject>> server_data;

  public HomeViewModel()
  {
    server_data = new MutableLiveData<Hashtable<String, JsonObject>>();
    ;
  }

  public LiveData<Hashtable<String, JsonObject>> getText()
  {
    return server_data;
  }
}