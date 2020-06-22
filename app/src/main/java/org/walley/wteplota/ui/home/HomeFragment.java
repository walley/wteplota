package org.walley.wteplota.ui.home;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.walley.wteplota.MessageEvent;
import org.walley.wteplota.R;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class HomeFragment extends Fragment
{
  public static final String TAG = "WT-H";
  TextView tv_data;
  TextView item;
  LinearLayout ll_scroll;
  Hashtable<String, JsonObject> data;
  GradientDrawable gd;
  private HomeViewModel homeViewModel;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState
                          )
  {

    data = new Hashtable<String, JsonObject>();
    View root = inflater.inflate(R.layout.fragment_home, container, false);

    gd = new GradientDrawable();
    gd.setColor(0xFF00FF00); // Changes this drawbale to use a single color instead of a gradient
    gd.setCornerRadius(5);
    gd.setSize(400, 100);
    gd.setStroke(3, 0x03DAC5);

    ll_scroll = (LinearLayout) root.findViewById(R.id.ll_scroll);
    homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    tv_data = root.findViewById(R.id.text_home);
    tv_data.setMovementMethod(new ScrollingMovementMethod());
    tv_data.setTextSize(20);

    tv_data.setText("kokot");

    homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
    {
      @Override
      public void onChanged(@Nullable String s)
      {
        tv_data.setText(s);
      }
    });

    get_temp();

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


  // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(MessageEvent event)
  {
    Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
  }

  private void get_temp()
  {
    final ArrayList<String> rooms_list = new ArrayList<>();
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
                Log.i(TAG, "json loaded" + result);
                Iterator it = result.iterator();
                String temps = "";
                rooms_list.clear();
                data.clear();

                while (it.hasNext()) {
                  JsonElement element = (JsonElement) it.next();
                  Log.i(TAG, "element " + element.getAsJsonObject().toString());
                  String xs = element.getAsJsonObject().toString();
                  JsonObject jo = element.getAsJsonObject();
                  temps += "--------\n";
                  for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
                    if (entry.getKey().equals("jmeno")) {
                      String room_name = entry.getValue().toString().replace("\"", "");
                      data.put(room_name, jo);
                      rooms_list.add(room_name);
                      Log.i(TAG, "jmeno, " + entry.getValue().toString());
                    }
                    temps += entry.getKey() + " = " + entry.getValue() + "\n";
                  }
                  tv_data.setText(temps);
//                  EventBus.getDefault().post(new MessageEvent(xs));
                }

                create_rooms_menu(rooms_list);
              }
            });
  }

  private void create_rooms_menu(ArrayList<String> rooms_list)
  {
    for (int i = 0; i < rooms_list.size(); i++) {
      item = new TextView(getActivity());
      item.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          StringBuilder ss;
          TextView tv = (TextView) v;
          String str = tv.getText().toString();
          JsonObject n = data.get(str);
          Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
          tv_data.setText("as");
          ss = new StringBuilder();
          if (n != null) {
            for (Map.Entry<String, JsonElement> entry : n.entrySet()) {
              ss.append(entry.getKey()).append(" = ");
              ss.append(entry.getValue()).append("\n");
            }
            tv_data.setText(ss.toString());
          }
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
}