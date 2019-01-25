package net.kalinec.dndencounters.parties;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PartyListAdapter extends RecyclerView.Adapter<PartyListAdapter.PartyViewHolder>
{

	private List<Character> partyMembers;

	private LayoutInflater layoutInflater;
	private RvClickListener mListener;

	public PartyListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		mListener = listener;
		partyMembers = new ArrayList<>();
	}

	public void setParty(Party newParty)
	{
		partyMembers.clear();
		partyMembers.addAll(newParty.getMembers());
		notifyDataSetChanged();
	}

	public Character get(int position)
	{
		return partyMembers.get(position);
	}

	@NonNull
	@Override
	public PartyListAdapter.PartyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_party_members, parent, false);
		return new PartyListAdapter.PartyViewHolder(itemView, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull PartyListAdapter.PartyViewHolder holder, int position)
	{
		if (partyMembers == null)
		{
			return;
		}
		final Character pc = partyMembers.get(position);
		if (pc != null)
		{
			holder.partyMemberNameTv.setText(pc.getName());
			holder.partyMemberClass.setText(pc.getCharacterClass());
			holder.PartyMemberLv.setText(String.format(Locale.getDefault(), "%d", pc.getLevel()));
		}
	}
	
	@Override
	public int getItemCount()
	{
		if (partyMembers == null)
		{
			return 0;
		}
		else
		{
			return partyMembers.size();
		}
	}
	
	static class PartyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView partyMemberNameTv, partyMemberClass, PartyMemberLv;
		private Button removePartyMembeBtn;
		private RvClickListener mListener;
		
		PartyViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			partyMemberNameTv = itemView.findViewById(R.id.EncounterNameTv);
			partyMemberClass = itemView.findViewById(R.id.partyMemberClass);
			PartyMemberLv = itemView.findViewById(R.id.PartyMemberLv);
			removePartyMembeBtn = itemView.findViewById(R.id.RemoveEncounterBt);
			mListener = listener;
			removePartyMembeBtn.setOnClickListener(this);
		}
		
		@Override
		public void onClick(View v)
		{
			mListener.onClick(v, getAdapterPosition());
		}
	}
}
