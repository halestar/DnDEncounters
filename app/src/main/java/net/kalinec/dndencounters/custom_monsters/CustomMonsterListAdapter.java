package net.kalinec.dndencounters.custom_monsters;

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
import net.kalinec.dndencounters.monsters.Monster;

import java.util.Comparator;
import java.util.List;

public class CustomMonsterListAdapter extends RecyclerView.Adapter<CustomMonsterListAdapter.CustomMonsterViewHolder>
{
	private static final Comparator<CustomMonster> ALPHABETICAL_COMPARATOR = new Comparator<CustomMonster>() {
		@Override
		public int compare(CustomMonster a, CustomMonster b) {
			return a.getUuid().compareTo(b.getUuid());
		}
	};

	private final SortedList<CustomMonster> monsterList = new SortedList<>(CustomMonster.class, new SortedList.Callback<CustomMonster>() {
		@Override
		public int compare(CustomMonster a, CustomMonster b) {
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
		public boolean areContentsTheSame(CustomMonster oldItem, CustomMonster newItem) {
			return oldItem.equals(newItem);
		}

		@Override
		public boolean areItemsTheSame(CustomMonster item1, CustomMonster item2) {
			return item1.equals(item2);
		}
	});

	private LayoutInflater layoutInflater;
	private RvClickListener mListener;

	public CustomMonsterListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.mListener = listener;
	}

	public void setMonsterList(List<CustomMonster> monsterList)
	{
		this.monsterList.clear();
		this.monsterList.addAll(monsterList);
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public CustomMonsterListAdapter.CustomMonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_monster, parent, false);
		return new CustomMonsterListAdapter.CustomMonsterViewHolder(itemView, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull CustomMonsterListAdapter.CustomMonsterViewHolder holder, int position)
	{
		final CustomMonster monster = monsterList.get(position);
		if (monster != null)
		{
			holder.MonsterNameTv.setText(monster.getName());
			holder.MonsterCrTv.setText(monster.getCr());
		}
	}
	
	@Override
	public int getItemCount()
	{
		return monsterList.size();
	}
	
	static class CustomMonsterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView MonsterNameTv, MonsterCrTv;
		private RvClickListener mListener;

		CustomMonsterViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			MonsterNameTv = itemView.findViewById(R.id.MonsterNameTv);
			MonsterCrTv = itemView.findViewById(R.id.MonsterCrTv);
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
