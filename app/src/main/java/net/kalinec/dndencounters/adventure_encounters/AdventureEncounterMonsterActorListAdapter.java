package net.kalinec.dndencounters.adventure_encounters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokenSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdventureEncounterMonsterActorListAdapter extends RecyclerView.Adapter<AdventureEncounterMonsterActorListAdapter.AdventureEncounterMonsterActorViewHolder>
{

	private final ArrayList<AdventureEncounterMonster> monsterList = new ArrayList<>();

	private LayoutInflater layoutInflater;
	private Context context;
	private RvClickListener removeMonsterListener;

	public AdventureEncounterMonsterActorListAdapter(Context context, RvClickListener removeMonsterListener)
	{
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.removeMonsterListener = removeMonsterListener;
	}

	public void setMonsterList(List<AdventureEncounterMonster> monsterList)
	{
		this.monsterList.clear();
		this.monsterList.addAll(monsterList);
		notifyDataSetChanged();
	}

	public AdventureEncounterMonster get(int i)
	{
		return monsterList.get(i);
	}

	@NonNull
	@Override
	public AdventureEncounterMonsterActorListAdapter.AdventureEncounterMonsterActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_encounter_monster_update, parent, false);
		return new AdventureEncounterMonsterActorListAdapter.AdventureEncounterMonsterActorViewHolder(itemView, removeMonsterListener);
	}

	@Override
	public void onBindViewHolder(@NonNull AdventureEncounterMonsterActorListAdapter.AdventureEncounterMonsterActorViewHolder holder, int position)
	{
		final AdventureEncounterMonster mToken = monsterList.get(position);
		if (mToken != null)
		{
			holder.MonsterNameTv.setText(mToken.getMonster().getName());
			holder.MonsterInitiativeEt.setText(Integer.toString(mToken.getInitiative()));
			holder.MonsterHpEt.setText(Integer.toString(mToken.getHp()));
			holder.MonsterDeathTb.setChecked(mToken.getStatus() == AdventureEncounterActor.ALIVE);
			MonsterTokenSpinnerAdapter monsterTokenAdapter = new MonsterTokenSpinnerAdapter(this.context);
			holder.MonsterTokenSp.setAdapter(monsterTokenAdapter);
			holder.MonsterTokenSp.setSelection(monsterTokenAdapter.findPosition(mToken.getToken()));
		}
	}
	
	@Override
	public int getItemCount()
	{
		return monsterList.size();
	}
	
	public static class AdventureEncounterMonsterActorViewHolder extends RecyclerView.ViewHolder implements
	                                                                                      View.OnClickListener
	{
		private TextView MonsterNameTv;
		private EditText MonsterInitiativeEt, MonsterHpEt;
		private Spinner MonsterTokenSp;
		private ToggleButton MonsterDeathTb;
		private Button RemoveMonsterBt;
		private RvClickListener removeMonsterListener;
		
		AdventureEncounterMonsterActorViewHolder(View itemView, RvClickListener removeMonsterListener)
		{
			super(itemView);
			MonsterNameTv = itemView.findViewById(R.id.MonsterNameTv);
			MonsterInitiativeEt = itemView.findViewById(R.id.MonsterInitiativeEt);
			MonsterHpEt = itemView.findViewById(R.id.MonsterHpEt);
			MonsterTokenSp = itemView.findViewById(R.id.MonsterTokenSp);
			MonsterDeathTb = itemView.findViewById(R.id.MonsterDeathTb);
			RemoveMonsterBt = itemView.findViewById(R.id.RemoveMonsterBt);
			this.removeMonsterListener = removeMonsterListener;
			RemoveMonsterBt.setOnClickListener(this);
			
		}
		
		public MonsterToken getMonsterToken()
		{
			return (MonsterToken)MonsterTokenSp.getSelectedItem();
		}
		
		public int getInitiative()
		{
			return Integer.parseInt(MonsterInitiativeEt.getText().toString());
		}
		
		public int getCurrentHp()
		{
			return Integer.parseInt(MonsterHpEt.getText().toString());
		}
		
		public boolean isAlive()
		{
			return MonsterDeathTb.isChecked();
		}
		
		@Override
		public void onClick(View v)
		{
			removeMonsterListener.onClick(v, getAdapterPosition());
		}
	}
}
