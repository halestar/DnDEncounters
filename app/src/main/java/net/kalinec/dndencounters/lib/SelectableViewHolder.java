package net.kalinec.dndencounters.lib;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;

import net.kalinec.dndencounters.R;


public class SelectableViewHolder extends RecyclerView.ViewHolder {
	static final int MULTI_SELECT = 2;
	static final int SINGLE_SELECT = 1;
	SelectableItem mItem;
    protected RvSelectListener mListener;
    protected CheckedTextView textView;
	
	SelectableViewHolder(@NonNull View itemView, RvSelectListener listener)
	{
        super(itemView);
        mListener = listener;
		textView = itemView.findViewById(R.id.checked_text_item);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItem.isSelected() && getItemViewType() == MULTI_SELECT)
                    setChecked(false);
                else
                    setChecked(true);
                mListener.onItemSelected(mItem);
            }
        });

    }
    public void setChecked(boolean value) {
        if (value) {
            textView.setBackgroundColor(Color.LTGRAY);
        } else {
            textView.setBackground(null);
        }
        mItem.setSelected(value);
        textView.setChecked(value);
    }
}
