package net.kalinec.dndencounters.playsessions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimpleEncounterListAdapter extends RecyclerView.Adapter<SimpleEncounterListAdapter.SimpleEncounterViewHolder>
{
	private static final Comparator<AdventureEncounter> ALPHABETICAL_COMPARATOR = new Comparator<AdventureEncounter>() {
		@Override
		public int compare(AdventureEncounter a, AdventureEncounter b) {
			return a.getEncounter().getEncounterName().compareTo(b.getEncounter().getEncounterName());
		}
	};

	private final ArrayList<AdventureEncounter> encounterList = new ArrayList<>();

	private LayoutInflater layoutInflater;
	private Context context;
	private RvClickListener mListener;

	public SimpleEncounterListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		mListener = listener;
	}

	public void setEncounterList(ArrayList<AdventureEncounter> newList)
	{
		encounterList.clear();
		encounterList.addAll(newList);
		notifyDataSetChanged();
	}


	public AdventureEncounter get(int position)
	{
		return encounterList.get(position);
	}

	@NonNull
	@Override
	public SimpleEncounterListAdapter.SimpleEncounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_encounters_played, parent, false);
		return new SimpleEncounterListAdapter.SimpleEncounterViewHolder(itemView, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull SimpleEncounterListAdapter.SimpleEncounterViewHolder holder, int position)
	{
		if (encounterList == null)
		{
			return;
		}
		final AdventureEncounter encounter = encounterList.get(position);
		if (encounter != null)
		{
			holder.encounterNameTxt.setText(encounter.getEncounter().getSelectableText());
		}
	}
	
	@Override
	public int getItemCount()
	{
		if (encounterList == null)
		{
			return 0;
		}
		else
		{
			return encounterList.size();
		}
	}
	
	static class SimpleEncounterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView encounterNameTxt;
		private RvClickListener mListener;
		
		public SimpleEncounterViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			encounterNameTxt = itemView.findViewById(R.id.encounterNameTxt);

			mListener = listener;
			itemView.setOnClickListener(this);
		}
		
		@Override
		public void onClick(View v)
		{
			mListener.onClick(v, getAdapterPosition());
		}
	}
}
