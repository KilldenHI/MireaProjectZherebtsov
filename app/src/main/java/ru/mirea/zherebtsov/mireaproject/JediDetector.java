package ru.mirea.zherebtsov.mireaproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.Manifest;


import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;


import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JediDetector#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JediDetector extends Fragment {



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public JediDetector() {
        // Required empty public constructor
    }

    public static JediDetector newInstance(String param1, String param2) {
        JediDetector fragment = new JediDetector();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private ImageView mImageView;
    private ProgressDialog mProgressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jedi_detector, container, false);

        Button captureButton = view.findViewById(R.id.button);
        mImageView = view.findViewById(R.id.imageView);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getActivity(), "Camera permission is required to take pictures.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            showLoadingView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoadingView();
                    showCaption();
                }
            }, 6000);
        }
    }

    private void showLoadingView() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Определение количество мидихлориан");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void hideLoadingView() {
        mProgressDialog.dismiss();
    }

    private void showCaption() {
        double mChislo = (Math.random() * 100) + 1;
        TextView layout = getView().findViewById(R.id.textView12);
        if (mChislo >= 50){
            layout.setText("М число равно "+ mChislo+ " Обьект чуствительный к силе!");
            layout.setTextColor(Color.GREEN);
        } else if ((mChislo < 50) & (mChislo >= 10)){
            layout.setText("М число равно "+ mChislo+ " Обьект НЕ чуствительный к силе!");
            layout.setTextColor(Color.YELLOW);
        }
        else if (mChislo < 10){
            layout.setText("М число равно "+ mChislo+ " Обьект имеет аномально малое число М!");
            layout.setTextColor(Color.RED);
        }


    }


}