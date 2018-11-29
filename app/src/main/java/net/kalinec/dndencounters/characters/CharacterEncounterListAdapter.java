package net.kalinec.dndencounters.characters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CharacterEncounterListAdapter extends RecyclerView.Adapter<CharacterEncounterListAdapter.CharacterEncounterViewHolder>
{

	private LayoutInflater layoutInflater;
	private List<AdventureEncounterPlayer> characterList;
	private Context context;

	public CharacterEncounterListAdapter(Context context)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.characterList = new ArrayList<>();
	}
	
	public void setCharacterList(List<AdventureEncounterPlayer> characterList)
	{
		this.characterList.clear();
		this.characterList = characterList;
		notifyDataSetChanged();
	}

	public AdventureEncounterPlayer get(int position)
	{
		return characterList.get(position);
	}
	
	@NonNull
	@Override
	public CharacterEncounterListAdapter.CharacterEncounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_character_encounter_info, parent, false);
		return new CharacterEncounterListAdapter.CharacterEncounterViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(@NonNull CharacterEncounterListAdapter.CharacterEncounterViewHolder holder, int position)
	{
		if (characterList == null)
		{
			return;
		}
		final AdventureEncounterPlayer character = characterList.get(position);
		if (character != null)
		{
			holder.PcNameTv.setText(character.getPc().getName());
			holder.PcAcTv.setText(Integer.toString(character.getPc().getAc()));
			holder.PcPpTv.setText(Integer.toString(character.getPc().getPp()));
			holder.PcDcTv.setText(Integer.toString(character.getPc().getSpellDc()));
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
	
	static class CharacterEncounterViewHolder extends RecyclerView.ViewHolder
	{
		private TextView PcNameTv, PcAcTv, PcPpTv, PcDcTv;

		public CharacterEncounterViewHolder(View itemView)
		{
			super(itemView);
			PcNameTv = itemView.findViewById(R.id.PcNameTv);
			PcAcTv = itemView.findViewById(R.id.PcAcTv);
			PcPpTv = itemView.findViewById(R.id.PcPpTv);
			PcDcTv = itemView.findViewById(R.id.PcDcTv);
		}
	}
}
