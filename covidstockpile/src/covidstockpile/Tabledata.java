package covidstockpile;

import javax.swing.table.DefaultTableModel;

public class Tabledata extends DefaultTableModel {
	private static final Object[] tableHeadings = new Object[] { "Item name", "Item type", "Quantity", "Expiration date" };
	private boolean isEditable = false;
	Tabledata(){
		super(tableHeadings, 0);
	}
	public boolean isCellEditable(int row, int column) {
		return isEditable;
	}
	public void toggleEditing() {
		isEditable =! isEditable;
	}
	public void setDataVector(Object[][] data) {
		super.setDataVector(data, tableHeadings);
	}
	public void addItem(String itemname, String itemtype, String quantity, String expiration) {
		addRow(new Object[] {
				itemname, itemtype, quantity, expiration
		});
	}
}
