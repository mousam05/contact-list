import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


public class ContactList {
	
	
	
	private static JTextField txtNameSearch;
	private static JButton btnRemove, btnView;
	private static JComboBox<String> cmbMain;
	private static JList<String> listMain;
	private static ArrayList<Integer> indexes;
	private static ArrayList<String> snames;
	
	//-----utility functions---------------------------------------------//
	
	//-----to accept a string of length <=100
	private static String readString100(String s) throws Exception{
		if(s.length()>100 || s.length() < 1)
			throw new Exception();
		return s;
	}
	
	//----to parse a string as a date
	private static Date setDate(String s) throws Exception{
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date d = df.parse(s);
		return d;
	}
	
	//----to update list of contacts when a contact is added/removed or type of contacts to be displayed is changed
	public static void updateMainList(ArrayList<Acquaintance> arr){
		int ch;
		try{
			ch = cmbMain.getSelectedIndex();
		}catch(Exception e){
			return;
		}
		
		ArrayList<String> names = new ArrayList<String>();
		
		if(ch == 4){
			for(int i=0; i<arr.size(); i++)
				names.add(arr.get(i).getName());
		}
		
		if(ch == 0){
			for(int i=0; i<arr.size(); i++)
				if(arr.get(i).getClass() == Relative.class)
					names.add(arr.get(i).getName());
		}
		
		if(ch == 1){
			for(int i=0; i<arr.size(); i++)
				if(arr.get(i).getClass() == Personal.class)
					names.add(arr.get(i).getName());
		}
		if(ch == 2){
			for(int i=0; i<arr.size(); i++)
				if(arr.get(i).getClass() == Professional.class)
					names.add(arr.get(i).getName());
		}
		if(ch == 3){
			for(int i=0; i<arr.size(); i++)
				if(arr.get(i).getClass() == Casual.class)
					names.add(arr.get(i).getName());
		}
		
		try{
			String[] temp = new String[names.size()];
			temp = names.toArray(temp);
			listMain.setListData(temp);
			btnView.setEnabled(names.size()>0);
			btnRemove.setEnabled(names.size()>0);
			if(names.size()>0) listMain.setSelectedIndex(0);
		}catch(Exception e){
			return;
		}
		
	}
	
