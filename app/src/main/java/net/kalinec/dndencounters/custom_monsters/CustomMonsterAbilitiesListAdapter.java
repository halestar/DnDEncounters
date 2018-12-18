package net.kalinec.dndencounters.custom_monsters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.MonsterAbility;

import java.util.ArrayList;
import java.util.List;

public class CustomMonsterAbilitiesListAdapter extends RecyclerView.Adapter<CustomMonsterAbilitiesListAdapter.CustomMonsterAbilityAbilitiesViewHolder>
{

	private final ArrayList<MonsterAbility> monsterAbilityList = new ArrayList<>();

	private LayoutInflater layoutInflater;
	private RvClickListener editListener, deleteListener;

	public CustomMonsterAbilitiesListAdapter(Context context, RvClickListener editListener, RvClickListener deleteListener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.editListener = editListener;
		this.deleteListener = deleteListener;
	}

	public void setMonsterAbilityList(List<MonsterAbility> monsterAbilityList)
	{
		this.monsterAbilityList.clear();
		this.monsterAbilityList.addAll(monsterAbilityList);
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public CustomMonsterAbilitiesListAdapter.CustomMonsterAbilityAbilitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_custom_monster_abilities, parent, false);
		return new CustomMonsterAbilitiesListAdapter.CustomMonsterAbilityAbilitiesViewHolder(itemView, editListener, deleteListener);
	}

	@Override
	public void onBindViewHolder(@NonNull CustomMonsterAbilitiesListAdapter.CustomMonsterAbilityAbilitiesViewHolder holder, int position)
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
	
	static class CustomMonsterAbilityAbilitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView MonsterInfoAbilityNameTv, MonsterInfoAbilityDescriptionTv;
		private Button EditAbilityBt, DeleteAbilityBt;
		private RvClickListener editListener, deleteListener;
		
		CustomMonsterAbilityAbilitiesViewHolder(View itemView, RvClickListener editListener, RvClickListener deleteListener)
		{
			super(itemView);
			MonsterInfoAbilityNameTv = itemView.findViewById(R.id.MonsterInfoAbilityNameTv);
			MonsterInfoAbilityDescriptionTv = itemView.findViewById(R.id.MonsterInfoAbilityDescriptionTv);
			this.editListener = editListener;
			this.deleteListener = deleteListener;
			EditAbilityBt = itemView.findViewById(R.id.EditAbilityBt);
			EditAbilityBt.setOnClickListener(this);
			DeleteAbilityBt = itemView.findViewById(R.id.DeleteAbilityBt);
			DeleteAbilityBt.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if(v == EditAbilityBt)
				editListener.onClick(v, getAdapterPosition());
			else
				deleteListener.onClick(v, getAdapterPosition());
		}
	}
}
