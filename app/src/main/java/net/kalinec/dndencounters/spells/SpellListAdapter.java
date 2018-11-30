package net.kalinec.dndencounters.spells;

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

public class SpellListAdapter extends RecyclerView.Adapter<SpellListAdapter.MonsterViewHolder>
{
	private static final Comparator<Spell> ALPHABETICAL_COMPARATOR = new Comparator<Spell>() {
		@Override
		public int compare(Spell a, Spell b) {
			return a.getName().compareTo(b.getName());
		}
	};

	private final SortedList<Spell> spellList = new SortedList<>(Spell.class, new SortedList.Callback<Spell>() {
		@Override
		public int compare(Spell a, Spell b) {
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
		public boolean areContentsTheSame(Spell oldItem, Spell newItem) {
			return oldItem.equals(newItem);
		}

		@Override
		public boolean areItemsTheSame(Spell item1, Spell item2) {
			return item1.equals(item2);
		}
	});

	private LayoutInflater layoutInflater;
	private RvClickListener mListener;

	public SpellListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.mListener = listener;
	}

	public void setSpellList(List<Spell> spellList)
	{
		this.spellList.clear();
		this.spellList.addAll(spellList);
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public SpellListAdapter.MonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_spells, parent, false);
		return new SpellListAdapter.MonsterViewHolder(itemView, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull SpellListAdapter.MonsterViewHolder holder, int position)
	{
		final Spell spell = spellList.get(position);
		if (spell != null)
		{
			holder.SpellNameTv.setText(spell.getName());
			holder.SpellLevelTv.setText(spell.getLevel());
		}
	}
	
	@Override
	public int getItemCount()
	{
		return spellList.size();
	}
	
	static class MonsterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView SpellNameTv, SpellLevelTv;
		private RvClickListener mListener;
		
		MonsterViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			SpellNameTv = itemView.findViewById(R.id.SpellNameTv);
			SpellLevelTv = itemView.findViewById(R.id.SpellLevelTv);
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
