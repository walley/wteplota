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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class wt_f_devices extends Fragment
{
  public static final String TAG = "WT-H";
  TextView tv_data;
  TextView item;
  LinearLayout ll_scroll;
  Hashtable<String, JsonObject> data_hash;
  GradientDrawable gd;
  ArrayAdapter<String> itemsAdapter;
  private SwipeRefreshLayout swipeContainer;
  private HomeViewModel homeViewModel;
  ArrayList<w_device> devices_array;
  //final
  ArrayList<String> rooms_list;
  device_adapter adapter;

  @SuppressLint("ResourceAsColor")
  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState
                          )
  {

    View root = inflater.inflate(R.layout.fragment_home, container, false);

    devices_array = new ArrayList<>();

//    device_adapter adapter = new device_adapter(getActivity(), devices_array);
//    ListView listView = (ListView) root.findViewById(R.id.lv_home);
//    listView.setAdapter(adapter);

    data_hash = new Hashtable<String, JsonObject>();

    gd = new GradientDrawable();
    gd.setColor(R.color.menu);
    gd.setCornerRadius(5);
    gd.setSize(300, 100);
    gd.setStroke(3, 0x03DAC5);

    ll_scroll = (LinearLayout) root.findViewById(R.id.ll_scroll);
    homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    tv_data = root.findViewById(R.id.text_home);
    tv_data.setMovementMethod(new ScrollingMovementMethod());
    tv_data.setTextSize(20);

    tv_data.setText("kokot");

/*    homeViewModel.observe(getViewLifecycleOwner(), new Observer<Hashtable<String, JsonObject>>()
    {
      @Override
      public void onChanged(@Nullable String s)
      {
        tv_data.setText(s);
      }
    });
*/
    get_temp();

    adapter = new device_adapter(getActivity(), devices_array);
    ListView listView = (ListView) root.findViewById(R.id.lv_home);
    listView.setAdapter(adapter);

    swipeContainer = (SwipeRefreshLayout) root.findViewById(R.id.swipeContainer);
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
    {
      @Override
      public void onRefresh()
      {
        get_temp();
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
  }

  private void get_temp()
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
                w_device device;

                if (result == null) {
                  Log.e(TAG, "json error");
                  return;
                }
                // do stuff with the result or error
                Log.i(TAG, "json loaded" + result);
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
                    if (entry.getKey().equals("jmeno")) {
                      String room_name = entry.getValue().toString().replace("\"", "");
                      data_hash.put(room_name, jo);
                      rooms_list.add(room_name);
                      Log.i(TAG, "jmeno, " + entry.getValue().toString());
                    }
                    temps += entry.getKey() + " = " + entry.getValue() + "\n";
                    device = new w_device(entry.getKey(), entry.getValue().toString());
                    devices_array.add(device);
                  }
                  tv_data.setText(temps);
//                  EventBus.getDefault().post(new MessageEvent(xs));
                }

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
          w_device device;

          devices_array.clear();
          Toast.makeText(getActivity(), room_name, Toast.LENGTH_SHORT).show();
          tv_data.setText("as");
          ss = new StringBuilder();
          if (room_data != null) {
            for (Map.Entry<String, JsonElement> entry : room_data.entrySet()) {
              ss.append(entry.getKey()).append(" = ");
              ss.append(entry.getValue()).append("\n");
              device = new w_device(entry.getKey(), entry.getValue().toString());
              devices_array.add(device);
            }
            tv_data.setText(ss.toString());
          }
          adapter.notifyDataSetChanged();
        }
      });
      item.setClickable(true);
      item.setTextSize(25);
      item.setBackground(gd);
      String entry = rooms_list.get(i);
      item.setText(entry.replace("\"", ""));
      ll_scroll.addView(item);
      Log.i(TAG, "entry, " + entry);
    }
  }

  public class w_device
  {
    private String name;
    private String value;
    private String type;

    public w_device(String name, String value)
    {
      this.setName(name);
      this.setValue(value);
    }

    public String getName()
    {
      return name;
    }

    public void setName(String name)
    {
      this.name = name;
    }

    public String getValue()
    {
      return value;
    }

    public void setValue(String value)
    {
      this.value = value;
    }

    public String getType()
    {
      return type;
    }

    public void setType(String type)
    {
      this.type = type;
    }
  }

  public class device_adapter extends ArrayAdapter<w_device>
  {
    public device_adapter(Context context, ArrayList<w_device> devices)
    {
      super(context, 0, devices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      w_device user = getItem(position);

      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_device, parent, false);
      }
      TextView tvName = (TextView) convertView.findViewById(R.id.tv_device_name);
      TextView tvHome = (TextView) convertView.findViewById(R.id.tv_device_value);
      tvName.setText(user.getName());
      tvHome.setText(user.getValue());
      return convertView;
    }
  }
}