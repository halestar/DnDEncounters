package net.kalinec.dndencounters.adventure_encounters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.kalinec.dndencounters.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PcInitiativeListAdapter extends RecyclerView.Adapter<PcInitiativeListAdapter.PcInitiativeViewHolder>
{

	public static final ArrayList<AdventureEncounterPlayer> playerList = new ArrayList<>();

	private LayoutInflater layoutInflater;
	
	public PcInitiativeListAdapter(Context context)
	{
		this.layoutInflater = LayoutInflater.from(context);
	}

	public void setPlayerList(ArrayList<AdventureEncounterPlayer> playerList)
	{
		this.playerList.clear();
		this.playerList.addAll(playerList);
		notifyDataSetChanged();
	}

	public AdventureEncounterPlayer get(int position)
	{
		return this.playerList.get(position);
	}

	@NonNull
	@Override
	public PcInitiativeListAdapter.PcInitiativeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_player_initiative, parent, false);
		return new PcInitiativeListAdapter.PcInitiativeViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull PcInitiativeListAdapter.PcInitiativeViewHolder holder, int position)
	{
		final AdventureEncounterPlayer pc = playerList.get(position);
		if (pc != null)
		{
			holder.PcNameTv.setText(pc.getPc().getName());
			holder.PlayerInitiativeEt.setText(String.format(Locale.getDefault(), "%d", pc
					.getInitiative()));
		}
	}

	@Override
	public int getItemCount()
	{
		return playerList.size();
	}

	static class PcInitiativeViewHolder extends RecyclerView.ViewHolder
	{
		private TextView PcNameTv;
		private EditText PlayerInitiativeEt;
		
		PcInitiativeViewHolder(View itemView)
		{
			super(itemView);
			PcNameTv = itemView.findViewById(R.id.PcNameTv);
			PlayerInitiativeEt = itemView.findViewById(R.id.PlayerInitiativeEt);
			PlayerInitiativeEt.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					playerList.get(getAdapterPosition()).setInitiative(Integer.parseInt(PlayerInitiativeEt.getText().toString()));
				}

				@Override
				public void afterTextChanged(Editable s)
				{

				}
			});
		}

		public int getInitiative()
		{
			return Integer.parseInt(PlayerInitiativeEt.getText().toString());
		}
	}
}
