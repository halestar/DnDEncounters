package net.kalinec.dndencounters.activities.custom_monsters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.lib.OnMonsterSelectedListener;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterListAdapter;
import net.kalinec.dndencounters.monsters.Monsters;

import java.util.ArrayList;
import java.util.List;

public class ViewMonstersFragment extends Fragment {

    private static final String SELECTOR_LISTENER = "SELECTOR_LISTENER";
    private List<Monster> monsterList, currentMonsterList;
    private MonsterListAdapter monsterListAdapter;
    private RecyclerView MonsterRv;
    private SearchView MonsterSv;
    private OnMonsterSelectedListener mListener;


    public ViewMonstersFragment() {

    }

    public static ViewMonstersFragment newInstance(OnMonsterSelectedListener mListener)
    {
        ViewMonstersFragment fragment = new ViewMonstersFragment();
        Bundle args = new Bundle();
        args.putSerializable(SELECTOR_LISTENER, mListener);
        fragment.setArguments(args);
        return fragment;
    }

    public static ViewMonstersFragment newInstance()
    {
        ViewMonstersFragment fragment = new ViewMonstersFragment();
        Bundle args = new Bundle();
        args.putSerializable(SELECTOR_LISTENER, null);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListener = (OnMonsterSelectedListener)getArguments().getSerializable(SELECTOR_LISTENER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if(monsterList == null)
            monsterList = Monsters.monsterList(container.getContext());
        View v = inflater.inflate(R.layout.fragment_view_monsters, container, false);
        MonsterSv = v.findViewById(R.id.CustomMonsterSv);
        MonsterRv = v.findViewById(R.id.MonsterRv);
        currentMonsterList = monsterList;
        monsterListAdapter = new MonsterListAdapter(getContext(), new RvClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                Monster monster = currentMonsterList.get(position);
                if(mListener != null)
                    mListener.onMonsterSelected(monster);
            }
        });
        monsterListAdapter.setMonsterList(monsterList);
        MonsterRv.setAdapter(monsterListAdapter);
        MonsterRv.setLayoutManager(new LinearLayoutManager(getContext()));

        MonsterSv.setActivated(true);
        MonsterSv.onActionViewExpanded();
        MonsterSv.setIconified(false);
        MonsterSv.clearFocus();
        MonsterSv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            private List<Monster> filter(List<Monster> models, String query) {
                final String lowerCaseQuery = query.toLowerCase();

                final List<Monster> filteredModelList = new ArrayList<>();
                for (Monster model : models) {
                    final String text = model.getName().toLowerCase();
                    if (text.contains(lowerCaseQuery)) {
                        filteredModelList.add(model);
                    }
                }
                return filteredModelList;
            }

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                currentMonsterList = filter(monsterList, newText);
                monsterListAdapter.setMonsterList(currentMonsterList);
                MonsterRv.scrollToPosition(0);
                return true;
            }
        });
        return v;
    }
}
