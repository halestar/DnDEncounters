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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class EncounterManagerListAdapter extends RecyclerView.Adapter<EncounterManagerListAdapter.EncounterViewHolder>
{
	private List<Encounter> encounterList = new ArrayList<>();
	private LayoutInflater layoutInflater;
	private RvClickListener editListener, removeListener;

	public EncounterManagerListAdapter(Context context, RvClickListener editListener, RvClickListener removeListener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.editListener = editListener;
		this.removeListener = removeListener;
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
	public EncounterManagerListAdapter.EncounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_encounter_management, parent, false);
		return new EncounterManagerListAdapter.EncounterViewHolder(itemView, editListener, removeListener);
	}

	@Override
	public void onBindViewHolder(@NonNull EncounterManagerListAdapter.EncounterViewHolder holder, int position)
	{
		final Encounter encounter = encounterList.get(position);
		if (encounter != null)
		{
			holder.EncounterNameTv.setText(encounter.getEncounterName());
			holder.EncounterCrTv.setText(String.format(Locale.getDefault(), "%d", encounter
					.getCr()));
		}
	}
	
	@Override
	public int getItemCount()
	{
		return encounterList.size();
	}
	
	static class EncounterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView EncounterNameTv, EncounterCrTv;
		private Button EditEncounterBt, RemoveEncounterBt;
		private RvClickListener editListener, removeListener;
		
		EncounterViewHolder(View itemView, RvClickListener editListener, RvClickListener removeListener)
		{
			super(itemView);
			EncounterNameTv = itemView.findViewById(R.id.EncounterNameTv);
			EncounterCrTv = itemView.findViewById(R.id.EncounterCrTv);
			this.editListener = editListener;
			this.removeListener = removeListener;
			EditEncounterBt = itemView.findViewById(R.id.EditEncounterBt);
			EditEncounterBt.setOnClickListener(this);
			RemoveEncounterBt = itemView.findViewById(R.id.RemoveEncounterBt);
			RemoveEncounterBt.setOnClickListener(this);
		}
		
		@Override
		public void onClick(View v)
		{
			if(v == EditEncounterBt)
				editListener.onClick(v, getAdapterPosition());
			else if(v == RemoveEncounterBt)
				removeListener.onClick(v, getAdapterPosition());
		}
	}
}
