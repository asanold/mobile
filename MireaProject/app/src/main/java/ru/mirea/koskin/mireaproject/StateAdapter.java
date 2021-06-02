package ru.mirea.koskin.mireaproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private ArrayList<State> histories;

    StateAdapter(Context context, List<State> histories) {
        this.histories = new ArrayList<>(histories);
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public StateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StateAdapter.ViewHolder holder, int position) {
        State history = histories.get(position);
        holder.getTextView_num().setText(Integer.toString(history.getNum()));
        holder.getEditText_multiline().setText(history.getText());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public void Update(ArrayList<State> histories){
        this.histories = new ArrayList<>(histories);
        notifyDataSetChanged();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView_num;
        final EditText editText_multiline;
        ViewHolder(View view){
            super(view);
            textView_num = (TextView)view.findViewById(R.id.textView_num);
            editText_multiline = (EditText) view.findViewById(R.id.editText_multiline);
        }

        public TextView getTextView_num(){
            return textView_num;
        }
        public EditText getEditText_multiline(){
            return editText_multiline;
        }
    }
}