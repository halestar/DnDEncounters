package net.kalinec.dndencounters.players;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder>
{
	private LayoutInflater layoutInflater;
	private List<Player> playerList;
	private Context context;
	private RvClickListener mListener;
	
	
	public PlayerListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		this.mListener = listener;
		this.playerList = new ArrayList<>();
		
	}
	
	public void setPlayerList(List<Player> playerList)
	{
		this.playerList.clear();
		this.playerList = playerList;
		notifyDataSetChanged();
	}

	public List<Player> getPlayerList() {
		return playerList;
	}

	public Player get(int position)
	{
		return playerList.get(position);
	}
	
	@NonNull
	@Override
	public PlayerListAdapter.PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_player, parent, false);
		return new PlayerListAdapter.PlayerViewHolder(itemView, mListener);
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
			int numPcs = player.getPcs().size();
			holder.PlayerNumPcs.setText(
					String.format(Locale.getDefault(), "%d", numPcs) + " PC(s)");
			if(player.getPortrait() != null) {
				Bitmap bmp = BitmapFactory.decodeByteArray(player.getPortrait(), 0, player.getPortrait().length);
				holder.PlayerPortraitIv.setImageBitmap(bmp);
			}
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
	
	static class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView PlayerNameTv, PlayerNumPcs;
		private ImageView PlayerPortraitIv;
		private RvClickListener mListener;
		
		PlayerViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			PlayerNameTv = itemView.findViewById(R.id.PlayerNameTv);
			PlayerNumPcs = itemView.findViewById(R.id.PlayerNumPcs);
			PlayerPortraitIv = itemView.findViewById(R.id.MonsterTokenPortrait);
			mListener = listener;
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mListener.onClick(v, getAdapterPosition());
		}
	}
}
