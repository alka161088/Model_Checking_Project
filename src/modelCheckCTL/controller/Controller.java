package modelCheckCTL.controller;
import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import modelCheckCTL.model.Model;
import modelCheckCTL.util.CTLFormula;
import modelCheckCTL.util.KripkeStructure;
import modelCheckCTL.util.State;
import modelCheckCTL.view.View;

public class Controller {
	
	private Model model;
	private View view;
	private String ctFile;
	private String stateId;
	private String ctlFormula;
	private KripkeStructure _kripke;
	
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
	public void setCtlFormula(String formula){
		ctlFormula = formula;
	}
	
	public static void main(String[] args) throws IOException {
		Controller control = new Controller();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Please enter the location for the CTL formula: ");
		String ctlFile = scanner.nextLine();
		control.setCtFile(ctlFile);
		
		System.out.println("Please enter the state ID: ");
		String stateID = scanner.nextLine();
		control.setStateId(stateID);

		System.out.println("Please enter the CTL formula: ");
		String formula = scanner.nextLine();
		control.setCtlFormula(formula);

		Path fileNamePath = Path.of(ctlFile);
		String modelInputString = Files.readString(fileNamePath);
		KripkeStructure kripkeStructure = new KripkeStructure(modelInputString);

		State state = kripkeStructure.FindStateByName(stateID);
		CTLFormula ctlFormula = new CTLFormula(formula, state, kripkeStructure);
		boolean isSatisfy = ctlFormula.IsSatisfy();
		
		if(isSatisfy) {
			System.out.println();
			System.out.println("The model holds!");
		} else {
			System.out.println();
			System.out.println("The model does not hold.");
		}

		scanner.close();
	}
}
