package net.kalinec.dndencounters.activities.monsters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterListAdapter;
import net.kalinec.dndencounters.monsters.Monsters;

import java.util.ArrayList;
import java.util.List;

public class SelectMonster extends DnDEncountersActivity
{
	public static final int REQUEST_SELECT_MONSTER = 1;
	private List<Monster> monsters, currentMonsterList;
	private MonsterListAdapter monsterListAdapter;
	private RecyclerView monsterSearchRv;
	private SearchView monsterSearchSv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_monster);
		
		monsters = currentMonsterList = Monsters.monsterList(getApplicationContext());
		monsterListAdapter = new MonsterListAdapter(getApplicationContext(), new RvClickListener()
		{
			@Override
			public void onClick(View view, int position)
			{
				Monster monster = currentMonsterList.get(position);
				selectMonster(monster);
			}
		});
		monsterListAdapter.setMonsterList(monsters);
		
		
		monsterSearchRv = (RecyclerView)findViewById(R.id.monsterSearchRv);
		monsterSearchRv.setAdapter(monsterListAdapter);
		monsterSearchRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		
		
		monsterSearchSv = (SearchView)findViewById(R.id.SpellSearchSv);
		monsterSearchSv.setActivated(true);
		monsterSearchSv.onActionViewExpanded();
		monsterSearchSv.setIconified(false);
		monsterSearchSv.clearFocus();
		monsterSearchSv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
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
				currentMonsterList = filter(monsters, newText);
				monsterListAdapter.setMonsterList(currentMonsterList);
				monsterSearchRv.scrollToPosition(0);
				return true;
			}
		});
	}
	
	public void selectMonster(Monster monster)
	{
		Intent data = new Intent();
		data.putExtra(Monster.PASSED_MONSTER, monster);
		setResult(RESULT_OK, data);
		finish();
	}
}
