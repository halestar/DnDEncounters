package net.kalinec.dndencounters.lib;

import android.view.View;

import java.io.Serializable;

public interface RvClickListener extends Serializable
{
	void onClick(View view, int position);
}
