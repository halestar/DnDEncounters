package net.kalinec.dndencounters.activities.spells;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import net.kalinec.dndencounters.MainActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.encounters.ViewEncounters;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.playsessions.PlaySession;
import net.kalinec.dndencounters.spells.Spell;
import net.kalinec.dndencounters.spells.SpellListAdapter;
import net.kalinec.dndencounters.spells.Spells;

import java.util.ArrayList;
import java.util.List;

public class SearchSpells extends AppCompatActivity {
    private List<Spell> spells, currentSpellList;
    private SpellListAdapter spellListAdapter;
    private RecyclerView SpellSearchRv;
    private SearchView SpellSearchSv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_spells);

        spells = currentSpellList = Spells.spellList(getApplicationContext());
        spellListAdapter = new SpellListAdapter(getApplicationContext(), new RvClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                Spell spell = currentSpellList.get(position);
                selectSpell(spell);
            }
        });
        spellListAdapter.setSpellList(spells);


        SpellSearchRv = (RecyclerView)findViewById(R.id.SpellSearchRv);
        SpellSearchRv.setAdapter(spellListAdapter);
        SpellSearchRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        SpellSearchSv = (SearchView)findViewById(R.id.SpellSearchSv);
        SpellSearchSv.setActivated(true);
        SpellSearchSv.onActionViewExpanded();
        SpellSearchSv.setIconified(false);
        SpellSearchSv.clearFocus();
        SpellSearchSv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            private List<Spell> filter(List<Spell> models, String query) {
                final String lowerCaseQuery = query.toLowerCase();

                final List<Spell> filteredModelList = new ArrayList<>();
                for (Spell model : models) {
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
                currentSpellList = filter(spells, newText);
                spellListAdapter.setSpellList(currentSpellList);
                SpellSearchRv.scrollToPosition(0);
                return true;
            }
        });
    }

    public void selectSpell(Spell spell)
    {
        Intent myIntent = new Intent(SearchSpells.this, ViewSpell.class);
        myIntent.putExtra(Spell.PASSED_SPELL, spell);
        SearchSpells.this.startActivity(myIntent);
    }
}
