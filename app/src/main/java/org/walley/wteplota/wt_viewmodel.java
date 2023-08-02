package org.walley.wteplota;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;


public class wt_viewmodel extends AndroidViewModel
{
  public static final String TAG = "WT-VM";
  String url;
  String api;
  SharedPreferences prefs;
  private Hashtable<String, JsonObject> data_hash;
  private MutableLiveData<Hashtable<String, JsonObject>> server_data;

  public wt_viewmodel(Application app)
  {
    super(app);
    server_data = new MutableLiveData<Hashtable<String, JsonObject>>();
    data_hash = new Hashtable<String, JsonObject>();

    prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
    url = prefs.getString("server_url", "https://localhost");
    Log.d(TAG, "wt_viewmodel() url:" + url);
    api = prefs.getString("api_type", "default_api");
    Log.d(TAG, "wt_viewmodel() api:" + api);
    if (api.equals("marek")) {
      url += "android.php";
    }
  }

  public LiveData<Hashtable<String, JsonObject>> get_server_data()
  {
    get_temp();
    server_data.setValue(data_hash);
    return server_data;
  }

  private void get_temp()
  {

    Context context = getApplication().getApplicationContext();
    Ion
            .with(context)
            .load(url)
            .asJsonArray()
            .setCallback(new FutureCallback<JsonArray>()
            {
              @Override
              public void onCompleted(Exception exception, JsonArray result)
              {
                String item_json;

                if (result == null) {
                  Log.e(TAG, "get_temp(): json error");
                  return;
                }
                // do stuff with the result or error
                Iterator<JsonElement> it = result.iterator();
                String temps = "";
                data_hash.clear();

                while (it.hasNext()) {
                  JsonElement element = (JsonElement) it.next();
                  Log.i(TAG, "element " + element.getAsJsonObject().toString());
                  String xs = element.getAsJsonObject().toString();
                  JsonObject jo = element.getAsJsonObject();
                  temps += "--------\n";
                  for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
                    if (entry.getKey().equals("Nazev")) {
                      String room_name = entry.getValue().toString().replace("\"", "");
                      data_hash.put(room_name, jo);
                      Log.i(
                              TAG,
                              "get_temp(), selected name entry:, " + entry.getValue().toString()
                           );
                    }
                    temps += entry.getKey() + " = " + entry.getValue() + "\n";
                  }
                  EventBus.getDefault().post(new MessageEvent("data_done"));
                }
                Log.d(TAG, "parsed stuff " + temps);
              }
            });
  }

}
