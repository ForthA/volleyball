package com.example.volleyball;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultGameFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Round> list;
    private RecyclerView recyclerView;
    private AdapterRound adapter;

    public ResultGameFragment() {
        // Required empty public constructor

        list = new ArrayList<>();
        list.add(new Round(1, 1590077643, false, true));
        list.add(new Round(2, 1590077700, true, false));
        list.add(new Round(3, 1590077750, false, true));
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultGameFragment newInstance(String param1, String param2) {
        ResultGameFragment fragment = new ResultGameFragment();
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
        Log.d("list", "" + list.size());
        View view = inflater.inflate(R.layout.fragment_result_game, container, false);

        recyclerView = view.findViewById(R.id.recycler_round);
        adapter = new AdapterRound(list);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}