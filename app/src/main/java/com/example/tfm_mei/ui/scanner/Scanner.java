package com.example.tfm_mei.ui.scanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tfm_mei.MainActivity;
import com.example.tfm_mei.R;


public class Scanner extends Fragment {

    private static final int CAMERA_PERMISSION_CODE=223;

    Button btnScanBarcode;
    ImageView ivBarcode;

    public Scanner() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scanner, container, false);
        addData (v);
        return v;
    }

    private void addData(View v) {
        btnScanBarcode = v.findViewById(R.id.btnScanBarcode);
        ivBarcode = v.findViewById(R.id.ivBarcode);

        btnScanBarcode.setOnClickListener(v1 -> ((MainActivity)getActivity()).scanCode());
    }
}