package net.kalinec.dndencounters.adventure_encounters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.encounters.EncounterPcUpdater;

import java.util.ArrayList;
import java.util.Locale;

public class AdventurePcListAdapter extends RecyclerView.Adapter<AdventurePcListAdapter.PcInitiativeViewHolder>
{

	public static ArrayList<EncounterPcUpdater> playerUpdates;
	private Context context;
	private LayoutInflater layoutInflater;

	public AdventurePcListAdapter(Context context, ArrayList<EncounterPcUpdater> playerUpdates)
	{
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.playerUpdates = playerUpdates;
	}

	public void setPlayerList(ArrayList<EncounterPcUpdater> playerList)
	{
		this.playerUpdates.clear();
		this.playerUpdates.addAll(playerList);
		notifyDataSetChanged();
	}

	public EncounterPcUpdater get(int position)
	{
		return this.playerUpdates.get(position);
	}

	@NonNull
	@Override
	public AdventurePcListAdapter.PcInitiativeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_adventure_players, parent, false);
		return new AdventurePcListAdapter.PcInitiativeViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull AdventurePcListAdapter.PcInitiativeViewHolder holder, int position)
	{
		final EncounterPcUpdater pc = playerUpdates.get(position);
		final int updaterPos = position;
		if (pc != null)
		{
			holder.PcNameTv.setText(pc.getPc().getName());
			holder.InitiativeEt.setText(String.format(Locale.getDefault(), "%d", pc
					.getInitiative()));
			holder.InitiativePositionEt.setText(String.format(Locale.getDefault(), "%d", pc
					.getInitiativePos()));
			holder.AcEt.setText(String.format(Locale.getDefault(), "%d", pc
					.getAc()));
			holder.HpEt.setText(String.format(Locale.getDefault(), "%d", pc
					.getHp()));
			holder.PpEt.setText(String.format(Locale.getDefault(), "%d", pc
					.getPp()));
			holder.SpellDcTv.setText(String.format(Locale.getDefault(), "%d", pc
					.getSpellDc()));
			holder.removePcBt.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					playerUpdates.remove(updaterPos);
					notifyDataSetChanged();
				}
			});
			if((position % 2) == 0)
				holder.rootLy.setBackgroundResource(R.color.RowStripe);
			else
				holder.rootLy.setBackgroundColor(Color.WHITE);
		}
	}

	@Override
	public int getItemCount()
	{
		return playerUpdates.size();
	}

	static class PcInitiativeViewHolder extends RecyclerView.ViewHolder
	{
		private TextView PcNameTv;
		private EditText InitiativeEt, InitiativePositionEt, AcEt, HpEt, PpEt, SpellDcTv;
		private Button removePcBt;
		private ConstraintLayout rootLy;
		
		PcInitiativeViewHolder(View itemView)
		{
			super(itemView);
			PcNameTv = itemView.findViewById(R.id.PcNameTv);
			rootLy = itemView.findViewById(R.id.rootLy);
			InitiativeEt = itemView.findViewById(R.id.InitiativeEt);
			InitiativeEt.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					playerUpdates.get(getAdapterPosition()).setInitiative(Integer.parseInt(InitiativeEt.getText().toString()));
				}

				@Override
				public void afterTextChanged(Editable s)
				{

				}
			});
			InitiativePositionEt = itemView.findViewById(R.id.InitiativePositionEt);
			InitiativePositionEt.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					try{playerUpdates.get(getAdapterPosition()).setInitiativePos(Integer.parseInt(InitiativePositionEt.getText().toString()));}
					catch (NumberFormatException e)
					{
						InitiativePositionEt.setText(Integer.toString(playerUpdates.get(getAdapterPosition()).getInitiativePos()));
					}
				}

				@Override
				public void afterTextChanged(Editable s)
				{

				}
			});
			AcEt = itemView.findViewById(R.id.AcEt);
			AcEt.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					playerUpdates.get(getAdapterPosition()).setAc(Integer.parseInt(AcEt.getText().toString()));
				}

				@Override
				public void afterTextChanged(Editable s)
				{

				}
			});
			HpEt = itemView.findViewById(R.id.HpEt);
			HpEt.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					playerUpdates.get(getAdapterPosition()).setHp(Integer.parseInt(HpEt.getText().toString()));
				}

				@Override
				public void afterTextChanged(Editable s)
				{

				}
			});
			PpEt = itemView.findViewById(R.id.PpEt);
			PpEt.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					playerUpdates.get(getAdapterPosition()).setPp(Integer.parseInt(PpEt.getText().toString()));
				}

				@Override
				public void afterTextChanged(Editable s)
				{

				}
			});
			SpellDcTv = itemView.findViewById(R.id.SpellDcTv);
			SpellDcTv.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after)
				{

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					playerUpdates.get(getAdapterPosition()).setSpellDc(Integer.parseInt(SpellDcTv.getText().toString()));
				}

				@Override
				public void afterTextChanged(Editable s)
				{

				}
			});

			removePcBt = itemView.findViewById(R.id.removePcBt);
		}
	}
}
