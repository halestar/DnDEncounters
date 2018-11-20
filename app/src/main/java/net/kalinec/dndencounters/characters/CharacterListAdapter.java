package net.kalinec.dndencounters.characters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>
{
	private static final Comparator<Character> ALPHABETICAL_COMPARATOR = new Comparator<Character>() {
		@Override
		public int compare(Character a, Character b) {
			return a.getName().compareTo(b.getName());
		}
	};

	private LayoutInflater layoutInflater;
	private List<Character> characterList;
	private Context context;
	private RvClickListener mListener;
	
	public CharacterListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.mListener = listener;
		this.characterList = new ArrayList<>();
	}
	
	public void setCharacterList(List<Character> characterList)
	{
		this.characterList.clear();
		this.characterList = characterList;
		notifyDataSetChanged();
	}

	public Character get(int position)
	{
		return characterList.get(position);
	}
	
	@NonNull
	@Override
	public CharacterListAdapter.CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_player_character, parent, false);
		return new CharacterListAdapter.CharacterViewHolder(itemView, mListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull CharacterListAdapter.CharacterViewHolder holder, int position)
	{
		if (characterList == null)
		{
			return;
		}
		final Character character = characterList.get(position);
		if (character != null)
		{
			holder.characterNameTv.setText(character.nameAndDescription());
			holder.characterLvTv.setText("Lv. " + Integer.toString(character.getLevel()));
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
	
	static class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView characterNameTv, characterLvTv;
		private RvClickListener mListener;
		
		public CharacterViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			mListener = listener;
			characterNameTv = itemView.findViewById(R.id.characterNameTv);
			characterLvTv = itemView.findViewById(R.id.characterLevelTv);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mListener.onClick(v, getAdapterPosition());
		}
	}
}
