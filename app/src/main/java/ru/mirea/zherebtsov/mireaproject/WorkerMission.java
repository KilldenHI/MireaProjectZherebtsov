package ru.mirea.zherebtsov.mireaproject;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.work.Constraints.Builder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerMission#newInstance} factory method to
 * create an instance of this fragment.
 */


public class WorkerMission extends Fragment {
    private Button button;
    private TextView textView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerMission() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerMission.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkerMission newInstance(String param1, String param2) {
        WorkerMission fragment = new WorkerMission();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_worker_mission, container, false);
        button = rootView.findViewById(R.id.button2);
        textView = rootView.findViewById(R.id.textView11);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNotificationWorker();
            }
        });

        return rootView;
    }

    private void startNotificationWorker() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .addTag(NotificationWorker.WORK_TAG)
                .build();



        WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        String currentDateAndTime = sdf.format(new Date(System.currentTimeMillis()));
                        textView.setText("Статус: Проект завершон! Время окончания строительства: " + currentDateAndTime);
                        textView.setTextColor(Color.GREEN);
                    } else {
                        textView.setText("Статус: Фаза строительства!");
                        textView.setTextColor(Color.YELLOW);
                    }
                });
    }
}