package com.example.practice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import io.objectbox.Box;

public class FragmentAdd extends Fragment {

    EditText editText;
    Box<Note> noteBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view1  = inflater.inflate(R.layout.fragment_add, container, false);

        noteBox = ObjectBox.getBoxStore().boxFor(Note.class);
        editText = view1.findViewById(R.id.enterText);

        view1.findViewById(R.id.btnAdd).setOnClickListener(view -> {
            if (editText.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "PLease fill in the gaps", Toast.LENGTH_SHORT).show();
            } else {
                String text = editText.getText().toString();
                editText.getText().clear();
                Note note  = new Note();
                note.text = text;
                noteBox.put(note);

                EventBus.getDefault().postSticky(new MessageToastEvent());
            }

        });
        return view1;
    }
}