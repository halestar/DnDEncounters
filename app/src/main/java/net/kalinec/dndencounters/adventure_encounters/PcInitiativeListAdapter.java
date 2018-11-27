package net.kalinec.dndencounters.adventure_encounters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvItemClickListener;
import net.kalinec.dndencounters.monster_tokens.MonsterTokenSpinnerAdapter;
import net.kalinec.dndencounters.monsters.Monster;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PcInitiativeListAdapter extends RecyclerView.Adapter<PcInitiativeListAdapter.PcInitiativeViewHolder>
{
	private static final Comparator<Monster> ALPHABETICAL_COMPARATOR = new Comparator<Monster>() {
		@Override
		public int compare(Monster a, Monster b) {
			return a.getName().compareTo(b.getName());
		}
	};

	private final List<AdventureEncounterPlayer> playerList = new ArrayList<>();

	private LayoutInflater layoutInflater;
	private Context context;

	public PcInitiativeListAdapter(Context context)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
	}

	public void setPlayerList(List<AdventureEncounterPlayer> playerList)
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
		if (playerList == null)
		{
			return;
		}
		final AdventureEncounterPlayer pc = playerList.get(position);
		if (pc != null)
		{
			holder.PcNameTv.setText(pc.getPc().getName());
			holder.PlayerInitiativeEt.setText(Integer.toString(pc.getInitiative()));
		}
	}

	@Override
	public int getItemCount()
	{
		if (playerList == null)
		{
			return 0;
		}
		else
		{
			return playerList.size();
		}
	}

	public static class PcInitiativeViewHolder extends RecyclerView.ViewHolder
	{
		private TextView PcNameTv;
		private EditText PlayerInitiativeEt;

		public PcInitiativeViewHolder(View itemView)
		{
			super(itemView);
			PcNameTv = itemView.findViewById(R.id.PcNameTv);
			PlayerInitiativeEt = itemView.findViewById(R.id.PlayerInitiativeEt);
		}

		public int getInitiative()
		{
			return Integer.parseInt(PlayerInitiativeEt.getText().toString());
		}
	}
}
