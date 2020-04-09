package covidstockpile;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.google.gson.Gson;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.Font;


import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTable table_1;
	private JTable table;
	private JTextField filterTextField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame() {
		Tabledata model = new Tabledata();
		setTitle("Stockpiling app");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 745, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		tabbedPane.addTab("Main Screen", null, panel, null);

		tabbedPane.add(new AddTab(model, tabbedPane), "Add item tab");

		

		JButton addButton = new JButton("Add Item");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		addButton.setBounds(10, 11, 100, 23);
		panel.add(addButton);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 76, 698, 307);
		panel.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		
		TableRowSorter sorter = new TableRowSorter(model);
		table.setRowSorter(sorter);
		
		filterTextField = new JTextField();
		filterTextField.setBounds(133, 46, 452, 20);
		panel.add(filterTextField);
		filterTextField.setColumns(10);
		
		filterTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				model.fireTableDataChanged();				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				model.fireTableDataChanged();				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				model.fireTableDataChanged();			
			}
			
		});
		
		sorter.setRowFilter(new RowFilter() {
			public boolean include(Entry entry) {
				String name = entry.getValue(0).toString();
				String searchText = filterTextField.getText();
				return name.startsWith(searchText);
			}
		});
		
		String options[] = {"All","Food","Drink","Basic"};
		JComboBox comboBox = new JComboBox(options);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sorter.setRowFilter(new RowFilter() {
					public boolean include(Entry entry) {
						String name1 = entry.getValue(1).toString();
						String itemType = (String)comboBox.getSelectedItem();
						if((String)comboBox.getSelectedItem()=="All") {
							itemType = "";
						}
						return name1.startsWith(itemType);
					}
				});
			}
		});
		comboBox.setBounds(10, 45, 113, 22);
		panel.add(comboBox);

		JToggleButton editButton = new JToggleButton("Edit table");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.toggleEditing();
			}
		});
		editButton.setBounds(608, 11, 100, 23);
		panel.add(editButton);

		table.setModel(model);

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveData("C:\\Users\\danil\\Documents\\!school\\text.json", model);
			}
		});
		saveButton.setBounds(10, 389, 89, 23);
		panel.add(saveButton);

		JButton removeBut = new JButton("Remove row");
		removeBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelectedRowFromJtable(model);
			}
		});
		removeBut.setBounds(595, 45, 113, 23);
		panel.add(removeBut);
		
		JButton about = new JButton("About");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Made by Danylo Kirchatyi, Created on 08/04/2020, Version 1.0",
						"About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		about.setBounds(639, 389, 69, 23);
		panel.add(about);
		
		

		loadData("C:\\Users\\danil\\Documents\\!school\\text.json", model);
	}
	void saveData(String filename, Tabledata model) {
		Gson gson = new Gson();
		Vector dataVector = model.getDataVector();
		String textData = gson.toJson(dataVector);


		Path path = Paths.get(filename);
		try {
			Files.writeString(path,textData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void loadData(String filename, Tabledata model) {
		Path path = Paths.get(filename);
		try {
			String textData = Files.readString(path);
			System.out.println(textData);

			Gson gson = new Gson();
			Object[][] tableData = gson.fromJson(textData, Object[][].class);
			model.setDataVector(tableData);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void deleteSelectedRowFromJtable(Tabledata model) {       
		int getSelectedRowForDeletion = table.getSelectedRow();
		if (getSelectedRowForDeletion >= 0) {
			model.removeRow(getSelectedRowForDeletion);
			JOptionPane.showMessageDialog(null, "Row Deleted");
		} else {
			JOptionPane.showMessageDialog(null,"Unable To Delete", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}