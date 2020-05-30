package com.example.volleyball.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.media.SoundPool;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.volleyball.GameFragment;
import com.example.volleyball.R;
import com.google.android.material.textfield.TextInputEditText;

public class MainFragment extends Fragment implements View.OnClickListener {

    TextInputEditText team1;
    TextInputEditText team2;

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        Button rsls = v.findViewById(R.id.btnResults);
        Button str = v.findViewById(R.id.btnStart);
        team1 = v.findViewById(R.id.txtTeam1);
        team2 = v.findViewById(R.id.txtTeam2);

        str.setOnClickListener(this);
        rsls.setOnClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btnResults:
                Navigation.findNavController(v).navigate(R.id.mainToResultGames);
                break;
            case R.id.btnStart:
                String team1Name = team1.getText().toString();
                String team2Name = team2.getText().toString();

                if (team1Name.equals("") || team2Name.equals("")) {
                    return;
                }
                Bundle b = new Bundle();
                b.putString(GameFragment.KEY_TEAM1, team1Name);
                b.putString(GameFragment.KEY_TEAM2, team2Name);
                Navigation.findNavController(v).navigate(R.id.mainToMatch, b);
        }
    }
}
