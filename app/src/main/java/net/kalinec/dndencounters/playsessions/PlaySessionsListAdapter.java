package net.kalinec.dndencounters.playsessions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlaySessionsListAdapter extends RecyclerView.Adapter<PlaySessionsListAdapter.PlaySessionsViewHolder>
{

	private final List<PlaySession> sessionList = new ArrayList<>();

	private LayoutInflater layoutInflater;
	private Context context;
	private RvClickListener mListener;

	public PlaySessionsListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		mListener = listener;
	}

	public void setSessionList(List<PlaySession> newList)
	{
		sessionList.clear();
		sessionList.addAll(newList);
		notifyDataSetChanged();
	}


	public PlaySession get(int position)
	{
		return sessionList.get(position);
	}

	@NonNull
	@Override
	public PlaySessionsListAdapter.PlaySessionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_active_adventures, parent, false);
		return new PlaySessionsListAdapter.PlaySessionsViewHolder(itemView, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull PlaySessionsListAdapter.PlaySessionsViewHolder holder, int position)
	{
		if (sessionList == null)
		{
			return;
		}
		final PlaySession session = sessionList.get(position);
		if (session != null)
		{
			holder.PlaySessionNameTv.setText(session.getSessionName());
			DateFormat df = new SimpleDateFormat("MM/dd/yy");
			holder.AdventureCreatedOnTv.setText(df.format(session.getStarted()));
		}
	}
	
	@Override
	public int getItemCount()
	{
		if (sessionList == null)
		{
			return 0;
		}
		else
		{
			return sessionList.size();
		}
	}
	
	static class PlaySessionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView PlaySessionNameTv, AdventureCreatedOnTv;
		private RvClickListener mListener;
		
		public PlaySessionsViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			PlaySessionNameTv = itemView.findViewById(R.id.PlaySessionNameTv);
			AdventureCreatedOnTv = itemView.findViewById(R.id.AdventureCreatedOnTv);
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
