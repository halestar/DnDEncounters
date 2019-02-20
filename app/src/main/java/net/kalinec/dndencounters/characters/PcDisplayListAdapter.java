package net.kalinec.dndencounters.characters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PcDisplayListAdapter extends RecyclerView.Adapter<PcDisplayListAdapter.PcDisplayViewHolder>
{

	private LayoutInflater layoutInflater;
	private List<Character> characterList;

	public PcDisplayListAdapter(Context context)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.characterList = new ArrayList<>();
	}
	
	public void setCharacterList(List<Character> characterList)
	{
		this.characterList.clear();
		this.characterList = characterList;
		notifyDataSetChanged();
	}

	public Character get(int position)
	{
		return characterList.get(position);
	}
	
	@NonNull
	@Override
	public PcDisplayListAdapter.PcDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_pc_display, parent, false);
		return new PcDisplayListAdapter.PcDisplayViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(@NonNull PcDisplayListAdapter.PcDisplayViewHolder holder, int position)
	{
		if (characterList == null)
		{
			return;
		}
		final Character character = characterList.get(position);
		if (character != null)
		{
			holder.PcNameTv.setText(character.getName());
			holder.PcRaceTv.setText(character.getCharacterRace());
			holder.PcClassTv.setText(character.getCharacterClass());
			holder.LevelTv.setText(Integer.toString(character.getLevel()));
			holder.AcTv.setText(Integer.toString(character.getAc()));
			holder.HpTv.setText(Integer.toString(character.getHp()));
			holder.PpTv.setText(Integer.toString(character.getPp()));
			holder.SpellDcTv.setText(Integer.toString(character.getSpellDc()));
			if((position % 2) == 0)
				holder.rootLy.setBackgroundResource(R.color.RowStripe);
			else
				holder.rootLy.setBackgroundColor(Color.WHITE);
		}
	}
	
	@Override
	public int getItemCount()
	{
		if (characterList == null)
		{
			return 0;
		}
		else
		{
			return characterList.size();
		}
	}
	
	static class PcDisplayViewHolder extends RecyclerView.ViewHolder
	{
		private TextView PcNameTv, PcRaceTv, PcClassTv, LevelTv, AcTv, HpTv, PpTv, SpellDcTv;
		private ConstraintLayout rootLy;

		PcDisplayViewHolder(View itemView)
		{
			super(itemView);
			rootLy = itemView.findViewById(R.id.rootLy);
			PcNameTv = itemView.findViewById(R.id.PcNameTv);
			PcRaceTv = itemView.findViewById(R.id.PcRaceTv);
			PcClassTv = itemView.findViewById(R.id.PcClassTv);
			LevelTv = itemView.findViewById(R.id.LevelTv);
			AcTv = itemView.findViewById(R.id.AcTv);
			HpTv = itemView.findViewById(R.id.HpTv);
			PpTv = itemView.findViewById(R.id.PpTv);
			SpellDcTv = itemView.findViewById(R.id.SpellDcTv);
		}

	}
}
