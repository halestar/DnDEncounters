package net.kalinec.dndencounters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.kalinec.dndencounters.encounter.AdventureEncounter;
import net.kalinec.dndencounters.encounter.EncounterMonster;
import net.kalinec.dndencounters.lib.RvItemClickListener;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterTokenAssignerListAdapter;
import net.kalinec.dndencounters.playsessions.PlaySession;

import java.util.List;

public class AssignMonsterTokens extends AppCompatActivity {
    public static final int REQUEST_ENCOUNTER_MONSTERS = 68;

    private AdventureEncounter adventureEncounter;
    private List<Monster> monsters;
    private RecyclerView MonsterTokenAssignerRv;
    private MonsterTokenAssignerListAdapter monsterTokenAssignerListAdapter;
    private List<EncounterMonster> encounterMonsters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        adventureEncounter = (AdventureEncounter)bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
        setContentView(R.layout.activity_assign_monster_tokens);

        monsters = adventureEncounter.getEncounter().getMonsters();
        monsterTokenAssignerListAdapter = new MonsterTokenAssignerListAdapter(getApplicationContext(), new RvItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //code when assighning a monster a token

            }
        });
        monsterTokenAssignerListAdapter.setMonsterList(monsters);
        //assign spinner adapter

        MonsterTokenAssignerRv = findViewById(R.id.MonsterTokenAssignerRv);
        MonsterTokenAssignerRv.setAdapter(monsterTokenAssignerListAdapter);
        MonsterTokenAssignerRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }
}
