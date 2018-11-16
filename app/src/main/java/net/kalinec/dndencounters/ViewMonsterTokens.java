package net.kalinec.dndencounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.MonsterToken;
import net.kalinec.dndencounters.monsters.MonsterTokenListAdapter;
import net.kalinec.dndencounters.monsters.MonsterTokens;

import java.util.List;

public class ViewMonsterTokens extends AppCompatActivity {

    private List<MonsterToken> monsterTokens;
    private RecyclerView MonsterTokenRv;
    private MonsterTokenListAdapter monsterTokenListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_monster_tokens);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MonsterTokenRv = findViewById(R.id.MonsterTokenRv);
        monsterTokens = MonsterTokens.getAllMonsterTokens(getApplicationContext());
        monsterTokenListAdapter = new MonsterTokenListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        MonsterTokenRv.setAdapter(monsterTokenListAdapter);
        MonsterTokenRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        ViewMonsterTokens.this.startActivity(myIntent);
    }

}