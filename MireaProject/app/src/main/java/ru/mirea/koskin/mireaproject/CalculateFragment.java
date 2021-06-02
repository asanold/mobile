package ru.mirea.koskin.mireaproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalculateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalculateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalculateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculateFragment newInstance(String param1, String param2) {
        CalculateFragment fragment = new CalculateFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_calculate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.signs));
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        getView().findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float val1 = 0;
                float val2 = 0;
                String val = "0";
                try {
                    val1 = Float.parseFloat(((EditText) getView().findViewById(R.id.edit_Val1)).getText().toString());
                } catch (NumberFormatException e) {
                    val = "error";
                }
                try {
                    val2 = Float.parseFloat(((EditText) getView().findViewById(R.id.edit_Val2)).getText().toString());
                } catch (NumberFormatException e) {
                    val = "error";
                }


                if (val != "error") {
                    int itemId = (int) spinner.getSelectedItemId();
                    switch (itemId) {
                        case 0:
                            val = Float.toString(val1 * val2);
                            break;
                        case 1:
                            val = Float.toString(val1 / val2);

                            break;
                        case 2:
                            val = Float.toString(val1 + val2);
                            break;
                        case 3:
                            val = Float.toString(val1 - val2);
                            break;
                        default:
                            break;
                    }
                }
                ((TextView)getView().findViewById(R.id.textView3)).setText(val);
            }
        });

    }

}