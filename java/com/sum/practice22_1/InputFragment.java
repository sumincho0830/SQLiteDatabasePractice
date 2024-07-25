package com.sum.practice22_1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InputFragment extends Fragment {
    EditText edtTitle, edtAuthor, edtContent;
    Button btnSave;

    OnDatabaseCallback callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (OnDatabaseCallback) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_input, container, false);

        edtTitle = rootView.findViewById(R.id.edtTitle);
        edtAuthor = rootView.findViewById(R.id.edtAuthor);
        edtContent = rootView.findViewById(R.id.edtContent);
        btnSave = rootView.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString();
                String author = edtAuthor.getText().toString();
                String contents = edtContent.getText().toString();

                callback.insert(title, author, contents); //메서드를 통해 MainActivity로 정보를 넘겨주고, MainActivity를 통하여 액티비티끼리 소통한다
                Toast.makeText(getActivity(), "책 정보를 추가했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
