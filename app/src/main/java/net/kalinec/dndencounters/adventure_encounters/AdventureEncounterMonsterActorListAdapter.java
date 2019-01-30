package net.kalinec.dndencounters.adventure_encounters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.adventure_encounters.EncounterUpdater;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokenSpinnerAdapter;
import net.kalinec.dndencounters.monster_tokens.MonsterTokens;

import java.util.ArrayList;
import java.util.List;

public class AdventureEncounterMonsterActorListAdapter extends RecyclerView.Adapter<AdventureEncounterMonsterActorListAdapter.AdventureEncounterMonsterActorViewHolder>
{

	private LayoutInflater layoutInflater;
	private Context context;
	public static ArrayList<EncounterUpdater> encounterUpdateList;
	private static List<MonsterToken> monsterTokens;

	public AdventureEncounterMonsterActorListAdapter(Context context, ArrayList<EncounterUpdater> encounterUpdateList)
	{
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.encounterUpdateList = encounterUpdateList;
		this.monsterTokens = MonsterTokens.getAllMonsterTokens(context);
	}

	public void setMonsterList(ArrayList<EncounterUpdater> encounterUpdateList)
	{
		this.encounterUpdateList.clear();
		this.encounterUpdateList.addAll(encounterUpdateList);
		notifyDataSetChanged();
	}

	public EncounterUpdater get(int i)
	{
		return encounterUpdateList.get(i);
	}

	@NonNull
	@Override
	public AdventureEncounterMonsterActorListAdapter.AdventureEncounterMonsterActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_encounter_monster_update, parent, false);
		return new AdventureEncounterMonsterActorListAdapter.AdventureEncounterMonsterActorViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull AdventureEncounterMonsterActorListAdapter.AdventureEncounterMonsterActorViewHolder holder, int position)
	{
		final EncounterUpdater updater = encounterUpdateList.get(position);
		if (updater != null)
		{
			holder.MonsterNameTv.setText(updater.getMonster().getName());
			holder.MonsterInitiativeEt.setText(Integer.toString(updater.getInitiative()));
			holder.MonsterHpEt.setText(Integer.toString(updater.getCurrentHp()));
			holder.MonsterDeathTb.setChecked(updater.isAlive());
			MonsterTokenSpinnerAdapter monsterTokenAdapter = new MonsterTokenSpinnerAdapter(this.context);
			holder.MonsterTokenSp.setAdapter(monsterTokenAdapter);
			holder.MonsterTokenSp.setSelection(monsterTokenAdapter.findPosition(updater.getAssignedToken()));
		}
	}
	
	@Override
	public int getItemCount()
	{
		return encounterUpdateList.size();
	}
	
	public static class AdventureEncounterMonsterActorViewHolder extends RecyclerView.ViewHolder implements
	                                                                                      View.OnClickListener
	{
		private TextView MonsterNameTv;
		private EditText MonsterInitiativeEt, MonsterHpEt;
		private Spinner MonsterTokenSp;
		private ToggleButton MonsterDeathTb;
		private Button RemoveMonsterBt;

		AdventureEncounterMonsterActorViewHolder(View itemView)
		{
			super(itemView);
			MonsterNameTv = itemView.findViewById(R.id.MonsterNameTv);
			MonsterInitiativeEt = itemView.findViewById(R.id.MonsterInitiativeEt);
			MonsterInitiativeEt.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					encounterUpdateList.get(getAdapterPosition()).setInitiative(Integer.parseInt(MonsterInitiativeEt.getText().toString()));
				}

				@Override
				public void afterTextChanged(Editable s)
				{

				}
			});
			MonsterHpEt = itemView.findViewById(R.id.MonsterHpEt);
			MonsterHpEt.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					encounterUpdateList.get(getAdapterPosition()).setCurrentHp(Integer.parseInt(MonsterHpEt.getText().toString()));
				}

				@Override
				public void afterTextChanged(Editable s)
				{

				}
			});
			MonsterTokenSp = itemView.findViewById(R.id.MonsterTokenSp);
			MonsterTokenSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
			{
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
				{

					encounterUpdateList.get(getAdapterPosition()).setAssignedToken(monsterTokens.get(position));
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});
			MonsterDeathTb = itemView.findViewById(R.id.MonsterDeathTb);
			MonsterDeathTb.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					encounterUpdateList.get(getAdapterPosition()).setAlive(MonsterDeathTb.isChecked());
				}
			});
			RemoveMonsterBt = itemView.findViewById(R.id.RemoveMonsterBt);
			RemoveMonsterBt.setOnClickListener(this);
			
		}
		
		@Override
		public void onClick(View v)
		{
			encounterUpdateList.remove(getAdapterPosition());
		}
	}
}
