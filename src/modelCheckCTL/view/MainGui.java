package modelCheckCTL.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;


import modelCheckCTL.model.KripkeModel;
import modelCheckCTL.model.Model;
import modelCheckCTL.util.ASTFormula;
import modelCheckCTL.util.FormulaNode;
import modelCheckCTL.util.eg1;


public class MainGui implements Runnable {
	private JLabel filenameLabel;
	private JLabel titleLabel;
	private JMenuItem closeModelItem;
	private JComboBox stateSelector;
	private JTextField formula;
	private JTextArea results;
	private ModelView modelView;
	private final JFileChooser loadDialog = new JFileChooser();
	private Model model;
	
	public MainGui(Model m)
	{
		model = m;
		FileFilter filter = new FileFilter(){
			public String getDescription()
			{
				return "Kripke Model (*.kripke)";
			}
			public boolean accept(File file)
			{
				if(file.isDirectory()) return true;
				Pattern pattern = Pattern.compile("^.*"+Pattern.quote(".")+"kripke$");
				Matcher matcher = pattern.matcher(file.getName());
				if(matcher.find()) return true;
				return false;
			}
		};
		loadDialog.addChoosableFileFilter(filter);
		loadDialog.setFileFilter(filter);
		
		javax.swing.SwingUtilities.invokeLater(this);
	}
	private void addMenu(final JFrame frame)
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		
		JMenuItem item = new JMenuItem("Load Kripke Model...");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int val = loadDialog.showOpenDialog(null);
				if(val == JFileChooser.APPROVE_OPTION)
				{
					loadModel(loadDialog.getSelectedFile());
				}
			}
		});
		menu.add(item);
		
		closeModelItem = new JMenuItem("Close Kripke Model");
		closeModelItem.setEnabled(false);
		closeModelItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				closeModel();
			}
		});
		menu.add(closeModelItem);
		
		menu.addSeparator();
		
		item = new JMenuItem("Exit");
		item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }

        });
		menu.add(item);
		
		//Help Menu
		menu = new JMenu("Help");
		menuBar.add(menu);
		item = new JMenuItem("Usage");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame,
					    "For this assignment I use a the following CTL syntax:\n\n" +
					    "phi ::= T | p | (!phi) | (phi && phi) | (phi || ph) | (phi -> phi)\n         | AXphi | EXphi | AFphi | EFphi | AGphi | EGphi | A[phiUphi] | E[phiUphi]\n\n" +
					    "Parentheses are strictly enforced and white space is ignored.",
					    "Usage",
					    JOptionPane.PLAIN_MESSAGE);
			}
		});
		menu.add(item);
		
		item = new JMenuItem("About");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame,
					    "Author: Trevor Hanz\n" +
					    "For: CS 5392\n" +
					    "Class: RRHEC",
					    "About",
					    JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(item);
		
		frame.setJMenuBar(menuBar);
	}
	private void addChecker(JPanel frame)
	{
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Formula"));
		panel.setLayout(new GridLayout(3,2));
		panel.setPreferredSize(new Dimension(200, 100));
		
		JLabel label = new JLabel("State: ");
		panel.add(label);
		
		stateSelector = new JComboBox();
		stateSelector.addItem("----");
		panel.add(stateSelector);
		
		label = new JLabel("Formula: ");
		panel.add(label);
		
		formula = new JTextField(10);
		panel.add(formula);
		
		JButton button = new JButton("check");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				checkModel();
			}
		});
		panel.add(button);
		
		JPanel panel2 = new JPanel();
		panel2.add(panel);
		panel2.setBorder(null);
		panel2.setLayout(new BoxLayout(panel2,BoxLayout.PAGE_AXIS));
		
		results = new JTextArea();
		results.setPreferredSize(new Dimension(300, 400));
		results.setBackground(panel.getBackground());
		results.setBorder(BorderFactory.createTitledBorder("Results"));
		results.setMargin(new Insets(5,5,5,5));
		results.setWrapStyleWord(true);
		results.setLineWrap(true);
		panel2.add(results);
		frame.add(panel2);
	}
	private void addModelView(JPanel frame)
	{
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Model"));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JPanel titleBox = new JPanel();
		titleBox.setLayout(new GridLayout(2,2));
		JLabel label = new JLabel("Loaded Model: ");
		titleBox.add(label);
		
		filenameLabel = new JLabel("none");
		titleBox.add(filenameLabel);
		
		label = new JLabel("Model Title: ");
		titleBox.add(label);
		
		titleLabel = new JLabel(model.getKripkeModel().getTitle());
		titleBox.add(titleLabel);
		panel.add(titleBox);
		
		modelView = new ModelView();
		panel.add(modelView);
		frame.add(panel);
	}
	
	public void run()
	{
		JFrame frame = new JFrame("th1382 - Model Checker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, 0));
		
		addMenu(frame);
		addChecker(panel);
		addModelView(panel);
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	private void loadModel(File file)
	{
		filenameLabel.setText(file.getName());
		closeModelItem.setEnabled(true);
		model.loadModel(file);
		titleLabel.setText(model.getKripkeModel().getTitle());
		stateSelector.removeAllItems();
		String[] states = model.getKripkeModel().getStates();
		for(int i=0; i<states.length; i++)
		{
			stateSelector.addItem(states[i]);
		}
		modelView.setModel(model.getKripkeModel());
		results.setText("");
		formula.setText("");
	}
	private void closeModel()
	{
		filenameLabel.setText("none");
		closeModelItem.setEnabled(false);
		model.getKripkeModel().clear();
		titleLabel.setText(model.getKripkeModel().getTitle());
		stateSelector.removeAllItems();
		stateSelector.addItem("----");
		modelView.setModel(null);
		results.setText("");
		formula.setText("");
	}
	private void checkModel()
	{
		if(stateSelector.getItemAt(0).toString().equals("----"))
		{
			results.setText("No Model Loaded.");
			return;
		}
		if(formula.getText().equals(""))
		{
			results.setText("No Formula Entered.");
			return;
		}
		try{
			InputStream stream = new ByteArrayInputStream(formula.getText().getBytes());
			results.setText("Checking "+stateSelector.getSelectedItem()+"|="+formula.getText()+" . . .\n");
			if(model.parseFormula(stream, stateSelector.getSelectedItem().toString()))
			{
				if(model.holds())
				{
					results.setText(results.getText()+"\n"+stateSelector.getSelectedItem()+"|="+formula.getText()+" holds!");
				}
				else
				{
					results.setText(results.getText()+"\n"+stateSelector.getSelectedItem()+"|="+formula.getText()+" does not hold.");
				}
			}
			else //did not parse
			{
				results.setText(results.getText()+"\n"+model.getError());
			}
		}catch(Exception e)
		{
			results.setText(e.getMessage());
		}
	}
}