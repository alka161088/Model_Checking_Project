package modelCheckCTL.util;

import modelCheckCTL.model.KripkeModel;
import modelCheckCTL.model.KripkeModel.State;

public class ASTFormula extends FormulaNode {

	public ASTFormula(int i) {
		super(i);
	}

	public ASTFormula(eg1 p, int i) {
		super(p, i);
	}
	
	@Override
	public boolean check(State state) {
		return ((FormulaNode)children[0]).check(state);
	}

	@Override
	public void mark(KripkeModel model) {
	}

	@Override
	public String getMarking() {
		return ((FormulaNode)jjtGetChild(0)).getMarking();
	}
}
