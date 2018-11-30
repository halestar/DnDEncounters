package net.kalinec.dndencounters.activities.monster_tokens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokenListAdapter;
import net.kalinec.dndencounters.monster_tokens.MonsterTokens;

import java.util.List;

public class ViewMonsterTokens extends DnDEncountersActivity {

    private List<MonsterToken> monsterTokens;
    private RecyclerView MonsterTokenRv;
    private MonsterTokenListAdapter monsterTokenListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_monster_tokens);
	    Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MonsterTokenRv = findViewById(R.id.MonsterTokenRv);
        monsterTokens = MonsterTokens.getAllMonsterTokens(getApplicationContext());
        monsterTokenListAdapter = new MonsterTokenListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("ViewMonsterTokens", "passsing: " + monsterTokenListAdapter.get(position));
                Intent myIntent = new Intent(ViewMonsterTokens.this, EditMonsterToken.class);
                myIntent.putExtra(MonsterToken.PASSED_MONSTER_TOKEN, monsterTokenListAdapter.get(position));
                startActivityForResult(myIntent, EditMonsterToken.REQUEST_NEW_MONSTER_TOKEN);
            }
        });
        monsterTokenListAdapter.setMonsterList(monsterTokens);
        MonsterTokenRv.setAdapter(monsterTokenListAdapter);
        MonsterTokenRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	
	
	    FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMonsterToken();
            }
        });
    }

    public void addMonsterToken()
    {
        Intent myIntent = new Intent(ViewMonsterTokens.this, AddMonsterToken.class);
        startActivityForResult(myIntent, AddMonsterToken.REQUEST_NEW_MONSTER_TOKEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(resultCode == RESULT_OK) {
            monsterTokens = MonsterTokens.getAllMonsterTokens(getApplicationContext());
            monsterTokenListAdapter.setMonsterList(monsterTokens);
            MonsterTokenRv.scrollToPosition(0);
        }
    }

}
