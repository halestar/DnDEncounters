package net.kalinec.dndencounters.monster_tokens;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.Comparator;
import java.util.List;

public class MonsterTokenListAdapter extends RecyclerView.Adapter<MonsterTokenListAdapter.MonsterTokenViewHolder>
{
	private static final Comparator<MonsterToken> ALPHABETICAL_COMPARATOR = new Comparator<MonsterToken>() {
		@Override
		public int compare(MonsterToken a, MonsterToken b) {
			return a.getTokenName().compareTo(b.getTokenName());
		}
	};

	private final SortedList<MonsterToken> monsterList = new SortedList<>(MonsterToken.class, new SortedList.Callback<MonsterToken>() {
		@Override
		public int compare(MonsterToken a, MonsterToken b) {
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
		public boolean areContentsTheSame(MonsterToken oldItem, MonsterToken newItem) {
			return oldItem.equals(newItem);
		}

		@Override
		public boolean areItemsTheSame(MonsterToken item1, MonsterToken item2) {
			return item1.equals(item2);
		}
	});

	private LayoutInflater layoutInflater;
	private RvClickListener mListener;

	public MonsterTokenListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.mListener = listener;
	}

	public void setMonsterList(List<MonsterToken> monsterList)
	{
		this.monsterList.clear();
		this.monsterList.addAll(monsterList);
		notifyDataSetChanged();
	}

	public MonsterToken get(int i)
	{
		return monsterList.get(i);
	}

	@NonNull
	@Override
	public MonsterTokenListAdapter.MonsterTokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_monster_tokens, parent, false);
		return new MonsterTokenListAdapter.MonsterTokenViewHolder(itemView, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull MonsterTokenListAdapter.MonsterTokenViewHolder holder, int position)
	{
		final MonsterToken mToken = monsterList.get(position);
		if (mToken != null)
		{
			holder.MonsterTokenName.setText(mToken.getTokenName());
			mToken.makePortrait(holder.MonsterTokenPortrait);
		}
	}
	
	@Override
	public int getItemCount()
	{
		return monsterList.size();
	}
	
	static class MonsterTokenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView MonsterTokenName;
		private ImageView MonsterTokenPortrait;
		private RvClickListener mListener;
		
		MonsterTokenViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			MonsterTokenName = itemView.findViewById(R.id.MonsterTokenName);
			MonsterTokenPortrait = itemView.findViewById(R.id.MonsterTokenPortrait);
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
