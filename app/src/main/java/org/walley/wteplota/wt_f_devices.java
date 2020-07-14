package org.walley.wteplota;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class wt_f_devices extends Fragment
{
  public static final String TAG = "WT-D";
  TextView tv_data;
  TextView item;
  LinearLayout ll_scroll;
  Hashtable<String, JsonObject> data_hash;
  GradientDrawable gd;
  ArrayAdapter<String> itemsAdapter;
  ArrayList<wt_device> devices_array;
  //final
  ArrayList<String> rooms_list;
  device_adapter adapter;
  private SwipeRefreshLayout swipeContainer;
  private wt_viewmodel wtviewmodel;

  @SuppressLint("ResourceAsColor")
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {

    View root = inflater.inflate(R.layout.fragment_devices, container, false);

    devices_array = new ArrayList<>();
    data_hash = new Hashtable<String, JsonObject>();
    rooms_list = new ArrayList<>();

    gd = new GradientDrawable();
    gd.setColor(R.color.menu);
    gd.setCornerRadius(5);
    gd.setSize(300, 100);
    gd.setStroke(3, 0x03DAC5);

    ll_scroll = (LinearLayout) root.findViewById(R.id.ll_scroll);
    wtviewmodel = ViewModelProviders.of(this).get(wt_viewmodel.class);
    tv_data = root.findViewById(R.id.text_home);
    tv_data.setMovementMethod(new ScrollingMovementMethod());
    tv_data.setTextSize(20);

    tv_data.setText("tap on room");

    wtviewmodel.get_server_data().observe(
            getViewLifecycleOwner(), new Observer<Hashtable<String, JsonObject>>()
            {
              @Override
              public void onChanged(Hashtable<String, JsonObject> result)
              {
                String temps = "vm onchanged(): ";
                update_temp();
              }
            });

    data_hash = wtviewmodel.get_server_data().getValue();
    update_temp();

    adapter = new device_adapter(getActivity(), devices_array);
    ListView listView = (ListView) root.findViewById(R.id.lv_home);
    listView.setAdapter(adapter);

    swipeContainer = (SwipeRefreshLayout) root.findViewById(R.id.swipeContainer);
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
    {
      @Override
      public void onRefresh()
      {
        data_hash = wtviewmodel.get_server_data().getValue();
        update_temp();
        swipeContainer.setRefreshing(false);
      }
    });
    return root;
  }

  @Override
  public void onStart()
  {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override
  public void onStop()
  {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(MessageEvent event)
  {
    Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
    switch (event.message) {
      case "data_done":
        update_temp();
        break;
    }
  }


  private void update_temp()
  {
    String temps = "";
    rooms_list.clear();

    Log.i(TAG, "update_temp() start.");

    for (String key : data_hash.keySet()) {
      JsonElement element = (JsonElement) data_hash.get(key);
      JsonObject jo = element.getAsJsonObject();
      for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
        if (entry.getKey().equals("Nazev")) {
          String room_name = entry.getValue().toString().replace("\"", "");
          rooms_list.add(room_name);
          Log.i(TAG, "update_temp() name: " + entry.getValue().toString());
        }
      }
    }
    create_rooms_menu(rooms_list);
  }


  private void xget_temp()
  {
    //final ArrayList<String>
    rooms_list = new ArrayList<>();
    // = new w_device[1];// = new w_device("Natxhan", "San Diexxxgo");

    Ion.with(getActivity())
            .load("http://91.232.214.23/android/android.php")
            .asJsonArray()
            .setCallback(new FutureCallback<JsonArray>()
            {
              @Override
              public void onCompleted(Exception exception, JsonArray result)
              {
                String item_json;

                if (result == null) {
                  Log.e(TAG, "json error");
                  return;
                }
                // do stuff with the result or error

                Iterator it = result.iterator();
                String temps = "";
                rooms_list.clear();
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
                      rooms_list.add(room_name);
                      Log.i(TAG, "jmeno, " + entry.getValue().toString());
                    }
                    temps += entry.getKey() + " = " + entry.getValue() + "\n";
                  }
//                  EventBus.getDefault().post(new MessageEvent(xs));
                }
                Log.i(TAG, "parsed stuff " + temps);
                create_rooms_menu(rooms_list);
                swipeContainer.setRefreshing(false);
              }
            });
  }

  /******************************************************************************/
  private void create_rooms_menu(ArrayList<String> rooms_list)
  /******************************************************************************/
  {
    ll_scroll.removeAllViews();
    ll_scroll.invalidate();
    for (int i = 0; i < rooms_list.size(); i++) {
      item = new TextView(getActivity());
      item.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          StringBuilder ss;
          TextView tv = (TextView) v;
          String room_name = tv.getText().toString();
          JsonObject room_data = data_hash.get(room_name);
          wt_device device;

          devices_array.clear();
          Toast.makeText(getActivity(), room_name, Toast.LENGTH_SHORT).show();
          tv_data.setText("as");
          ss = new StringBuilder();
          if (room_data != null) {
            for (Map.Entry<String, JsonElement> entry : room_data.entrySet()) {
              ss.append(entry.getKey()).append(" = ");
              ss.append(entry.getValue()).append("\n");
              device = new wt_device(entry.getKey(), entry.getValue().toString());
              devices_array.add(device);
            }
          }
          adapter.notifyDataSetChanged();
        }
      });
      item.setClickable(true);
      item.setTextSize(25);
      item.setBackground(gd);
      String entry = rooms_list.get(i);
      Log.i(TAG, "create_rooms_menu(): creating room: " + entry);
      item.setText(entry.replace("\"", ""));
      ll_scroll.addView(item);
    }
  }

  public class device_adapter extends ArrayAdapter<wt_device>
  {
    public device_adapter(Context context, ArrayList<wt_device> devices)
    {
      super(context, 0, devices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      wt_device device = getItem(position);

      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_device, parent, false);
      }
      TextView tv_device_name = (TextView) convertView.findViewById(R.id.tv_device_name);
      TextView tv_device_value = (TextView) convertView.findViewById(R.id.tv_device_value);
      ImageView iv_device_image = (ImageView) convertView.findViewById(R.id.iv_device_image);

      tv_device_name.setText(device.getName());
      tv_device_value.setText(device.getValue());
      int temp_temp = 0;
      try {
        temp_temp = Integer.parseInt(device.getValue().toString().replace("\"", ""));
      } catch (Exception e) {
        temp_temp = 1;
      }

      if (temp_temp < -120) {
        tv_device_value.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
      } else if (-120 < temp_temp && temp_temp < 0) {
        tv_device_value.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
      } else {
        tv_device_value.setTextColor(ContextCompat.getColor(getContext(), R.color.light_green));
      }
      return convertView;
    }
  }
}