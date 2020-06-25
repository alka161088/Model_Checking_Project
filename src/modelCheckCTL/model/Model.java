package modelCheckCTL.model;

import java.io.File;
import java.io.InputStream;

import modelCheckCTL.controller.Controller;
import modelCheckCTL.util.ASTFormula;
import modelCheckCTL.util.FormulaNode;
import modelCheckCTL.util.ParseException;
import modelCheckCTL.util.eg1;

public class Model {
	
	private Controller controller;
	
	private KripkeModel model;
	
	private ASTFormula ast;
	private boolean holds = false;
	private String error;
	
	public Model(Controller c)
	{
		controller = c;
		
		model = new KripkeModel();
	}
	
	public KripkeModel getKripkeModel()
	{
		return model;
	}
	public void loadModel(File file)
	{
		model.loadModel(file);
	}
	
	public boolean parseFormula(InputStream formula, String state)
	{
		eg1 parser = new eg1(formula);
		try {
			ast = parser.Formula();
		} catch (ParseException e) {
			error = e.getMessage();
			return false;
		}
		holds = model.checkFormula((FormulaNode)ast, state);
		
		return true;
	}
	
	public boolean holds()
	{
		return holds;
	}
	
	public String getError()
	{
		return error;
	}
}
