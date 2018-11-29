package net.kalinec.dndencounters.adventure_encounters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.monsters.Monster;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdventureActorsListAdapter extends RecyclerView.Adapter<AdventureActorsListAdapter.AdventureActorsViewHolder>
{
	private static final Comparator<AdventureEncounterActor> INITIATIVE_COMPARATOR = new Comparator<AdventureEncounterActor>() {
		@Override
		public int compare(AdventureEncounterActor a, AdventureEncounterActor b) {
			return (-1) * Integer.valueOf(a.getInitiative()).compareTo(Integer.valueOf(b.getInitiative()));
		}
	};

	private final List<AdventureEncounterActor> actorList = new ArrayList<>();

	private LayoutInflater layoutInflater;
	private Context context;

	public AdventureActorsListAdapter(Context context)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
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
		if (actorList == null)
		{
			return;
		}
		final AdventureEncounterActor actor = actorList.get(position);
		if (actor != null)
		{
			holder.ActorNameTv.setText(actor.getName());
			holder.ActorHpTv.setText(Integer.toString(actor.getHp()));
			holder.ActorInitiativeTv.setText(Integer.toString(actor.getInitiative()));
			if(actor.getStatus() == AdventureEncounterActor.DEAD)
				holder.ActorDeadTv.setVisibility(View.VISIBLE);
			else
				holder.ActorDeadTv.setVisibility(View.GONE);
			if(actor.getActorType() == AdventureEncounterActor.MONSTER_ACTOR)
				((AdventureEncounterMonster)actor).getToken().makePortrait(holder.ActorPortraitIv);
			else
			{
				Bitmap portrait = ((AdventureEncounterPlayer) actor).getPlayer().getMiniPortrait();
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
		if (actorList == null)
		{
			return 0;
		}
		else
		{
			return actorList.size();
		}
	}

	public static class AdventureActorsViewHolder extends RecyclerView.ViewHolder
	{
		private TextView ActorNameTv, ActorHpTv, ActorInitiativeTv, ActorDeadTv;
		private ImageView ActorPortraitIv;

		public AdventureActorsViewHolder(View itemView)
		{
			super(itemView);
			ActorNameTv = itemView.findViewById(R.id.ActorNameTv);
			ActorHpTv = itemView.findViewById(R.id.ActorHpTv);
			ActorInitiativeTv = itemView.findViewById(R.id.ActorInitiativeTv);
			ActorDeadTv = itemView.findViewById(R.id.ActorDeadTv);
			ActorPortraitIv = itemView.findViewById(R.id.ActorPortraitIv);
		}
	}
}
