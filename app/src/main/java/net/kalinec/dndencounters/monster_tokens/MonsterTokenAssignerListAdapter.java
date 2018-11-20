package net.kalinec.dndencounters.monster_tokens;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.lib.RvItemClickListener;
import net.kalinec.dndencounters.monsters.Monster;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MonsterTokenAssignerListAdapter extends RecyclerView.Adapter<MonsterTokenAssignerListAdapter.MonsterTokenAssigngerViewHolder>
{
	private static final Comparator<Monster> ALPHABETICAL_COMPARATOR = new Comparator<Monster>() {
		@Override
		public int compare(Monster a, Monster b) {
			return a.getName().compareTo(b.getName());
		}
	};

	private final List<AdventureEncounterMonster> monsterList = new ArrayList<>();

	private LayoutInflater layoutInflater;
	private Context context;
	private RvItemClickListener mListener;
	private MonsterTokenSpinnerAdapter monsterTokenSpinnerAdapter;

	public MonsterTokenAssignerListAdapter(Context context, RvItemClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.mListener = listener;
		this.monsterTokenSpinnerAdapter = new MonsterTokenSpinnerAdapter(context);
	}

	public void setMonsterList(List<AdventureEncounterMonster> monsterList)
	{
		this.monsterList.clear();
		this.monsterList.addAll(monsterList);
		notifyDataSetChanged();
	}
	
	public AdventureEncounterMonster get(int position)
	{
		return this.monsterList.get(position);
	}

	@NonNull
	@Override
	public MonsterTokenAssignerListAdapter.MonsterTokenAssigngerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_monster_token_assigner, parent, false);
		return new MonsterTokenAssignerListAdapter.MonsterTokenAssigngerViewHolder(itemView, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull MonsterTokenAssignerListAdapter.MonsterTokenAssigngerViewHolder holder, int position)
	{
		if (monsterList == null)
		{
			return;
		}
		final AdventureEncounterMonster monster = monsterList.get(position);
		if (monster != null)
		{
			holder.encounterMonsterNameTv.setText(monster.getMonster().getName());
			holder.monsterTokenSp.setAdapter(monsterTokenSpinnerAdapter);
		}
	}
	
	@Override
	public int getItemCount()
	{
		if (monsterList == null)
		{
			return 0;
		}
		else
		{
			return monsterList.size();
		}
	}
	
	static class MonsterTokenAssigngerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener
	{
		private TextView encounterMonsterNameTv;
		private Spinner monsterTokenSp;
		private RvItemClickListener mListener;
		
		public MonsterTokenAssigngerViewHolder(View itemView, RvItemClickListener listener)
		{
			super(itemView);
			encounterMonsterNameTv = itemView.findViewById(R.id.encounterMonsterNameTv);
			monsterTokenSp = itemView.findViewById(R.id.monsterTokenSp);
			mListener = listener;
			monsterTokenSp.setOnItemSelectedListener(this);
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			mListener.onClick(view, this.getAdapterPosition(), position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}
}
