package com.example.practice;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import io.objectbox.Box;

public class FragmentTodos extends Fragment {

    Box<Note> noteBox;
    List<Note> items = new LinkedList<>();
    Adapter adapter;
    RecyclerView recyclerView;

    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todos, container, false);

        noteBox = ObjectBox.getBoxStore().boxFor(Note.class);
        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter(items);

        items.addAll(noteBox.getAll());
        recyclerView.setAdapter(adapter);
        adapter.notifyItemInserted(items.size());
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventToast(MessageToastEvent messageToastEvent) {
        EventBus.getDefault().removeStickyEvent(messageToastEvent);
        adapter.items = noteBox.getAll();
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventVisible(VisibilityEvent visibilityEvent) {
        Log.d("zeka1", "eventVisible: ");
        EventBus.getDefault().removeStickyEvent(visibilityEvent);
        for (int i = 0; i < adapter.items.size(); i++) {
            Log.d("zeka2", "eventVisible: ");
            if (adapter.items.get(i).status) {
                Log.d("zeka3", "eventVisible: ");
                EventBus.getDefault().postSticky(new ShowEvent(true));
                break;
            } else if (adapter.items.size() -1 == i) {
                Log.d("zeka4", "eventVisible: ");
                EventBus.getDefault().postSticky(new ShowEvent(false));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.edit) {
            item.setChecked(!item.isChecked());
            adapter.setSelectionClicked(item.isChecked());
            adapter.notifyDataSetChanged();
        }

        if (id == R.id.delete) {

            for (Note n : adapter.items) {
                if (n.status) {
                    noteBox.remove(n);
                }
            }
            eventToast(new MessageToastEvent());
        }
        return super.onOptionsItemSelected(item);
    }
}