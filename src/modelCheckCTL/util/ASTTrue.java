package modelCheckCTL.util;

import modelCheckCTL.model.KripkeModel;
import modelCheckCTL.model.KripkeModel.State;

public class ASTTrue extends FormulaNode {

	public ASTTrue(int i) {
		super(i);
	}

	public ASTTrue(eg1 p, int i) {
		super(p, i);
	}
	
	@Override
	public String toString()
	{
		return "T";
	}
	
	@Override
	public boolean check(State state) {
		return true;
	}

	@Override
	public void mark(KripkeModel model) {
	}

	@Override
	public String getMarking() {
		return "T";
	}

	@Override
	public FormulaNode clone() {
		return new ASTTrue(id);
	}
}
