package net.kalinec.dndencounters.lib;

public interface SelectableItem {
	boolean isSelected();
	
	void setSelected(boolean isSelected);
	
	String getSelectableText();

}
