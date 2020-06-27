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
//	
	public void run()
	{
//		JFrame frame = new JFrame("th1382 - Model Checker");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setLocationRelativeTo(null);
//		frame.setResizable(false);
//		JPanel panel = new JPanel();
//		panel.setLayout(new BoxLayout(panel, 0));
//		
//		addMenu(frame);
//		addChecker(panel);
//		addModelView(panel);
//		
//		frame.add(panel);
//		frame.pack();
//		frame.setVisible(true);
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