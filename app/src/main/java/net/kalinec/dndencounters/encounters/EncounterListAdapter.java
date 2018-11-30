package net.kalinec.dndencounters.encounters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class EncounterListAdapter extends RecyclerView.Adapter<EncounterListAdapter.EncounterViewHolder>
{
	private static final Comparator<Encounter> ALPHABETICAL_COMPARATOR = new Comparator<Encounter>() {
		@Override
		public int compare(Encounter a, Encounter b) {
			return a.getEncounterName().compareTo(b.getEncounterName());
		}
	};
	
	private final SortedList<Encounter> encounterList = new SortedList<>(Encounter.class, new SortedList.Callback<Encounter>() {
		@Override
		public int compare(Encounter a, Encounter b) {
			return ALPHABETICAL_COMPARATOR.compare(a, b);
		}
		
		@Override
		public void onInserted(int position, int count) {
			notifyItemRangeInserted(position, count);
		}
		
		@Override
		public void onRemoved(int position, int count) {
			notifyItemRangeRemoved(position, count);
		}
		
		@Override
		public void onMoved(int fromPosition, int toPosition) {
			notifyItemMoved(fromPosition, toPosition);
		}
		
		@Override
		public void onChanged(int position, int count) {
			notifyItemRangeChanged(position, count);
		}
		
		@Override
		public boolean areContentsTheSame(Encounter oldItem, Encounter newItem) {
			return oldItem.equals(newItem);
		}
		
		@Override
		public boolean areItemsTheSame(Encounter item1, Encounter item2) {
			return item1.equals(item2);
		}
	});
	
	private LayoutInflater layoutInflater;
	private RvClickListener mListener;
	
	public EncounterListAdapter(Context context, RvClickListener listener)
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
	public EncounterListAdapter.EncounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_encounter, parent, false);
		return new EncounterListAdapter.EncounterViewHolder(itemView, mListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull EncounterListAdapter.EncounterViewHolder holder, int position)
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
		private RvClickListener mListener;
		
		EncounterViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			EncounterNameTv = itemView.findViewById(R.id.EncounterNameTv);
			EncounterCrTv = itemView.findViewById(R.id.EncounterCrTv);
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
