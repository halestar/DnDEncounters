package net.kalinec.dndencounters.activities.custom_monsters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.custom_monsters.CustomMonster;
import net.kalinec.dndencounters.custom_monsters.CustomMonsterListAdapter;
import net.kalinec.dndencounters.custom_monsters.CustomMonsters;
import net.kalinec.dndencounters.dice.DiceRollerDialog;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterListAdapter;
import net.kalinec.dndencounters.monsters.Monsters;
import net.kalinec.dndencounters.players.Player;

import java.util.ArrayList;
import java.util.List;

public class ViewCustomMonstersFragment extends Fragment {

    private List<CustomMonster> monsterList, currentMonsterList;
    private CustomMonsterListAdapter monsterListAdapter;
    private RecyclerView CustomMonsterRv;
    private SearchView CustomMonsterSv;


    public ViewCustomMonstersFragment() {

    }
    public static ViewCustomMonstersFragment newInstance()
    {
        ViewCustomMonstersFragment fragment = new ViewCustomMonstersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if(monsterList == null)
            monsterList = CustomMonsters.getCustomMonsters(container.getContext());
        View v = inflater.inflate(R.layout.fragment_view_custom_monsters, container, false);
        CustomMonsterSv = v.findViewById(R.id.CustomMonsterSv);
        CustomMonsterRv = v.findViewById(R.id.CustomMonsterRv);
        currentMonsterList = monsterList;
        monsterListAdapter = new CustomMonsterListAdapter(getContext(), new RvClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                CustomMonster monster = currentMonsterList.get(position);
                Intent myIntent = new Intent(getContext(), EditCustomMonster.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(CustomMonster.PASSED_MONSTER, monster);
                myIntent.putExtras(bundle);
                startActivityForResult(myIntent, DiceRollerDialog.REQUEST_DICE_ROLL);
            }
        });
        monsterListAdapter.setMonsterList(monsterList);
        CustomMonsterRv.setAdapter(monsterListAdapter);
        CustomMonsterRv.setLayoutManager(new LinearLayoutManager(getContext()));

        CustomMonsterSv.setActivated(true);
        CustomMonsterSv.onActionViewExpanded();
        CustomMonsterSv.setIconified(false);
        CustomMonsterSv.clearFocus();
        CustomMonsterSv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            private List<CustomMonster> filter(List<CustomMonster> models, String query) {
                final String lowerCaseQuery = query.toLowerCase();

                final List<CustomMonster> filteredModelList = new ArrayList<>();
                for (CustomMonster model : models) {
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
                CustomMonsterRv.scrollToPosition(0);
                return true;
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        monsterListAdapter.notifyDataSetChanged();
    }
}
