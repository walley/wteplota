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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
    api = prefs.getString("api_type", "default_api");

    if (api.equals("marek")) {
      url += "android.php";
    }

    if (api.equals("walley")) {
      url += "wiot/v1/devices?output=json";
    }

    Log.d(TAG, "wt_viewmodel() url:" + url);
    Log.d(TAG, "wt_viewmodel() api:" + api);
  }

  public LiveData<Hashtable<String, JsonObject>> get_server_data()
  {
    get_temp();
    server_data.setValue(data_hash);
    return server_data;
  }

  private void parse_result(JsonArray result)
  {
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
                  "parse_result(array), selected name entry:, " + entry.getValue().toString()
               );
        }
        temps += entry.getKey() + " = " + entry.getValue() + "\n";
      }
      EventBus.getDefault().post(new message_event("data_done"));
    }
    Log.d(TAG, "parsed array stuff\n" + temps);
  }

  public JsonObject deep_merge(JsonObject source, JsonObject target) throws Exception
  {
    JsonObject x = new JsonObject();
    x.add(source.get("name").getAsString(), source);
    x.add(source.get("name").getAsString(), target);
    return x;

/*
    for (Map.Entry<String, JsonElement> sourceEntry : source.entrySet()) {
      String key = sourceEntry.getKey();
      JsonElement value = sourceEntry.getValue();
      if (!target.has(key)) {
        //target does not have the same key, so perhaps it should be added to target
        if (!value.isJsonNull()) //well, only add if the source value is not null
          target.add(key, value);
      } else {
        if (!value.isJsonNull()) {
          if (value.isJsonObject()) {
            //source value is json object, start deep merge
            deep_merge(value.getAsJsonObject(), target.get(key).getAsJsonObject());
          } else {
            target.add(key, value);
          }
        } else {
          target.remove(key);
        }
      }
    }
    return target;

 */
  }

  private String get_room_id(JsonObject jo)
  {
    for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
      if (entry.getKey().equals("room")) {
        String room_name = entry.getValue().toString().replace("\"", "");
        return room_name;

/*      JsonObject temp = data_hash.get(room_name);
      if (temp != null) {
        try {
          Log.d(TAG, room_name + "merged: " + deep_merge(temp,jo).toString());

          data_hash.put(room_name, deep_merge(temp,jo));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }

      } else {
        data_hash.put(room_name, jo);
      }

 */
      }
    }

    return "default_room";
  }

  private void parse_result(JsonObject result)
  {
    Map<String, Object> attributes = new HashMap<String, Object>();
    Set<Map.Entry<String, JsonElement>> entrySet = result.entrySet();
    String temps = "";

    data_hash.clear();

    for (Map.Entry<String, JsonElement> main_entry : entrySet) {
      Log.d(TAG, "entry key: " + main_entry.getKey());
      JsonElement element = (JsonElement) main_entry.getValue();
      Log.d(TAG, "entry value (element): " + element.getAsJsonObject().toString());
      JsonObject jo = element.getAsJsonObject();
      Log.d(TAG, "room id: " + get_room_id(jo));
      String room_name = get_room_id(jo);
      JsonObject temp = data_hash.get(room_name);
      if (temp != null) {
        try {
          data_hash.put(room_name, deep_merge(temp, jo));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      } else {
        data_hash.put(room_name, jo);
      }

      Log.d(TAG, "data_hash: " + data_hash.toString());

      EventBus.getDefault().post(new message_event("data_done"));
    }
  }

  private void get_as_array()
  {
    Log.d(TAG, "get_as_array(): api marek");
    Context context = getApplication().getApplicationContext();
    Ion.with(context)
            .load(url)
            .asJsonArray()
            .setCallback(new FutureCallback<JsonArray>()
            {
              @Override
              public void onCompleted(Exception e, JsonArray result)
              {
                if (result == null) {
                  Log.e(TAG, "get_as_array(): json error " + e.toString());
                  return;
                }
                parse_result(result);
              }
            });
  }

  private void get_as_object()
  {
    Log.d(TAG, "get_as_object(): api walley");
    Context context = getApplication().getApplicationContext();
    Ion.with(context)
            .load(url)
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>()
            {
              @Override
              public void onCompleted(Exception e, JsonObject result)
              {
                if (result == null) {
                  Log.e(TAG, "get_as_object(): json error " + e.toString());
                  return;
                }
                parse_result(result);
              }

            });
  }

  private void get_temp()
  {

    Context context = getApplication().getApplicationContext();
    Log.d(TAG, "get_temp():");

    switch (api) {
      case "marek":
        get_as_array();
        break;
      case "walley":
        get_as_object();
        break;
    }

  }

}
