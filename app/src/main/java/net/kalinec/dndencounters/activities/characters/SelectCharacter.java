package net.kalinec.dndencounters.activities.characters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.characters.PcListAdapter;
import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.CharacterDao;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectCharacter extends DnDEncountersActivity {

    public static final int REQUEST_SELECT_CHARACTER = 40;
    private LiveData<List<Character>> pcs;
    private PcListAdapter pcListAdapter;
    private RecyclerView characterSearchRv;
    private SearchView characterSearchSv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_character);
        CharacterDao characterDao = AppDatabase.getDatabase(getApplicationContext()).characterDao();
        pcs = characterDao.getAllCharacters();
        pcListAdapter = new PcListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                Character c = pcListAdapter.get(position);
                selectPc(c);
            }
        });
        pcs.observe(this, new Observer<List<Character>>() {
            @Override
            public void onChanged(@Nullable List<Character> characters) {
                pcListAdapter.setCharacterList(characters);
            }
        });

        characterSearchRv = findViewById(R.id.characterSearchRv);
        characterSearchRv.setAdapter(pcListAdapter);
        characterSearchRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        characterSearchSv = (SearchView)findViewById(R.id.characterSearchSv);
        characterSearchSv.setActivated(true);
        characterSearchSv.onActionViewExpanded();
        characterSearchSv.setIconified(false);
        characterSearchSv.clearFocus();
        characterSearchSv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            private List<Character> filter(List<Character> models, String query) {
                final String lowerCaseQuery = query.toLowerCase();

                final List<Character> filteredModelList = new ArrayList<>();
                for (Character model : models) {
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
                List<Character> filteredPcs = filter(pcListAdapter.getCharacterList(), newText);
                pcListAdapter.setCharacterList(filteredPcs);
                characterSearchRv.scrollToPosition(0);
                return true;
            }
        });

    }

    private void selectPc(Character pc)
    {
        Intent data = new Intent();
        data.putExtra(Character.PASSED_CHARACTER, pc);
        setResult(RESULT_OK, data);
        finish();
    }
}
