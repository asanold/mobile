package ru.mirea.koskin.mireaproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ru.mirea.koskin.mireaproject.ui.HistoryDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link History#newInstance} factory method to
 * create an instance of this fragment.
 */
public class History extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private StateAdapter adapter;
    ArrayList<State> histories = new ArrayList<State>();

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private String fileName = "histories";

    private ShowDialog activity_ShowDialog;

    public History() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment History.
     */
    // TODO: Rename and change types and number of parameters
    public static History newInstance(String param1, String param2) {
        History fragment = new History();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try
        {
            //activity_ShowDialog = (ShowDialog)context;
        }
        catch(ClassCastException e)
        {
            Log.e(this.getClass().getSimpleName(), "ShowDialog interface needs to be implemented by Activity.", e);
            throw e;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.recyclerView);

        setInitialData();
        adapter = new StateAdapter(getActivity(), histories);
        recyclerView.setAdapter(adapter);

        getView().findViewById(R.id.fabtn_Add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }

    public void openDialog() {
        HistoryDialogFragment dialog = new HistoryDialogFragment();
        Log.d("History", getActivity().getClass().getSimpleName());
        dialog.show(this.getChildFragmentManager(), "dialog fragment");
        //dialog.setTargetFragment(this, 1);
        //activity_ShowDialog.showDialog(dialog);
    }

    public ArrayList<State> getListFromFile() {
        FileInputStream fis = null;
        ArrayList<State> arr = new ArrayList<>();
        try {
            fis = getActivity().openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                State.setNum_All(ois.readInt());
                arr = (ArrayList<State>) ois.readObject();
            } catch (IOException ex){
                Log.d("getListFromFile_IOexc", ex.getMessage() + " "+ arr);
            }
            Log.d(LOG_TAG, "Download end. "+histories.size());
        } catch (Exception ex) {
            Log.d("getListFromFile", ex.getMessage() + " "+ arr);
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fis != null)
                    fis.close();
                Log.d("getListFromFile_retArr", ""+arr);
                return arr;
            } catch (IOException ex) {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    public void setListToFile(){
        FileOutputStream fos = null;
        try {
            fos = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeInt(State.getNum_All());
            oos.writeObject(histories);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void applyTexts(String multitext) {
        Log.d("History", "Apply text OK");
        histories.add(new State(multitext));
        setListToFile();
        adapter.Update(histories);
    }

    private void setInitialData() {
        histories = getListFromFile();
        if (histories == null){
            histories = new ArrayList<State>();
        }
    }

    public interface ShowDialog {
        void showDialog(DialogFragment dialogFragment);
    }
}