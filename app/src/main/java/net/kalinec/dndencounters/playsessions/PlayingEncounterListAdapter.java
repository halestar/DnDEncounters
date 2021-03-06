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
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.ArrayList;
import java.util.List;

public class PlayingEncounterListAdapter extends RecyclerView.Adapter<PlayingEncounterListAdapter.PlayingEncounterViewHolder>
{

	private final List<Encounter> encounterList = new ArrayList<>();

	private LayoutInflater layoutInflater;
	private RvClickListener mListener;

	public PlayingEncounterListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		mListener = listener;
	}

	public void setEncounterList(List<Encounter> newList)
	{
		encounterList.clear();
		encounterList.addAll(newList);
		notifyDataSetChanged();
	}


	public Encounter get(int position)
	{
		return encounterList.get(position);
	}

	@NonNull
	@Override
	public PlayingEncounterListAdapter.PlayingEncounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_encounters_to_play, parent, false);
		return new PlayingEncounterListAdapter.PlayingEncounterViewHolder(itemView, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull PlayingEncounterListAdapter.PlayingEncounterViewHolder holder, int position)
	{
		final Encounter encounter = encounterList.get(position);
		if (encounter != null)
		{
			holder.encounterNameTxt.setText(encounter.getSelectableText());
		}
	}
	
	@Override
	public int getItemCount()
	{
		return encounterList.size();
	}
	
	static class PlayingEncounterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView encounterNameTxt;
		private Button playEncounterBtn, removeEncounterBtn;
		private RvClickListener mListener;
		
		PlayingEncounterViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			encounterNameTxt = itemView.findViewById(R.id.encounterNameTxt);
			playEncounterBtn = itemView.findViewById(R.id.playEncounterBtn);
			playEncounterBtn.setOnClickListener(this);
			removeEncounterBtn = itemView.findViewById(R.id.removeEncounterBtn);
			removeEncounterBtn.setOnClickListener(this);

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
