package net.kalinec.dndencounters.activities.monster_tokens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.lib.RvItemClickListener;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokenAssignerListAdapter;
import net.kalinec.dndencounters.monster_tokens.MonsterTokens;

import java.util.ArrayList;
import java.util.List;

public class AssignMonsterTokens extends DnDEncountersActivity {
    public static final int REQUEST_ENCOUNTER_MONSTERS = 68;

    private AdventureEncounter adventureEncounter;
    private List<Monster> monsters;
    private RecyclerView MonsterTokenAssignerRv;
    private MonsterTokenAssignerListAdapter monsterTokenAssignerListAdapter;
    private ArrayList<AdventureEncounterMonster> encounterMonsters;
    private List<MonsterToken> monsterTokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        adventureEncounter = (AdventureEncounter)bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
        setContentView(R.layout.activity_assign_monster_tokens);

        monsters = adventureEncounter.getEncounter().getMonsters();
        monsterTokens = MonsterTokens.getAllMonsterTokens(getApplicationContext());
        encounterMonsters = new ArrayList<>();
        for(Monster m: monsters)
            encounterMonsters.add(new AdventureEncounterMonster(m, monsterTokens.get(0)));
        monsterTokenAssignerListAdapter = new MonsterTokenAssignerListAdapter(getApplicationContext(), new RvItemClickListener() {
            @Override
            public void onClick(View view, int holder_position, int spinner_position)
            {
                AdventureEncounterMonster
                        monster = monsterTokenAssignerListAdapter.get(holder_position);
                MonsterToken mToken = monsterTokens.get(spinner_position);
                AdventureEncounterMonster
                        newMonster = new AdventureEncounterMonster(monster.getMonster(), mToken);
                encounterMonsters.set(holder_position, newMonster);
                Log.d("AssignMonsterTokens", "Added to encounterMonster. Now: " + encounterMonsters);
            }
        });
        monsterTokenAssignerListAdapter.setMonsterList(encounterMonsters);
        //assign spinner adapter

        MonsterTokenAssignerRv = findViewById(R.id.MonsterTokenAssignerRv);
        MonsterTokenAssignerRv.setAdapter(monsterTokenAssignerListAdapter);
        MonsterTokenAssignerRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }
    
    public void completeAssignment(View v)
    {
        Intent data = new Intent();
        data.putExtra(AdventureEncounterMonster.PASSED_ENCOUNTER_MONSTERS, encounterMonsters);
        setResult(RESULT_OK, data);
        finish();
    }
}
