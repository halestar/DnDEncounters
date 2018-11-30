package net.kalinec.dndencounters.monsters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kalinec.dndencounters.R;

import java.util.ArrayList;
import java.util.List;

public class MonsterAbilitiesListAdapter extends RecyclerView.Adapter<MonsterAbilitiesListAdapter.MonsterAbilityAbilitiesViewHolder>
{
	
	private final ArrayList<MonsterAbility> monsterAbilityList = new ArrayList<>();
	
	private LayoutInflater layoutInflater;
	
	public MonsterAbilitiesListAdapter(Context context)
	{
		this.layoutInflater = LayoutInflater.from(context);
	}
	
	public void setMonsterAbilityList(List<MonsterAbility> monsterAbilityList)
	{
		this.monsterAbilityList.clear();
		this.monsterAbilityList.addAll(monsterAbilityList);
		notifyDataSetChanged();
	}
	
	@NonNull
	@Override
	public MonsterAbilitiesListAdapter.MonsterAbilityAbilitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_monster_abilities, parent, false);
		return new MonsterAbilitiesListAdapter.MonsterAbilityAbilitiesViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(@NonNull MonsterAbilitiesListAdapter.MonsterAbilityAbilitiesViewHolder holder, int position)
	{
		final MonsterAbility monsterAbility = monsterAbilityList.get(position);
		if (monsterAbility != null)
		{
			holder.MonsterInfoAbilityNameTv.setText(monsterAbility.getName());
			holder.MonsterInfoAbilityDescriptionTv.setText(monsterAbility.getDescription());
		}
	}
	
	@Override
	public int getItemCount()
	{
		return monsterAbilityList.size();
	}
	
	static class MonsterAbilityAbilitiesViewHolder extends RecyclerView.ViewHolder
	{
		private TextView MonsterInfoAbilityNameTv, MonsterInfoAbilityDescriptionTv;
		
		MonsterAbilityAbilitiesViewHolder(View itemView)
		{
			super(itemView);
			MonsterInfoAbilityNameTv = itemView.findViewById(R.id.MonsterInfoAbilityNameTv);
			MonsterInfoAbilityDescriptionTv = itemView.findViewById(R.id.MonsterInfoAbilityDescriptionTv);
		}
	}
}
