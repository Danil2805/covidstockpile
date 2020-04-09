package covidstockpile;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class AddTab extends JPanel {
	private Tabledata tabledata;
	AddTab (Tabledata tabledata, JTabbedPane tabbedPane) {
		this.tabledata = tabledata;
		setLayout(null);

		JLabel itemname = new JLabel("Item name:");
		itemname.setFont(new Font("Tahoma", Font.PLAIN, 16));
		itemname.setBounds(10, 11, 89, 20);
		add(itemname);

		JLabel quantity = new JLabel("Quantity:");
		quantity.setFont(new Font("Tahoma", Font.PLAIN, 16));
		quantity.setBounds(10, 73, 71, 20);
		add(quantity);

		JLabel expiration = new JLabel("Expires:");
		expiration.setFont(new Font("Tahoma", Font.PLAIN, 16));
		expiration.setBounds(10, 104, 71, 20);
		add(expiration);
		expiration.setVisible(false);

		JTextArea nameInput = new JTextArea();
		nameInput.setBounds(99, 11, 113, 20);
		add(nameInput);

		JSpinner quantityInput = new JSpinner();
		quantityInput.setBounds(99, 73, 113, 20);
		add(quantityInput);

		JTextArea expireInput = new JTextArea();
		expireInput.setBounds(99, 104, 113, 20);
		add(expireInput);
		expireInput.setVisible(false);

		JLabel itemType = new JLabel("Item type:");
		itemType.setFont(new Font("Tahoma", Font.PLAIN, 16));
		itemType.setBounds(10, 42, 89, 20);
		add(itemType);

		JComboBox typeChoice = new JComboBox(new Object[]{});
		typeChoice.setModel(new DefaultComboBoxModel(new String[] {"", "Basic", "Food", "Drink"}));
		typeChoice.setBounds(99, 42, 113, 22);
		add(typeChoice);

		typeChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(typeChoice.getSelectedItem() == "Drink" || typeChoice.getSelectedItem() =="Food") {
					expiration.setVisible(true);
					expireInput.setVisible(true);
				} else {
					expiration.setVisible(false);
					expireInput.setVisible(false);

				}
			}
		});
		
		JComboBox unitsChoice = new JComboBox();
		unitsChoice.setModel(new DefaultComboBoxModel(new String[] {"", "kg", "g", "ml", "l"}));
		unitsChoice.setBounds(217, 73, 113, 20);
		add(unitsChoice);

		JButton addtotable = new JButton("Add");
		addtotable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameInput.getText();
				String type = (String)typeChoice.getSelectedItem();
				int inpquantity = (int)quantityInput.getValue();
				String expires = expireInput.getText();
				String units = (String)unitsChoice.getSelectedItem();
				tabledata.addItem(name,type,inpquantity+units,expires);
				tabbedPane.setSelectedIndex(0);
				nameInput.setText("");
				quantityInput.setValue(0);
				expireInput.setText("");
				typeChoice.setSelectedIndex(0);
				unitsChoice.setSelectedIndex(0);
			}
		});
		addtotable.setBounds(10, 135, 100, 23);
		add(addtotable);
	}

}
