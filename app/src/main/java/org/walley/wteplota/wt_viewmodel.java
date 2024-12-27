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
    url = prefs.getString("server_url", "https://localhost/");
    if (!url.endsWith("/")) {url += "/";}
    api = prefs.getString("api_type", "default_api");

    if (api.equals("marek")) {
      url += "android.php";
    }

    if (api.equals("walley")) {
//      url += "wiot/v1/devices?output=json";
      url += "wiot/v1/house?output=json";
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

  public JsonObject deep_merge(String name, JsonObject one, JsonObject two)
  {
    JsonObject x = new JsonObject();

    Log.d(TAG, "deep_merge(): one" + one.toString());
    Log.d(TAG, "deep_merge(): two" + two.toString());


    x.add(name + Math.random() * 1000, one);
    x.add(name + Math.random() * 1000, two);
    Log.d(TAG, "jo x: " + x.toString());
    return x;
  }

  private String get_room_name(JsonObject jo)
  {
    for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
      if (entry.getKey().equals("roomname")) {
        return entry.getValue().toString().replace("\"", "");
      }
    }

    return "default_room";
  }

  private String get_device_name(JsonObject jo)
  {
    for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
      if (entry.getKey().equals("name")) {
        return entry.getValue().toString().replace("\"", "");
      }
    }

    return "default_device";
  }

  private void parse_result(JsonObject result)
  {
    String temps = "";
    Log.d(
            TAG,
            "parse_result(OBJECT): result: " + result.toString() + "size" + result.entrySet().size()
         );

    data_hash.clear();

    for (Map.Entry<String, JsonElement> main_entry : result.entrySet()) {
      String entryset_key = main_entry.getKey();
      JsonElement entryset_value = (JsonElement) main_entry.getValue();
      JsonObject jo = entryset_value.getAsJsonObject();
      //String room_name = get_room_name(jo);
      String room_name = entryset_key;

      Log.d(TAG, "parse_result(OBJECT): entry key: " + entryset_key);
      Log.d(TAG, "parse_result(OBJECT): entry value (element): " + entryset_value.toString());
      Log.d(TAG, "parse_result(OBJECT): room name: " + room_name);


      for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
        String key = entry.getKey();
        JsonElement value = entry.getValue();

        Log.d(TAG, "Key: " + key + ", Value: " + value);
       try {
         JsonObject final_temp = new JsonObject();
         final_temp.add(key, value);
         data_hash.put(room_name, final_temp);
       } catch (Exception e) {
         Log.e(TAG, "exception Key: " + key + ", Value: " + value);
       }
      }


     /* Set<Map.Entry<String, JsonElement>> i;
      for (i = jo.entrySet(); jo.entrySet() != null;) {
        // JsonObject temp = data_hash.get(room_name);
        JsonObject temp = null; //entryset_value.getAsJsonObject();

        Log.d(TAG,i.toString());

        if (temp != null) {
          temp.add(get_device_name(jo), jo);
          data_hash.put(room_name, temp);
        } else {
          JsonObject final_temp = new JsonObject();
          final_temp.add(get_device_name(jo), jo);
          data_hash.put(room_name, final_temp);
        }
      }*/

    }
    Log.d(TAG, "parse_result(OBJECT): final data_hash: " + data_hash.toString());
    EventBus.getDefault().post(new message_event("data_done"));
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
