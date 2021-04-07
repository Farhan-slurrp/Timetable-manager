package com.example.timetablemanager;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayFragment extends Fragment {
    Dialog addDialog, editDialog;
    int fHour, fMinute, tHour, tMinute;
    ArrayList<ListItem> listItem;
    String mUsername = "";

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
        String json = sharedPreferences.getString("task list " + mParam2 + mUsername, null);
        Type type = new TypeToken<ArrayList<ListItem>>() {}.getType();
        listItem = gson.fromJson(json, type);

        if(listItem == null) {
            listItem = new ArrayList<>();
        }

        mRecycleView = v.findViewById(R.id.recyclerView);
        mRecycleView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mAdapter = new ListAdapter(listItem);

        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecycleView);

        mAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                listItem.remove(position);
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
                FromTime.setText(timeList[0] + " " + timeList[1]);
                ToTime.setText(timeList[3] + " " + timeList[4]);


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
                                        FromTime.setText(DateFormat.format("hh:mm aa", calendar));
                                    }
                                }, 12, 0, false
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
                                        ToTime.setText(DateFormat.format("hh:mm aa", calendar));
                                    }
                                }, 12, 0, false
                        );
                        timepicker.updateTime(tHour, tMinute);
                        timepicker.show();
                    }
                });

                Button mAddToList = editDialog.findViewById(R.id.addToList);
                mAddToList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String event = mEvent.getText().toString();
                        String mTime = FromTime.getText().toString() + " - " + ToTime.getText().toString();

                        listItem.get(position).changeItem(event, mTime);
                        mAdapter.notifyItemChanged(position);
                        saveData(container.getContext());
                        editDialog.dismiss();
                    }
                });
            }
        });

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
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        fHour = hourOfDay;
                                        fMinute = minute;

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(0, 0, 0, fHour, fMinute);
                                        FromTime.setText(DateFormat.format("hh:mm aa", calendar));
                                    }
                                }, 12, 0, false
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
                                        ToTime.setText(DateFormat.format("hh:mm aa", calendar));
                                    }
                                }, 12, 0, false
                        );
                        timepicker.updateTime(tHour, tMinute);
                        timepicker.show();
                    }
                });

                Button mAddToList = addDialog.findViewById(R.id.addToList);
                mAddToList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText mEvent = addDialog.findViewById(R.id.eventname);
                        TextView mFrom = addDialog.findViewById(R.id.timeFrom);
                        TextView mTo = addDialog.findViewById(R.id.timeTo);

                        if(mFrom.getText().toString().equals("From...") ||
                                mTo.getText().toString().equals("To...") ||
                                mEvent.getText().toString().equals("")) {
                            Toast.makeText(container.getContext(), "Time and event name required", Toast.LENGTH_SHORT).show();
                        } else {
                            String event = mEvent.getText().toString();
                            String mTime = mFrom.getText().toString() + " - " + mTo.getText().toString();

                            listItem.add(new ListItem(event, mTime));
                            mAdapter.notifyItemInserted(listItem.size());
                            saveData(container.getContext());
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
        editor.putString("task list " + mParam2 + mUsername, json);
        editor.apply();
    }

    public void getUsername(String username) {
        mUsername = username;
    }
}