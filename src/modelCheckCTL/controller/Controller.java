package modelCheckCTL.controller;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

import modelCheckCTL.model.Model;
import modelCheckCTL.view.View;

public class Controller {
	
	private Model model;
	private View view;
	private String ctFile;
	private String stateId;
	
	public Controller()
	{
		model = new Model(this);
		view = new View(this, model);
	}
	public void setModel(Model m)
	{
		model = m;
	}
	public void setView(View v)
	{
		view = v;
	}
	public void setCtFile(String ct)
	{
		ctFile = ct;
	}
	public void setStateId(String id)
	{
		stateId = id;
	}
	
	public static void main(String[] args) {
		Controller control = new Controller();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Please enter the location for the CTL formula: ");
		String ctlFile = scanner.nextLine();
		control.setCtFile(ctlFile);
		
		System.out.println("Please enter the state ID: ");
		String stateID = scanner.nextLine();
		control.setStateId(stateID);
		
		scanner.close();
		
	}
}
