package org.walley.wteplota;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class wt_f_devices extends wt_f_base
{
  public static final String TAG = "WT-D";
  TextView tv_data;
  TextView item;
  LinearLayout ll_scroll;
  Hashtable<String, JsonObject> data_hash;
  GradientDrawable gd;
  ArrayAdapter<String> itemsAdapter;
  ArrayList<wt_device> devices_array;
  ArrayList<String> rooms_list;
  device_adapter adapter;
  Boolean first_run = true;
  private SwipeRefreshLayout swipeContainer;
  private wt_viewmodel wtviewmodel;

  @SuppressLint("ResourceAsColor")
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {

    View root = inflater.inflate(R.layout.fragment_devices, container, false);

    devices_array = new ArrayList<>();
    data_hash = new Hashtable<String, JsonObject>();
    rooms_list = new ArrayList<>();

    stuff();

    gd = new GradientDrawable();
    gd.setColor(R.color.menu);
    gd.setCornerRadius(40);
    gd.setSize(250, 100);
    gd.setStroke(3, 0x03DAC5);

    ll_scroll = (LinearLayout) root.findViewById(R.id.ll_scroll);
    wtviewmodel = ViewModelProviders.of(this).get(wt_viewmodel.class);
    tv_data = root.findViewById(R.id.text_home);
    tv_data.setMovementMethod(new ScrollingMovementMethod());
    tv_data.setTextSize(20);

    tv_data.setText(R.string.tap_on_room);

    wtviewmodel.get_server_data().observe(
            getViewLifecycleOwner(), new Observer<Hashtable<String, JsonObject>>()
            {
              @Override
              public void onChanged(Hashtable<String, JsonObject> result)
              {
                Log.i(TAG, "vm observer onchanged()");
                update_rooms_list();
              }
            });

    data_hash = wtviewmodel.get_server_data().getValue();

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
        update_rooms_list();
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
  public void onMessageEvent(message_event event)
  {
    //Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
    Log.i(TAG, event.message);
    switch (event.message) {
      case "data_done":
        update_rooms_list();

        if (first_run) {
          show_listview("Uvod");
        } else {
          first_run = false;
        }

        break;
    }
  }

  private void update_rooms_list()
  {
    String temps = "";
    rooms_list.clear();

    Log.i(TAG, "update_rooms_list() start.");

    for (String key : data_hash.keySet()) {
      Log.i(TAG, "update_rooms_list(): key: " + key);
      if (base_api.equals("marek")) {
        JsonElement element = (JsonElement) data_hash.get(key);
        assert element != null;
        JsonObject jo = element.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
          if (entry.getKey().equals("Nazev")) {
            String room_name = entry.getValue().toString().replace("\"", "");
            rooms_list.add(room_name);
            Log.i(TAG, "update_rooms_list() name: " + entry.getValue().toString());
          }
        }
      } else if (base_api.equals("walley")) {
        rooms_list.add(key);
      }
    }
    Log.i(TAG, "update_rooms_list(): list: " + rooms_list.toString());
    create_rooms_menu(rooms_list);
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
          TextView tv = (TextView) v;
          String room_name = tv.getText().toString();
          show_listview(room_name);
        }
      });
      item.setClickable(true);
      item.setTextSize(25);
      item.setGravity(Gravity.CENTER);
      item.setBackground(gd);
      String entry = rooms_list.get(i);
      Log.i(TAG, "create_rooms_menu(): creating room: " + entry);
      item.setText(entry.replace("\"", ""));
      ll_scroll.addView(item);
    }
  }

  private void add_to_devices_list(String name, String value)
  {
    String device_name;
    String device_type;
    String device_value;
    int device_index = 0;

    boolean has_name = false;
    boolean has_type = false;
    boolean has_value = false;


    if (name.contains(".typ")) {
      device_name = name.substring(0, name.length() - 4).replace("\"", "");
      device_type = value.replace("\"", "");
      device_value = "";
    } else {
      device_name = name.replace("\"", "");
      device_type = "";
      device_value = value.replace("\"", "");
    }

    for (int i = 0; i < devices_array.size(); i++) {
      wt_device x = devices_array.get(i);
      if (x.getName().equals(device_name)) {
        has_name = true;
        device_index = i;
        if (x.getType() != null && !x.getType().isEmpty()) {
          has_type = true;
        }
        if (x.getValue() != null && !x.getValue().isEmpty()) {
          has_value = true;
        }
        break;
      }
    }

    Log.i(
            TAG,
            "a_t_d_l(" + name + "," + value + "): has_value:" + has_value + ", has_type:" + has_type
         );

    if (has_name) {
      Log.i(
              TAG,
              "a_t_d_l(" + name + "," + value + "): existing :" + device_name + "," + device_value + "," + device_type
           );
      devices_array.get(device_index).setName(device_name);
      if (!has_type) {
        devices_array.get(device_index).setType(device_type);
      }
      if (!has_value) {
        devices_array.get(device_index).setValue(device_value);
      }
    } else {
      Log.i(
              TAG,
              "a_t_d_l(" + name + "," + value + "): new :" + device_name + "," + device_value + "," + device_type
           );
      wt_device x = new wt_device(device_name, device_value, device_type);
      devices_array.add(x);
    }
  }

  private void show_listview(String room_name)
  {
    JsonObject room_data = data_hash.get(room_name);
    wt_device device;

    devices_array.clear();
    if (room_data != null) {
      for (Map.Entry<String, JsonElement> entry : room_data.entrySet()) {
        if (!entry.getKey().equals("Nazev")) {
          device = new wt_device(entry.getKey(), entry.getValue().toString());
          add_to_devices_list(entry.getKey(), entry.getValue().toString());
        }
      }
    }
    adapter.notifyDataSetChanged();
  }

  public class device_adapter extends ArrayAdapter<wt_device>
  {
    public device_adapter(Context context, ArrayList<wt_device> devices)
    {
      super(context, 0, devices);
    }

    @NotNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      wt_device device = getItem(position);

      Log.i(
              TAG,
              "getView(): device:" + device.getName() + "," + device.getValue() + "," + device.getType() + "."
           );

      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_device, parent, false);
      }
      TextView tv_device_name = (TextView) convertView.findViewById(R.id.tv_device_name);
      TextView tv_device_value = (TextView) convertView.findViewById(R.id.tv_device_value);

      ImageView iv_device_image = (ImageView) convertView.findViewById(R.id.iv_device_image);

      tv_device_name.setText(device.getName());
      tv_device_value.setText(device.getValue());
      float temp_temp = 0;
      try {
        temp_temp = Float.parseFloat(device.getValue().replace("\"", ""));
      } catch (Exception e) {
        temp_temp = 1;
      }

      Color negative;
      int positive;

      if (is_dark_theme()) {
        positive = ContextCompat.getColor(getContext(), R.color.light_green);
      } else {
        positive = ContextCompat.getColor(getContext(), R.color.dark_green);
      }

      if (temp_temp < -120) {
        tv_device_value.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
      } else if (-120 < temp_temp && temp_temp < 0) {
        tv_device_value.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
      } else {
        tv_device_value.setTextColor(positive);
      }

      Drawable unwrappedDrawable;
      switch (device.getType()) {
        case "dvere":
          unwrappedDrawable = AppCompatResources.getDrawable(
                  getContext(), R.drawable.ic_door_open_24);
          break;
        case "teplomer":
          unwrappedDrawable = AppCompatResources.getDrawable(
                  getContext(), R.drawable.ic_thermometer_empty_24);
          break;
        case "motor":
          unwrappedDrawable = AppCompatResources.getDrawable(
                  getContext(), R.drawable.ic_fan_24);
          break;
        case "voda":
          unwrappedDrawable = AppCompatResources.getDrawable(
                  getContext(), R.drawable.ic_faucet_24);
          break;

        default:
          unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_cat_24);
      }

      Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
      if (is_dark_theme()) {
        DrawableCompat.setTint(wrappedDrawable, Color.WHITE);
        Log.i(TAG, "Dark Theme");
      } else {
        Log.i(TAG, "Shit Theme");
        DrawableCompat.setTint(wrappedDrawable, Color.BLACK);
      }

      iv_device_image.setImageDrawable(wrappedDrawable);

      return convertView;
    }
  }
}
