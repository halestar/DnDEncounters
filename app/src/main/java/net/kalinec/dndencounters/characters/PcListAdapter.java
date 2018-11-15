package net.kalinec.dndencounters.characters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kalinec.dndencounters.EditCharacter;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.PlayerDao;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.players.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PcListAdapter extends RecyclerView.Adapter<PcListAdapter.PcViewHolder>
{


	private static final Comparator<Character> ALPHABETICAL_COMPARATOR = new Comparator<Character>() {
		@Override
		public int compare(Character a, Character b) {
			return a.getName().compareTo(b.getName());
		}
	};

	private final SortedList<Character> characterList = new SortedList<>(Character.class, new SortedList.Callback<Character>() {
		@Override
		public int compare(Character a, Character b) {
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
		public boolean areContentsTheSame(Character oldItem, Character newItem) {
			return oldItem.equals(newItem);
		}

		@Override
		public boolean areItemsTheSame(Character item1, Character item2) {
			return item1.equals(item2);
		}
	});

	private LayoutInflater layoutInflater;
	private Context context;
	private RvClickListener mListener;



	public PcListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		mListener = listener;
	}

	public void setCharacterList(List<Character> characterList)
	{
		this.characterList.clear();
		this.characterList.addAll(characterList);
		notifyDataSetChanged();
	}

	public Character get(int position)
	{
		return characterList.get(position);
	}

	@NonNull
	@Override
	public PcListAdapter.PcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_pc, parent, false);
		return new PcListAdapter.PcViewHolder(itemView, mListener);
	}

	public ArrayList<Character> getCharacterList() {
		ArrayList<Character> pcs = new ArrayList<>();
		for(int i = 0; i < characterList.size(); i++)
		{
			pcs.add(characterList.get(i));
		}
		return pcs;
	}

	@Override
	public void onBindViewHolder(@NonNull PcListAdapter.PcViewHolder holder, int position)
	{
		if (characterList == null)
		{
			return;
		}
		final Character character = characterList.get(position);
		if (character != null)
		{
			holder.characterNameTv.setText(character.getName());
			PlayerDao playerDao = AppDatabase.getDatabase(context).playerDao();
			Player owner = playerDao.getPlayerById(character.getPlayerId());
			holder.playeNameTv.setText(owner.getName());
			holder.characterLevelTv.setText("Lv. " + Integer.toString(character.getLevel()));
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
	
	static class PcViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView characterNameTv, playeNameTv, characterLevelTv;
		private RvClickListener mListener;
		
		public PcViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			characterNameTv = itemView.findViewById(R.id.characterNameTv);
			playeNameTv = itemView.findViewById(R.id.playeNameTv);
			characterLevelTv = itemView.findViewById(R.id.characterLevelTv);
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