	public static void saveToFile(ArrayList<Acquaintance> arr){
		try{
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("contacts"));
			os.writeObject(arr);
			os.close();
		}catch(Exception e){
			System.out.println( "Some error occurred during saving records to file.");
		}
	}
	//-----------end of utility functions-----------------------------------//
	//-----------static classes---------------------------------------------//
	
	
	
	public static class Acquaintance implements Serializable{
		protected String name, mobNo, email;
		protected DateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
		
		protected String getBasic(){
			return "Name: " + name + "\nMobile no.: " + mobNo + "\nEmail: " + email;
		}
		
		public String show(){
				return null;
		} //to be overridden
		
		public void set(JFrame w){
			
			JTextField nameF = new JTextField(), mobNoF = new JTextField(), emailF = new JTextField();
			int option;
			Object[] message = {
					"Name", nameF, "Mobile no.", mobNoF, "Email", emailF, new JLabel("All fields required*")
			};
			
			do{
				
				 option = JOptionPane.showConfirmDialog(w, message , "New aquaintance: Part 1 of 2", JOptionPane.OK_CANCEL_OPTION);
			}while(option != JOptionPane.OK_OPTION || nameF.getText().equals("") || mobNoF.getText().equals("") || emailF.getText().equals("") );
			
			name = nameF.getText();
			mobNo = mobNoF.getText();
			email = emailF.getText();
		}
		
		public String getName(){
			return name;
		}
		
	}
	
	public static class Relative extends Acquaintance implements Serializable{
		private Date bday, lastmeet;
		
		public void set(JFrame w){
			super.set(w);
			JTextField bdayF = new JTextField(), lastmeetF = new JTextField();
			int option;
			Object[] message = {
					"Birthday", bdayF, "Date of last meeting", lastmeetF, new JLabel("Format dd/mm/yyyy")
			};
			
			int flag;
			do{
				 flag = 1;
				 option = JOptionPane.showConfirmDialog(w, message , "New aquaintance: Part 2 of 2", JOptionPane.OK_CANCEL_OPTION);
				 try{
					bday = setDate(bdayF.getText());
					lastmeet = setDate(lastmeetF.getText());
					}catch(Exception e){
						flag = 0;
				}
			}while(option != JOptionPane.OK_OPTION|| flag == 0);
		}
		
		public String show(){
			return getBasic() + "\nBirthday: " + datef.format(bday) + "\nDate of last meeting: " + datef.format(lastmeet) + "\n[RELATIVE]";
			
		}
	}
	
	public static class Personal extends Acquaintance implements Serializable{
		private String context, events;
		private Date acqdate;
		
		public void set(JFrame w){
			super.set(w);
			
			JTextField contextF = new JTextField(), eventsF = new JTextField(), acqdateF = new JTextField();
			int option;
			Object[] message = {
					"Context of meeting", contextF, "Other useful events", eventsF, "Date of acquaintance", acqdateF,
					new JLabel("Format dd/mm/yyyy, all fields required")
			};
			
			int flag;
			do{
				 flag = 1;
				 option = JOptionPane.showConfirmDialog(w, message , "New aquaintance: Part 2 of 2", JOptionPane.OK_CANCEL_OPTION);
				 try{
					acqdate = setDate(acqdateF.getText());
					context = readString100(contextF.getText());
					events = readString100(eventsF.getText());
					}catch(Exception e){
						flag = 0;
				}
			}while(option != JOptionPane.OK_OPTION|| flag == 0);
			
		}
		
		public String show(){
			return getBasic() + "\nContext of meeting: " + context + "\nOther useful events: " + events + "\nDate of acquaintance: " + datef.format(acqdate) + "\n[PERSONAL]";
			
		}
		
	}
	
	public static class Professional extends Acquaintance implements Serializable{
		private String common;
		
		public void set(JFrame w){
			super.set(w);
			JTextField commonF = new JTextField();
			int option;
			Object[] message = {
					"Common interests", commonF, new JLabel("All fields required*")
			};
			int flag;
			do{
				flag = 1;
				
				 option = JOptionPane.showConfirmDialog(w, message , "New aquaintance: Part 2 of 2", JOptionPane.OK_CANCEL_OPTION);
				 try{
					common =readString100(commonF.getText());
				}catch(Exception e){
							flag = 0;
				}
			}while(option != JOptionPane.OK_OPTION || flag == 0);
			
			
			
		}
		public String show(){
			return getBasic() + "\nCommon interests: " + common + "\n[PROFESSIONAL]";
			
		}
	}
	
	
	public static class Casual extends Acquaintance implements Serializable{
		private Date meet;
		private String placemeet, ccstances, other;
		
		public void set(JFrame w){
			super.set(w);
						
			JTextField placemeetF = new JTextField(), ccstancesF = new JTextField(), otherF = new JTextField(),  meetF = new JTextField();
			int option;
			Object[] message = {
					"Place of meeting", placemeetF, "Circumstances of meeting",ccstancesF, "Other useful events", otherF, "Date of meeting", meetF,
					new JLabel("Format dd/mm/yyyy, all fields required")
			};
			
			int flag;
			do{
				 flag = 1;
				 option = JOptionPane.showConfirmDialog(w, message , "New aquaintance: Part 2 of 2", JOptionPane.OK_CANCEL_OPTION);
				 try{
					meet = setDate(meetF.getText());
					placemeet = readString100(placemeetF.getText());
					ccstances = readString100(ccstancesF.getText());
					other = readString100(otherF.getText());
					}catch(Exception e){
						flag = 0;
				}
			}while(option != JOptionPane.OK_OPTION|| flag == 0);
			
			
		}
		public String show(){
			return getBasic() + "\nPlace of meeting: " + placemeet + "\nCircumstances of meeting: " + ccstances + "\nOther useful details: " + other + "\nDate of meeting: " + datef.format(meet) + "\n[CASUAL]";
		}
	}

	//to return cth member of class cl
	public static Acquaintance getMember(int c, Class<? extends Acquaintance> cl, ArrayList<Acquaintance> arr){
		
		int temp = -1;
		for(int i=0; i<arr.size(); i++){
			if(arr.get(i).getClass() == cl){
				temp++;
				if(temp == c){
					return arr.get(i);
				}
			}
		}
		
		return null;
	}
	
	
	public static void ContactListGUI(JFrame window, ArrayList<Acquaintance> arr){
		String[] typesAll = {"Relative", "Personal", "Professional", "Casual", "All"};
		window.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setEnabled(false);
		tabbedPane.setBounds(0, 0, 498, 465);
		window.getContentPane().add(tabbedPane);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(tabbedPane.getSelectedIndex() == 0 ){
					updateMainList(arr);
				}
			}
		});
		JPanel mainTab = new JPanel();
		
		tabbedPane.addTab("Main", null, mainTab, null);
		mainTab.setLayout(null);
		
		JLabel lblChooseType = new JLabel("Choose type:");
		lblChooseType.setBounds(39, 46, 117, 16);
		mainTab.add(lblChooseType);
		
		cmbMain = new JComboBox<String>(typesAll);
		cmbMain.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			
				updateMainList(arr);
				
			}
		});
		
		cmbMain.setSelectedIndex(4);
		cmbMain.setBounds(247, 42, 156, 25);
		mainTab.add(cmbMain);
		
		
		JScrollPane scrollmain = new JScrollPane();
		
		mainTab.add(scrollmain);
		scrollmain.setBounds(39, 86, 364, 145);
		
		listMain = new JList<String>();
		scrollmain.setViewportView(listMain);
		listMain.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ch = cmbMain.getSelectedIndex();
				int c = listMain.getSelectedIndex();
				switch(ch){
				case 0:
					arr.remove(getMember(c, Relative.class, arr));
					break;
				case 1:
					arr.remove(getMember(c, Personal.class, arr));
					break;
				case 2:
					arr.remove(getMember(c, Professional.class, arr));
					break;
				case 3:
					arr.remove(getMember(c, Casual.class, arr));
					break;
				case 4:
					arr.remove(c);
				}
				
				updateMainList(arr);
			}
		});
		btnRemove.setBounds(39, 247, 110, 26);
		mainTab.add(btnRemove);
		
		btnView = new JButton("View");
		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ch = cmbMain.getSelectedIndex();
				int c = listMain.getSelectedIndex();
				switch(ch){
				case 0:
					JOptionPane.showMessageDialog(window, getMember(c, Relative.class, arr).show(), "Acquaintance details", 1);
					break;
				case 1:
					JOptionPane.showMessageDialog(window, getMember(c, Personal.class, arr).show(), "Acquaintance details", 1);
					break;
				case 2:
					JOptionPane.showMessageDialog(window, getMember(c, Professional.class, arr).show(), "Acquaintance details", 1);
					break;
				case 3:
					JOptionPane.showMessageDialog(window, getMember(c, Casual.class, arr).show(), "Acquaintance details", 1);
					break;
				case 4:
					JOptionPane.showMessageDialog(window, arr.get(c).show(), "Acquaintance details", 1);
					
				}
			}
		});
		btnView.setBounds(163, 247, 110, 26);
		mainTab.add(btnView);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				saveToFile(arr);
				System.exit(0);
			}
			
		});
		btnClose.setBounds(285, 358, 110, 26);
		mainTab.add(btnClose);
		
		JButton btnAddNew = new JButton("Add new...");
		btnAddNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ch = cmbMain.getSelectedIndex();
				if(ch == -1 || ch == 4){
					JOptionPane.showMessageDialog(window, "Select a specific contact type.");
					return;
				}
				
				switch(ch){
				case 0:
					arr.add(new Relative());
					break;
				case 1:
					arr.add(new Personal());
					break;
				case 2:
					arr.add(new Professional());
					break;
				case 3:
					arr.add(new Casual());
				
				}
				
				arr.get(arr.size() - 1).set(window);
				updateMainList(arr);
			}
			
		});
		btnAddNew.setBounds(285, 247, 110, 26);
		mainTab.add(btnAddNew);
		
		JButton btnSearch_1 = new JButton("Search...");
		btnSearch_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		btnSearch_1.setBounds(285, 317, 110, 26);
		mainTab.add(btnSearch_1);
		
		JPanel searchTab = new JPanel();
		tabbedPane.addTab("Search", null, searchTab, null);
		searchTab.setLayout(null);
		
		JLabel lblEnterName = new JLabel("Name:");
		lblEnterName.setBounds(48, 44, 80, 16);
		searchTab.add(lblEnterName);
		
		txtNameSearch = new JTextField();
		txtNameSearch.setBounds(146, 42, 142, 20);
		searchTab.add(txtNameSearch);
		txtNameSearch.setColumns(10);
		
		JScrollPane scrollsearch = new JScrollPane();
		
		scrollsearch.setBounds(146, 147, 279, 126);
		searchTab.add(scrollsearch);
		
		JList<String> listSearchList = new JList<String>();
		scrollsearch.setViewportView(listSearchList);
		
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String nm = txtNameSearch.getText();
				indexes = new ArrayList<Integer>();
				snames = new ArrayList<String>();
				int found = 0;
				for(int i=0; i<arr.size(); i++){
					if(arr.get(i).getName().contains(nm)){
						
						snames.add( arr.get(i).getName() + " (" +  arr.get(i).getClass().toString() + ")");
						indexes.add(new Integer(i));
						found++;
					}
				}
				if(found == 0){
					JOptionPane.showMessageDialog(window, "Contact not found!");
					return;
				}
				
				String[] temps = new String[snames.size()];
				temps = snames.toArray(temps);
				listSearchList.setListData(temps);				
				
			}
		});
		btnSearch.setBounds(315, 39, 110, 26);
		searchTab.add(btnSearch);
		
		JLabel lblSearchResults = new JLabel("Search results:");
		lblSearchResults.setBounds(48, 119, 142, 16);
		searchTab.add(lblSearchResults);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(0);
				listSearchList.setListData(new String[]{});
				snames = null;
				indexes = null;
			}
		});
		btnBack.setBounds(315, 357, 110, 26);
		searchTab.add(btnBack);
		
		JButton btnRemove_1 = new JButton("Remove");
		btnRemove_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ch = listSearchList.getSelectedIndex();
				if(ch == -1){
					JOptionPane.showMessageDialog(window, "Please select a contact.", "Error!", 0);
					return;
				}
				
				arr.remove((int) indexes.get(ch));
				indexes.remove(ch);
				snames.remove(ch);
				String[] temps = new String[snames.size()];
				temps = snames.toArray(temps);
				listSearchList.setListData(temps);	
				
				for(int i=ch; i<indexes.size(); i++)
					indexes.set(i, indexes.get(i) - 1);
			}
			
		});
		btnRemove_1.setBounds(315, 308, 110, 26);
		searchTab.add(btnRemove_1);
		
		JButton btnView_1 = new JButton("View");
		btnView_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ch = listSearchList.getSelectedIndex();
				if(ch == -1){
					JOptionPane.showMessageDialog(window, "Please select a contact.", "Error!", 0);
					return;
				}
				
				JOptionPane.showMessageDialog(window, arr.get(indexes.get(ch)).show(), "Acquaintance details", 1);
				
			}
		});
		btnView_1.setBounds(184, 308, 110, 26);
		searchTab.add(btnView_1);
		
		updateMainList(arr);
	}
	//--------------------end of static classes-------------------------------------//
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		
		ArrayList<Acquaintance> arr = new ArrayList<Acquaintance>();
		JFrame window = new JFrame("Contact List");
		try{
			ObjectInputStream is;
			is = new ObjectInputStream(new FileInputStream("contacts"));
			arr = (ArrayList<Acquaintance>) is.readObject();
			is.close();
		}catch(Exception e){
			JOptionPane.showMessageDialog(window, "No saved records could be retrieved.");
		}
		
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.setSize(new Dimension(500, 500));
		ContactListGUI(window, arr);
		window.setVisible(true);
		
	}
}