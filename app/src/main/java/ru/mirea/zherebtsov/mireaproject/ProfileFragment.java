package ru.mirea.zherebtsov.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    private EditText editTextName;
    private EditText editTextEmail;
    private RadioGroup radioGroupClass;
    private String selectedClass;
    private SharedPreferences.Editor	editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences sharedPref = requireContext().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        editTextName = view.findViewById(R.id.editText_name);
        editTextEmail = view.findViewById(R.id.editText_email);
        radioGroupClass = view.findViewById(R.id.radioGroup_class);

        TextView name = view.findViewById(R.id.textView15);
        TextView rod = view.findViewById(R.id.textView155);
        TextView clas = view.findViewById(R.id.textView1555);
        name.setText("ИМЯ \n"+sharedPref.getString("name", ""));
        rod.setText("ПЛАНЕТА \n"+sharedPref.getString("email", ""));
        clas.setText("СТАТУС \n"+sharedPref.getString("class", ""));

        radioGroupClass.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.radioButton_jedi){
                selectedClass = "Джедай";
            } else if(checkedId == R.id.radioButton_sith) {
                selectedClass = "Ситх";
            }
        });
        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> saveProfileData());
        return view;
    }

    public void saveProfileData() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || selectedClass == null) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("class", selectedClass);

        editor.apply();
        Toast.makeText(requireContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show();
    }
}