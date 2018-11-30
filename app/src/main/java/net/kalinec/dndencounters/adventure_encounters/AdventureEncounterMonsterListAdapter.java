package net.kalinec.dndencounters.adventure_encounters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdventureEncounterMonsterListAdapter extends RecyclerView.Adapter<AdventureEncounterMonsterListAdapter.AdventureEncounterMonsterViewHolder>
{

	private final ArrayList<AdventureEncounterMonster> monsterList = new ArrayList<>();

	private LayoutInflater layoutInflater;
	private RvClickListener mListener;

	public AdventureEncounterMonsterListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.mListener = listener;
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
	public AdventureEncounterMonsterListAdapter.AdventureEncounterMonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_adventure_encounter_monsters, parent, false);
		return new AdventureEncounterMonsterListAdapter.AdventureEncounterMonsterViewHolder(itemView, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull AdventureEncounterMonsterListAdapter.AdventureEncounterMonsterViewHolder holder, int position)
	{
		final AdventureEncounterMonster mToken = monsterList.get(position);
		if (mToken != null)
		{
			holder.MonsterNameTv.setText(mToken.getMonster().getName());
			holder.MonsterAcTv.setText(String.format(Locale.getDefault(), "%d", mToken.getMonster()
					.getAc()));
			holder.MonsterCurrentHpTxt.setText(String.format(Locale.getDefault(), "%d", mToken
					.getHp()));
			holder.MonsterMaxHpTxt.setText(String.format(Locale.getDefault(), "%d", mToken
					.getMaxHp()));
			mToken.getToken().makePortrait(holder.MonsterTokenPortraitIv);
		}
	}
	
	@Override
	public int getItemCount()
	{
		return monsterList.size();
	}
	
	static class AdventureEncounterMonsterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView MonsterNameTv, MonsterAcTv, MonsterCurrentHpTxt, MonsterMaxHpTxt;
		private ImageView MonsterTokenPortraitIv;
		private RvClickListener mListener;
		
		AdventureEncounterMonsterViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			MonsterNameTv = itemView.findViewById(R.id.MonsterNameTv);
			MonsterAcTv = itemView.findViewById(R.id.MonsterAcTv);
			MonsterCurrentHpTxt = itemView.findViewById(R.id.MonsterCurrentHpTxt);
			MonsterTokenPortraitIv = itemView.findViewById(R.id.MonsterTokenPortraitIv);
			MonsterMaxHpTxt = itemView.findViewById(R.id.MonsterMaxHpTxt);
			mListener = listener;
			itemView.setOnClickListener(this);
		}
		
		@Override
		public void onClick(View v)
		{
			Log.d("AdventureEncounterMonsterViewHolder", "Clicked iobn holder, position is " + getAdapterPosition());
			mListener.onClick(v, getAdapterPosition());
		}
	}
}
