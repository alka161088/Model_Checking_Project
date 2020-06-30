package modelCheckCTL.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import modelCheckCTL.util.CTLFormula;
import modelCheckCTL.model.KripkeStructure;
import modelCheckCTL.model.State;
import modelCheckCTL.view.View;

public class Controller {
	
	//private Model model;
	private View view;

	private String ctFile;
	private String stateId;
	private String ctlFormula;
	private KripkeStructure _kripke;
	
	public Controller()
	{
//		model = new Model(this);
//		view = new View(this, model);
		view = new View(this);
	}
//	public void setModel(Model m)
//	{
//		model = m;
//	}
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
		if(ctlFile.matches("model(.*)"))
		{
			control.setCtFile(ctlFile);
		}
		else
		{
			System.out.println("Input Format Not Valid\n");
			System.exit(0);

		}
		System.out.println("Please enter the state ID: ");
		String stateID = scanner.nextLine();
		if(stateID.matches("s[0-10]"))
		{
			control.setStateId(stateID);
		}
		else
		{
			System.out.println("Input Format Not Valid: Right usage s0 - s10\n");
			System.exit(0);

		}

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
			System.out.println(String.format("Property %s holds in state %s!", formula, stateID));
		} else {
			System.out.println();
			System.out.println(String.format("Property %s does not hold in state %s!", formula, stateID));
		}

		scanner.close();
	}
}
