package com.example.timetablemanager;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayFragment extends Fragment {
    public static String EVENT_EXTRA = "com.example.timetablemanager.EVENT_EXTRA";
    Dialog addDialog, editDialog;
    int fHour, fMinute, tHour, tMinute;
    ArrayList<ListItem> listItem;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DaysFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DayFragment newInstance(String param1, String param2) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private RecyclerView mRecycleView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_days, container, false);


        SharedPreferences sharedPreferences = container.getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list " + mParam2 + mParam1, null);
        Type type = new TypeToken<ArrayList<ListItem>>() {}.getType();
        listItem = gson.fromJson(json, type);

        if(listItem == null) {
            listItem = new ArrayList<>();
        }

        mRecycleView = v.findViewById(R.id.recyclerView);
        mRecycleView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mAdapter = new ListAdapter(listItem, mParam1, mParam2);

        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecycleView);

        mAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                listItem.remove(position);
                resetIcon(container.getContext(), position);
                mAdapter.notifyItemRemoved(position);
                saveData(container.getContext());
            }

            @Override
            public void onEditClick(int position) {
                editDialog = new Dialog(container.getContext());

                editDialog.setContentView(R.layout.editlist_popup);
                Button mBack = editDialog.findViewById(R.id.back);
                mBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss();
                    }
                });
                ImageView mFrom = editDialog.findViewById(R.id.from);
                ImageView mTo = editDialog.findViewById(R.id.to);
                TextView FromTime = editDialog.findViewById(R.id.timeFrom);
                TextView ToTime = editDialog.findViewById(R.id.timeTo);
                EditText mEvent = editDialog.findViewById(R.id.eventname);

                String Name = listItem.get(position).getName();
                String Time = listItem.get(position).getTime();
                mEvent.setText(Name);
                String [] timeList = Time.split(" ");
                FromTime.setText(timeList[0]);
                ToTime.setText(timeList[2]);


                editDialog.show();

                mFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timepicker = new TimePickerDialog(
                                container.getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        fHour = hourOfDay;
                                        fMinute = minute;

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(0, 0, 0, fHour, fMinute);
                                        FromTime.setText(DateFormat.format("HH:mm", calendar));
                                    }
                                }, 24, 0, true
                        );
                        timepicker.updateTime(fHour, fMinute);
                        timepicker.show();
                    }
                });

                mTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timepicker = new TimePickerDialog(
                                container.getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        tHour = hourOfDay;
                                        tMinute = minute;

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(0, 0, 0, tHour, tMinute);
                                        ToTime.setText(DateFormat.format("HH:mm", calendar));
                                    }
                                }, 24, 0, true
                        );
                        timepicker.updateTime(tHour, tMinute);
                        timepicker.show();
                    }
                });

                Button mAddToList = editDialog.findViewById(R.id.addToList);
                mAddToList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(Float.parseFloat(FromTime.getText().toString().replace(":","."))
                                > Float.parseFloat(ToTime.getText().toString().replace(":",".")) && !ToTime.getText().toString().equals("00:00")) {
                            Toast.makeText(container.getContext(), "Invalid time range", Toast.LENGTH_SHORT).show();
                        } else {
                            String event = mEvent.getText().toString();
                            String mTime = FromTime.getText().toString() + " - " + ToTime.getText().toString();

                            listItem.get(position).changeItem(event, mTime);
                            mAdapter.notifyItemChanged(position);
                            saveData(container.getContext());
                            if(listItem.size() > 1) {
                                for(int i = 0; i < listItem.size(); i++) {
                                    for(int j = i + 1; j < listItem.size(); j++) {
                                        float first = Float.parseFloat(listItem.get(i).getTime().substring(0, 5).replace(":","."));
                                        float second = Float.parseFloat(listItem.get(j).getTime().substring(0, 5).replace(":","."));

                                        if(first > second) {
                                            Collections.swap(listItem, i, j);
                                            mAdapter.notifyItemMoved(i, j);
                                            mAdapter.notifyItemChanged(i);
                                            mAdapter.notifyItemChanged(j);
                                            saveData(container.getContext());
                                        }
                                    }
                                }
                            }
                            editDialog.dismiss();
                        }

                    }
                });
            }

            // Trigger alarm icon clicked
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSetAlarmClick(int position, ImageView AlarmBtn) {

                int hour = Integer.parseInt(listItem.get(position).getTime().substring(0, 2));
                int minute = Integer.parseInt(listItem.get(position).getTime().substring(3, 5));

                Calendar calendar = Calendar.getInstance();

                switch(mParam2) {
                    case "mon":
                        calendar.set(2021, 3, 19, hour, minute, 0);
                        break;
                    case "tue":
                        calendar.set(2021, 3, 20, hour, minute, 0);
                        break;
                    case "wed":
                        calendar.set(2021, 3, 21, hour, minute, 0);
                        break;
                    case "thu":
                        calendar.set(2021, 3, 22, hour, minute, 0);
                        break;
                    case "fri":
                        calendar.set(2021, 3, 23, hour, minute, 0);
                        break;
                    case "sat":
                        calendar.set(2021, 3, 24, hour, minute, 0);
                        break;
                    case "sun":
                        calendar.set(2021, 3, 25, hour, minute, 0);
                        break;
                }

                int id = position;

                switch ((int) AlarmBtn.getTag()) {
                    case R.drawable.notif_off:
                        AlarmBtn.setTag(R.drawable.notif_on);
                        AlarmBtn.setImageResource(R.drawable.notif_on);
                        startAlarm(calendar, listItem.get(position).getName(), id);
                        saveIcon(container.getContext(), listItem, position, R.drawable.notif_on);
                        break;
                    case R.drawable.notif_on:
                        AlarmBtn.setTag(R.drawable.notif_off);
                        AlarmBtn.setImageResource(R.drawable.notif_off);
                        cancelAlarm(id);
                        saveIcon(container.getContext(), listItem, position, R.drawable.notif_off);
                        break;
                }

            }
        });

        //add list
        addDialog = new Dialog(container.getContext());

        Button mAddBtn = v.findViewById(R.id.addButton);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.setContentView(R.layout.addlist_popup);
                Button mBack = addDialog.findViewById(R.id.back);
                mBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addDialog.dismiss();
                    }
                });
                addDialog.show();
                ImageView mFrom = addDialog.findViewById(R.id.from);
                ImageView mTo = addDialog.findViewById(R.id.to);
                TextView FromTime = addDialog.findViewById(R.id.timeFrom);
                TextView ToTime = addDialog.findViewById(R.id.timeTo);

                mFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timepicker = new TimePickerDialog(
                                container.getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        fHour = hourOfDay;
                                        fMinute = minute;

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(0, 0, 0, fHour, fMinute);
                                        FromTime.setText(DateFormat.format("HH:mm", calendar));
                                    }
                                }, 24, 0, true
                        );
                        timepicker.updateTime(fHour, fMinute);
                        timepicker.show();
                    }
                });

                mTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timepicker = new TimePickerDialog(
                                container.getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        tHour = hourOfDay;
                                        tMinute = minute;

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(0, 0, 0, tHour, tMinute);
                                        ToTime.setText(DateFormat.format("HH:mm", calendar));
                                    }
                                }, 24, 0, true
                        );
                        timepicker.updateTime(tHour, tMinute);
                        timepicker.show();
                    }
                });

                Button mAddToList = addDialog.findViewById(R.id.addToList);
                mAddToList.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        EditText mEvent = addDialog.findViewById(R.id.eventname);
                        TextView mFrom = addDialog.findViewById(R.id.timeFrom);
                        TextView mTo = addDialog.findViewById(R.id.timeTo);

                        if(mFrom.getText().toString().equals("From...") ||
                                mTo.getText().toString().equals("To...") ||
                                mEvent.getText().toString().equals("")) {
                            Toast.makeText(container.getContext(), "Event name and time required", Toast.LENGTH_SHORT).show();
                        } else if(Float.parseFloat(mFrom.getText().toString().replace(":","."))
                                > Float.parseFloat(mTo.getText().toString().replace(":",".")) && !mTo.getText().toString().equals("00:00")) {
                            Toast.makeText(container.getContext(), "Invalid time range", Toast.LENGTH_SHORT).show();
                        } else {
                            String event = mEvent.getText().toString();
                            String mTime = mFrom.getText().toString() + " - " + mTo.getText().toString();

                            listItem.add(new ListItem(event, mTime));
                            mAdapter.notifyItemInserted(listItem.size());
                            saveData(container.getContext());

                            if(listItem.size() > 1) {
                                for(int i = 0; i < listItem.size(); i++) {
                                    for(int j = i + 1; j < listItem.size(); j++) {
                                        float first = Float.parseFloat(listItem.get(i).getTime().substring(0, 5).replace(":","."));
                                        float second = Float.parseFloat(listItem.get(j).getTime().substring(0, 5).replace(":","."));

                                        if(first > second) {
                                            Collections.swap(listItem, i, j);
                                            mAdapter.notifyItemMoved(i, j);
                                            mAdapter.notifyItemChanged(i);
                                            mAdapter.notifyItemChanged(j);
                                            saveData(container.getContext());
                                        }
                                    }
                                }
                            }
                            addDialog.dismiss();
                        }
                    }
                });
            }
        });
        // Inflate the layout for this fragment
        return v;

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(listItem, fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            saveData(requireContext());

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    public void saveData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listItem);
        editor.putString("task list " + mParam2 + mParam1, json);
        editor.apply();
    }

    public void saveIcon(Context context, ArrayList listItem, int position, int icon) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("icon " + position + mParam1 + mParam2, icon);
        editor.commit();
    }

    public void resetIcon(Context context, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("icon " + position + mParam1 + mParam2, -1);
        editor.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c, String msg, int id) {
        AlarmManager alarmManager = (AlarmManager) getLayoutInflater().getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        intent.putExtra(EVENT_EXTRA, msg);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getLayoutInflater().getContext(), id, intent, PendingIntent.FLAG_ONE_SHOT);

        if(c.before(Calendar.getInstance())) {
            switch(mParam2) {
                case "mon":
                    c.add(Calendar.MONDAY, 1);
                    break;
                case "tue":
                    c.add(Calendar.TUESDAY, 1);
                    break;
                case "wed":
                    c.add(Calendar.WEDNESDAY, 1);
                    break;
                case "thu":
                    c.add(Calendar.THURSDAY, 1);
                    break;
                case "fri":
                    c.add(Calendar.FRIDAY, 1);
                    break;
                case "sat":
                    c.add(Calendar.SATURDAY, 1);
                    break;
                case "sun":
                    c.add(Calendar.SUNDAY, 1);
                    break;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
        }
    }

    private void cancelAlarm(int num) {
        AlarmManager alarmManager = (AlarmManager) getLayoutInflater().getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getLayoutInflater().getContext(), num, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.cancel(pendingIntent);
    }

}