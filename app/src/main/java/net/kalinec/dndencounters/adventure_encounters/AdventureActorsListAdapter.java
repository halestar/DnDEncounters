package net.kalinec.dndencounters.adventure_encounters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.players.Players;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AdventureActorsListAdapter extends RecyclerView.Adapter<AdventureActorsListAdapter.AdventureActorsViewHolder>
{
	protected Context context;
	private static final Comparator<AdventureEncounterActor> INITIATIVE_COMPARATOR = new Comparator<AdventureEncounterActor>() {
		@Override
		public int compare(AdventureEncounterActor a, AdventureEncounterActor b) {
			return (-1) * Integer.compare(a.getInitiative(), b.getInitiative());
		}
	};

	private final List<AdventureEncounterActor> actorList = new ArrayList<>();

	private LayoutInflater layoutInflater;
	
	public AdventureActorsListAdapter(Context context)
	{
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
	}

	public void setActorList(List<AdventureEncounterActor> actorList)
	{
		this.actorList.clear();
		this.actorList.addAll(actorList);
		this.actorList.sort(INITIATIVE_COMPARATOR);
		notifyDataSetChanged();
	}

	public AdventureEncounterActor get(int position)
	{
		return this.actorList.get(position);
	}

	@NonNull
	@Override
	public AdventureActorsListAdapter.AdventureActorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_actor_list, parent, false);
		return new AdventureActorsListAdapter.AdventureActorsViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull AdventureActorsListAdapter.AdventureActorsViewHolder holder, int position)
	{
		final AdventureEncounterActor actor = actorList.get(position);
		if (actor != null)
		{
			holder.ActorNameTv.setText(actor.getName());
			holder.ActorInitiativeTv.setText(String.format(Locale.getDefault(), "%d", actor
					.getInitiative()));
			if(actor.getStatus() == AdventureEncounterActor.DEAD)
				holder.ActorDeadTv.setVisibility(View.VISIBLE);
			else
				holder.ActorDeadTv.setVisibility(View.GONE);
			if(actor.getActorType() == AdventureEncounterActor.MONSTER_ACTOR)
				((AdventureEncounterMonster)actor).getToken().makePortrait(holder.ActorPortraitIv);
			else
			{
				Player player = Players.findOwner(context, ((AdventureEncounterPlayer) actor).getPc());
				Bitmap portrait = null;
				if(player != null)
					portrait = player.getMiniPortrait();
				if(portrait == null)
					holder.ActorPortraitIv.setImageResource(R.drawable.app_icon);
				else
					holder.ActorPortraitIv.setImageBitmap(portrait);
			}
		}
	}

	@Override
	public int getItemCount()
	{
		return actorList.size();
	}
	
	static class AdventureActorsViewHolder extends RecyclerView.ViewHolder
	{
		private TextView ActorNameTv, ActorInitiativeTv, ActorDeadTv;
		private ImageView ActorPortraitIv;
		
		AdventureActorsViewHolder(View itemView)
		{
			super(itemView);
			ActorNameTv = itemView.findViewById(R.id.ActorNameTv);
			ActorInitiativeTv = itemView.findViewById(R.id.ActorInitiativeTv);
			ActorDeadTv = itemView.findViewById(R.id.ActorDeadTv);
			ActorPortraitIv = itemView.findViewById(R.id.ActorPortraitIv);
		}
	}
}
