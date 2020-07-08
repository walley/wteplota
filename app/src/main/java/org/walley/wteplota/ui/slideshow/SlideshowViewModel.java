package org.walley.wteplota.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel
{

  private MutableLiveData<String> server_data;

  public SlideshowViewModel()
  {
    server_data = new MutableLiveData<>();
    server_data.setValue("This is slideshow fragment");
  }

  public LiveData<String> getText()
  {
    return server_data;
  }
}