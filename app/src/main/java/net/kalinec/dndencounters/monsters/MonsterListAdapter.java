package net.kalinec.dndencounters.monsters;

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

public class MonsterListAdapter extends RecyclerView.Adapter<MonsterListAdapter.MonsterViewHolder>
{
	private static final Comparator<Monster> ALPHABETICAL_COMPARATOR = new Comparator<Monster>() {
		@Override
		public int compare(Monster a, Monster b) {
			return a.getName().compareTo(b.getName());
		}
	};
	
	private final SortedList<Monster> monsterList = new SortedList<>(Monster.class, new SortedList.Callback<Monster>() {
		@Override
		public int compare(Monster a, Monster b) {
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
		public boolean areContentsTheSame(Monster oldItem, Monster newItem) {
			return oldItem.equals(newItem);
		}
		
		@Override
		public boolean areItemsTheSame(Monster item1, Monster item2) {
			return item1.equals(item2);
		}
	});
	
	private LayoutInflater layoutInflater;
	private RvClickListener mListener;
	
	public MonsterListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.mListener = listener;
	}
	
	public void setMonsterList(List<Monster> monsterList)
	{
		this.monsterList.clear();
		this.monsterList.addAll(monsterList);
		notifyDataSetChanged();
	}
	
	@NonNull
	@Override
	public MonsterListAdapter.MonsterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_monster, parent, false);
		return new MonsterListAdapter.MonsterViewHolder(itemView, mListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull MonsterListAdapter.MonsterViewHolder holder, int position)
	{
		final Monster monster = monsterList.get(position);
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
	
	static class MonsterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView MonsterNameTv, MonsterCrTv;
		private RvClickListener mListener;
		
		MonsterViewHolder(View itemView, RvClickListener listener)
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
