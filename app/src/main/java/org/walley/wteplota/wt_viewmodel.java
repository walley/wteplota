package org.walley.wteplota;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class wt_viewmodel extends AndroidViewModel
{
  public static final String TAG = "WT-VM";
  private Hashtable<String, JsonObject> data_hash;
  private MutableLiveData<Hashtable<String, JsonObject>> server_data;

  public wt_viewmodel(Application app)
  {
    super(app);
    server_data = new MutableLiveData<Hashtable<String, JsonObject>>();
    data_hash = new Hashtable<String, JsonObject>();
  }

  public MutableLiveData<Hashtable<String, JsonObject>> xget_server_data()
  {
    return server_data;

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
    Ion.with(context)
            .load("http://91.232.214.23/android/android.php")
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
                Iterator it = result.iterator();
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
//                  EventBus.getDefault().post(new MessageEvent(xs));
                }
                Log.i(TAG, "parsed stuff " + temps);
              }
            });
  }

}
