package com.example.practice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import org.greenrobot.eventbus.EventBus;

import java.util.List;


import io.objectbox.Box;

public class Adapter extends RecyclerView.Adapter<AdapterVH> {
    public List<Note> items;
    private boolean isSelectionClicked;

    public Adapter(List<Note> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public AdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new AdapterVH(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterVH holder, @SuppressLint("RecyclerView") int position) {

        holder.textView.setText(items.get(position).text);
        holder.checkBox.setChecked(items.get(position).status);

        if (isSelectionClicked) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.deleteImg.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.deleteImg.setVisibility(View.VISIBLE);
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            Note note = items.get(holder.getAdapterPosition());

            note.status = isChecked;
            EventBus.getDefault().postSticky(new VisibilityEvent());

        });

        holder.cardView.setOnClickListener(v -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.edititems);

            Button btnUpdate = dialog.findViewById(R.id.btnAddUpdated);
            EditText editText = dialog.findViewById(R.id.updateText);

            editText.setText(items.get(position).text);

            btnUpdate.setOnClickListener(v1 -> {

                String text = "";

                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(dialog.getContext(), "PLease fill in the gaps", Toast.LENGTH_SHORT).show();
                } else {
                    text = editText.getText().toString();
                }
                items.get(position).setText(text);
//                items.set(position, new Note(text));
                notifyItemChanged(position);
                dialog.dismiss();
            });
            dialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean isSelectionClicked() {
        return isSelectionClicked;
    }

    public void setSelectionClicked(boolean selectionClicked) {
        isSelectionClicked = selectionClicked;
    }
}

class AdapterVH extends RecyclerView.ViewHolder {
    TextView textView;
    ImageView deleteImg;
    Box<Note> noteBox;
    CheckBox checkBox;
    CardView cardView;
    EditText editText;

    public AdapterVH(@NonNull View itemView, Adapter adapter) {
        super(itemView);

        editText = itemView.findViewById(R.id.updateText);
        cardView = itemView.findViewById(R.id.cardView);

        textView = itemView.findViewById(R.id.text);
        deleteImg = itemView.findViewById(R.id.deleteImg);
        noteBox = ObjectBox.getBoxStore().boxFor(Note.class);

        checkBox = itemView.findViewById(R.id.check_box);

        deleteImg.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Are you sure to Delete?");
            builder.setMessage("Click any button to continue");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                Note note = adapter.items.get(getAdapterPosition());
                noteBox.remove(note);
                adapter.items.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            });
            builder.setNegativeButton("No", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }
}
