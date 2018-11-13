package net.kalinec.dndencounters.characters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kalinec.dndencounters.EditCharacter;
import net.kalinec.dndencounters.R;

import java.util.List;

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>
{
	private LayoutInflater layoutInflater;
	private List<Character> characterList;
	private Context context;
	private Intent destinationIntent;
	
	public Intent getDestinationIntent()
	{
		return destinationIntent;
	}
	
	public void setDestinationIntent(Intent destinationIntent)
	{
		this.destinationIntent = destinationIntent;
	}
	
	public CharacterListAdapter(Context context)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.destinationIntent = new Intent(context, EditCharacter.class);
	}
	
	public void setCharacterList(List<Character> characterList)
	{
		this.characterList = characterList;
		notifyDataSetChanged();
	}
	
	@NonNull
	@Override
	public CharacterListAdapter.CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_player_character, parent, false);
		return new CharacterListAdapter.CharacterViewHolder(itemView);
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
			holder.itemView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Bundle bundle = new Bundle();
					bundle.putSerializable(Character.PASSED_CHARACTER, character);
					destinationIntent.putExtras(bundle);
					context.startActivity(destinationIntent);
				}
			});
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
	
	static class CharacterViewHolder extends RecyclerView.ViewHolder
	{
		private TextView characterNameTv, characterLvTv;
		
		public CharacterViewHolder(View itemView)
		{
			super(itemView);
			characterNameTv = itemView.findViewById(R.id.characterNameTv);
			characterLvTv = itemView.findViewById(R.id.characterLevelTv);
		}
	}
}
