package net.kalinec.dndencounters.players;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.ViewPlayer;
import net.kalinec.dndencounters.db.AppDatabase;

import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder>
{
	private LayoutInflater layoutInflater;
	private List<Player> playerList;
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
	
	
	public PlayerListAdapter(Context context)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.destinationIntent = new Intent(context, ViewPlayer.class);
		
	}
	
	public void setPlayerList(List<Player> playerList)
	{
		this.playerList = playerList;
		notifyDataSetChanged();
	}
	
	@NonNull
	@Override
	public PlayerListAdapter.PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_player, parent, false);
		return new PlayerListAdapter.PlayerViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(@NonNull PlayerListAdapter.PlayerViewHolder holder, int position)
	{
		if (playerList == null)
		{
			return;
		}
		final Player player = playerList.get(position);
		if (player != null)
		{
			holder.PlayerNameTv.setText(player.getName());
			int numPcs = AppDatabase.getDatabase(context).playerDao().numPcs(player.getUid());
			holder.PlayerNumPcs.setText(Integer.toString(numPcs) + " PC(s)");
			Bitmap bmp = BitmapFactory.decodeByteArray(player.getPortrait(), 0, player.getPortrait().length);
			holder.PlayerPortraitIv.setImageBitmap(bmp);
			holder.itemView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Bundle bundle = new Bundle();
					bundle.putSerializable(Player.PASSED_PLAYER, player);
					destinationIntent.putExtras(bundle);
					context.startActivity(destinationIntent);
				}
			});
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
	
	static class PlayerViewHolder extends RecyclerView.ViewHolder
	{
		private TextView PlayerNameTv, PlayerNumPcs;
		private ImageView PlayerPortraitIv;
		
		public PlayerViewHolder(View itemView)
		{
			super(itemView);
			PlayerNameTv = itemView.findViewById(R.id.PlayerNameTv);
			PlayerNumPcs = itemView.findViewById(R.id.PlayerNumPcs);
			PlayerPortraitIv = itemView.findViewById(R.id.PlayerPortraitIv);
		}
	}
}
