package net.kalinec.dndencounters.dice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.kalinec.dndencounters.R;

import java.util.ArrayList;
import java.util.List;

public class DiceButtonsListAdapter extends RecyclerView.Adapter<DiceButtonsListAdapter.DiceButtonsViewHolder>
{
	private List<DiceParser> dice = new ArrayList<>();

	private LayoutInflater layoutInflater;
	private Context context;

	public DiceButtonsListAdapter(Context context)
	{
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
	}

	public void setDice(List<DiceParser> dice)
	{
		this.dice.clear();
		this.dice.addAll(dice);
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public DiceButtonsListAdapter.DiceButtonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_dice_button, parent, false);
		return new DiceButtonsListAdapter.DiceButtonsViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull DiceButtonsListAdapter.DiceButtonsViewHolder holder, int position)
	{
		final DiceParser toRoll = dice.get(position);
		if (toRoll != null)
		{
			holder.DiceBt.setText(toRoll.getDiceStr());
			holder.DiceBt.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					final AlertDialog rollResultAlert = new AlertDialog.Builder(context).create();
					rollResultAlert.setTitle("Roll Result");
					rollResultAlert.setMessage("Rolled " + toRoll.getDiceStr() + ": " + Integer.toString(toRoll.result()));
					rollResultAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							rollResultAlert.dismiss();
						}
					});
					rollResultAlert.show();
				}
			});
		}
	}
	
	@Override
	public int getItemCount()
	{
		return dice.size();
	}
	
	static class DiceButtonsViewHolder extends RecyclerView.ViewHolder
	{
		private Button DiceBt;

		DiceButtonsViewHolder(View itemView)
		{
			super(itemView);
			DiceBt = itemView.findViewById(R.id.DiceBt);
		}
	}
}
