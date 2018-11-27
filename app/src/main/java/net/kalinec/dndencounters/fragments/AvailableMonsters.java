package net.kalinec.dndencounters.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonsterListAdapter;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.ArrayList;
import java.util.List;

public class AvailableMonsters extends Fragment {

    private static final String AVAILABLE_MONSTERS = "AVAILABLE_MONSTERS";

    private ArrayList<AdventureEncounterMonster> availableMonsters = new ArrayList<>();
    private RecyclerView availableMonstersRv;
    private AdventureEncounterMonsterListAdapter monsterListAdapter;
    private View monsterView;

    public AvailableMonsters() {
        // Required empty public constructor
    }

    public static AvailableMonsters newInstance(ArrayList<AdventureEncounterMonster> availableMonsters) {
        AvailableMonsters fragment = new AvailableMonsters();
        Bundle args = new Bundle();
        args.putSerializable(AVAILABLE_MONSTERS, availableMonsters);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            availableMonsters = (ArrayList<AdventureEncounterMonster>)getArguments().getSerializable(AVAILABLE_MONSTERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        monsterView = inflater.inflate(R.layout.fragment_available_monsters, container, false);
        monsterListAdapter = new AdventureEncounterMonsterListAdapter(getContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                AdventureEncounterMonster selectedMonster = availableMonsters.get(position);
                mListener.onMonsterSelectedListener(selectedMonster);
            }
        });
        monsterListAdapter.setMonsterList(availableMonsters);
        availableMonstersRv = monsterView.findViewById(R.id.availableMonstersRv);
        availableMonstersRv.setAdapter(monsterListAdapter);
        availableMonstersRv.setLayoutManager(new LinearLayoutManager(getContext()));
        return monsterView;
    }

    public interface OnMonsterSelectedListener
    {
        public void onMonsterSelectedListener(AdventureEncounterMonster selectedMonster);
    }
    private OnMonsterSelectedListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnMonsterSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

}
