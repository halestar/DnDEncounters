package net.kalinec.dndencounters.modules;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.modules.Module;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ModuleListAdapter extends RecyclerView.Adapter<ModuleListAdapter.ModuleViewHolder>
{
	private static final Comparator<Module> ALPHABETICAL_COMPARATOR = new Comparator<Module>() {
		@Override
		public int compare(Module a, Module b) {
			return a.getUuid().compareTo(b.getUuid());
		}
	};
	
	private final SortedList<Module> moduleList = new SortedList<>(Module.class, new SortedList.Callback<Module>() {
		@Override
		public int compare(Module a, Module b) {
			return ALPHABETICAL_COMPARATOR.compare(a, b);
		}
		
		@Override
		public void onInserted(int position, int count) {
			notifyItemRangeInserted(position, count);
		}
		
		@Override
		public void onRemoved(int position, int count) {
			notifyItemRangeRemoved(position, count);
		}
		
		@Override
		public void onMoved(int fromPosition, int toPosition) {
			notifyItemMoved(fromPosition, toPosition);
		}
		
		@Override
		public void onChanged(int position, int count) {
			notifyItemRangeChanged(position, count);
		}
		
		@Override
		public boolean areContentsTheSame(Module oldItem, Module newItem) {
			return oldItem.equals(newItem);
		}
		
		@Override
		public boolean areItemsTheSame(Module item1, Module item2) {
			return item1.equals(item2);
		}
	});
	
	private LayoutInflater layoutInflater;
	private RvClickListener mListener;
	
	public ModuleListAdapter(Context context, RvClickListener listener)
	{
		this.layoutInflater = LayoutInflater.from(context);
		mListener = listener;
	}
	
	public void setModuleList(List<Module> newList)
	{
		moduleList.clear();
		moduleList.addAll(newList);
		notifyDataSetChanged();
	}
	
	
	public Module get(int position)
	{
		return moduleList.get(position);
	}
	
	@NonNull
	@Override
	public ModuleListAdapter.ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		final View itemView = layoutInflater.inflate(R.layout.item_list_module, parent, false);
		return new ModuleListAdapter.ModuleViewHolder(itemView, mListener);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ModuleListAdapter.ModuleViewHolder holder, int position)
	{
		final Module module = moduleList.get(position);
		if (module != null)
		{
			holder.ModuleNameTv.setText(module.getModuleName());
			holder.TierTv.setText(Integer.toString(module.getTier()));
			holder.NumEncountersTv.setText(Integer.toString(module.getEncounters().size()));
		}
	}
	
	@Override
	public int getItemCount()
	{
		return moduleList.size();
	}
	
	static class ModuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TextView ModuleNameTv, TierTv, NumEncountersTv;
		private RvClickListener mListener;
		
		ModuleViewHolder(View itemView, RvClickListener listener)
		{
			super(itemView);
			ModuleNameTv = itemView.findViewById(R.id.ModuleNameTv);
			TierTv = itemView.findViewById(R.id.TierTv);
			NumEncountersTv = itemView.findViewById(R.id.NumEncountersTv);
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
