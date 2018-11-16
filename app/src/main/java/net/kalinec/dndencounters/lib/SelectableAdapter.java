package net.kalinec.dndencounters.lib;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kalinec.dndencounters.R;

import java.util.ArrayList;
import java.util.List;

public class SelectableAdapter extends RecyclerView.Adapter implements RvSelectListener {

    private final List<SelectableItem> mValues;
    private boolean isMultiSelectEnabled = false;
    RvSelectListener mListener;

    public SelectableAdapter(List<SelectableItem> items, boolean isMultiSelectEnabled, RvSelectListener listener)
    {
        mListener = listener;
        mValues = items;
        for(SelectableItem i: mValues)
            i.setSelected(false);
        this.isMultiSelectEnabled = isMultiSelectEnabled;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.checked_item_view, viewGroup, false);
        return new SelectableViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        SelectableViewHolder holder = (SelectableViewHolder)viewHolder;
        SelectableItem selectableItem = mValues.get(i);
        String name = selectableItem.getSelectableText();
        holder.textView.setText(name);
        TypedValue value = new TypedValue();
        if(isMultiSelectEnabled)
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
        else
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
        int checkMarkDrawableResId = value.resourceId;
        holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        holder.mItem = selectableItem;
        holder.setChecked(holder.mItem.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<SelectableItem> getSelectedItems() {

        List<SelectableItem> selectedItems = new ArrayList<>();
        for (SelectableItem item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectEnabled){
            return SelectableViewHolder.MULTI_SELECT;
        }
        else{
            return SelectableViewHolder.SINGLE_SELECT;
        }
    }

    @Override
    public void onItemSelected(SelectableItem item) {
        if(!isMultiSelectEnabled)
        {
            for(SelectableItem sItem: mValues)
            {
                if(!sItem.equals(item) && sItem.isSelected())
                    sItem.setSelected(false);
                else if(sItem.equals(item) && sItem.isSelected())
                    sItem.setSelected(true);
            }
            notifyDataSetChanged();
        }
        mListener.onItemSelected(item);
    }
}
