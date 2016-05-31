package com.example.adeshpa.nytimessearch.models;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.adeshpa.nytimessearch.R;

/**
 * Created by adeshpa on 5/30/16.
 */
public class FilterDialogFragment extends DialogFragment {
    private EditText mEditText;

    // Defines the listener interface
    public interface FilterDialogListener {
        void onFinishEditDialog(String inputText);
    }

    public FilterDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter, container);
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);

        // set this instance as callback for editor action
        Button btnOk = (Button) view.findViewById(R.id.dialog_ok);
        btnOk.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {
                sendBackResult(v);
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.dialog_cancel);
        btnCancel.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {
                done(v);
            }
        });

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Please enter username");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult(View view) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        FilterDialogListener listener = (FilterDialogListener) getActivity();
        listener.onFinishEditDialog(mEditText.getText().toString());
        dismiss();
    }

    public void done(View view) {
        dismiss();
    }


}
