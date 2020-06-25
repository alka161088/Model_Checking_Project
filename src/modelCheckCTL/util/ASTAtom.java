package modelCheckCTL.util;

import modelCheckCTL.model.KripkeModel;
import modelCheckCTL.model.KripkeModel.State;

public class ASTAtom extends FormulaNode {
	private String name;
	
	public ASTAtom(int i) {
		super(i);
	}

	public ASTAtom(eg1 p, int i) {
		super(p, i);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public void setName(String n)
	{
		name = n;
	}

	@Override
	public boolean check(State state) {
		return state.hasAtom(name);
	}

	@Override
	public void mark(KripkeModel model) {
	}

	@Override
	public String getMarking() {
		return name;
	}
	
	@Override
	public FormulaNode clone() {
		ASTAtom result = new ASTAtom(id);
		result.setName(name);
		return result;
	}
}
