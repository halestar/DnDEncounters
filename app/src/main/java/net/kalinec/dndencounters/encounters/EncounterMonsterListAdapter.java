package net.kalinec.dndencounters.encounters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.Monster;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EncounterMonsterListAdapter
		extends RecyclerView.Adapter<EncounterMonsterListAdapter.MonsterViewHolder>
{
	private static final Comparator<Monster> ALPHABETICAL_COMPARATOR = new Comparator<Monster>() {
		@Override
		public int compare(Monster a, Monster b) {
			return a.getName().compareTo(b.getName());
		}
	};
	
	private List<Monster> monsterList = new ArrayList<>();
	
	private LayoutInflater layoutInflater;
	private Context context;
	private RvClickListener mListener;
	
	public EncounterMonsterListAdapter(Context context,RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		mListener = listener;
	}
	
	public void setMonsterList(List<Monster> monsterList)
	{
		this.monsterList = monsterList;
		notifyDataSetChanged();
	}
	
	public void add(Monster monster)
	{
		monsterList.add(monster);
		monsterList.sort(ALPHABETICAL_COMPARATOR);
		notifyDataSetChanged();
	}
	
	public void remove(Monster monster)
	{
		monsterList.remove(monster);
		monsterList.sort(ALPHABETICAL_COMPARATOR);
		notifyDataSetChanged();
	}
	
	@NonNull
	@Override
	public EncounterMonsterListAdapter.MonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_encounter_monsters, parent, false);
		return new EncounterMonsterListAdapter.MonsterViewHolder(itemView, mListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull EncounterMonsterListAdapter.MonsterViewHolder holder, int position)
	{
		if (monsterList == null)
		{
			return;
		}
		final Monster monster = monsterList.get(position);
		if (monster != null)
		{
			holder.encounterMonsterTv.setText(monster.getName());
			holder.encounterMonsterCrTv.setText(monster.getCr());
			holder.removeMonsterBtn.setOnClickListener(holder);
			holder.dupeMonsterBtn.setOnClickListener(holder);
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
	
	static class MonsterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView encounterMonsterTv, encounterMonsterCrTv;
		private Button removeMonsterBtn, dupeMonsterBtn;
		private RvClickListener mListener;
		
		public MonsterViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			encounterMonsterTv = itemView.findViewById(R.id.encounterMonsterTv);
			encounterMonsterCrTv = itemView.findViewById(R.id.encounterMonsterCrTv);
			mListener = listener;
			
			removeMonsterBtn = itemView.findViewById(R.id.removeMonsterBtn);
			dupeMonsterBtn = itemView.findViewById(R.id.dupeMonsterBtn);
		}
		
		@Override
		public void onClick(View v)
		{
			
			mListener.onClick(v, getAdapterPosition());
		}
	}
	
	
}
