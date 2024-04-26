package ru.mirea.zherebtsov.mireaproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;



public class TextConverter extends Fragment {

    private static final int REQUEST_CODE_PICK_TXT = 100;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 101;
    private boolean isWork = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_converter, container, false);
        Button pickFileButton = view.findViewById(R.id.b1);

        int storagePermissionStatus = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if	(storagePermissionStatus ==	PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
        }


        pickFileButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
            } else {
                openFileChooser();
            }
        });
        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select a TXT file"), REQUEST_CODE_PICK_TXT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_TXT && resultCode == getActivity().RESULT_OK && data != null) {
            Uri txtUri = data.getData();
            convertTxtToDocx(txtUri);
        }
    }

    private void convertTxtToDocx(Uri txtUri) {
        try {
            InputStream is = getActivity().getContentResolver().openInputStream(txtUri);
            Scanner scanner = new Scanner(is);
            XWPFDocument docx = new XWPFDocument();
            XWPFParagraph par = docx.createParagraph();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                par.createRun().setText(line);
            }
            File name = new File(txtUri.getPath());
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File outputFile = new File(path, name.getName().substring(0, name.getName().length() - 4) +"_document.docx");
            //File outputFile = new File(new File(txtUri.getPath()).getParent(), name.getName().substring(0, name.getName().length() - 4) +"_document.docx");
            FileOutputStream out = new FileOutputStream(outputFile);
            docx.write(out);
            out.close();
            is.close();
            openFile(outputFile);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Error converting file: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("FileAccess", "Error accessing file", e);
        }
    }

    private void openFile(File file) {
        Uri uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Open with"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
}

