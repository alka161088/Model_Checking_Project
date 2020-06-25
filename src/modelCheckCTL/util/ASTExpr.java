package modelCheckCTL.util;

import modelCheckCTL.model.KripkeModel;
import modelCheckCTL.model.KripkeModel.State;

public class ASTExpr extends FormulaNode {

	public ASTExpr(int i) {
		super(i);
	}

	public ASTExpr(eg1 p, int i) {
		super(p, i);
	}
	
	@Override
	public String toString()
	{
		switch((Integer)value)
		{
			case eg1Constants.NOT:
				return "Not";
			case eg1Constants.AND:
				return "And";
			case eg1Constants.OR:
				return "Or";
			case eg1Constants.THEN:
				return "Then";
		}
		return eg1TreeConstants.jjtNodeName[id];
	}
	
	public void setType(int type)
	{
		
		if(type == eg1Constants.THEN)
		{
			SimpleNode n = new ASTExpr(id);
			n.jjtSetValue(eg1Constants.NOT);
			n.jjtAddChild(children[0], 0);
			children[0].jjtSetParent(n);
			n.jjtSetParent(this);
			this.jjtAddChild(n, 0);
			jjtSetValue(eg1Constants.OR);
		}
		else
		{
			jjtSetValue(type);
		}
	}

	@Override
	public boolean check(State state) {
		switch((Integer)value)
		{
		case eg1Constants.NOT:
			return !((FormulaNode)jjtGetChild(0)).check(state);
		case eg1Constants.AND:
			return ((FormulaNode)jjtGetChild(0)).check(state) && ((FormulaNode)jjtGetChild(1)).check(state);
		case eg1Constants.OR:
			return ((FormulaNode)jjtGetChild(0)).check(state) || ((FormulaNode)jjtGetChild(1)).check(state);
		}
		return false;
	}
	
	@Override
	public void mark(KripkeModel model) {
	}
	
	@Override
	public String getMarking()
	{
		switch((Integer)value)
		{
		case eg1Constants.NOT:
			return "(!"+((FormulaNode)jjtGetChild(0)).getMarking()+")";
		case eg1Constants.AND:
			return "("+((FormulaNode)jjtGetChild(0)).getMarking()+"&&"+((FormulaNode)jjtGetChild(1)).getMarking()+")";
		case eg1Constants.OR:
			return "("+((FormulaNode)jjtGetChild(0)).getMarking()+"||"+((FormulaNode)jjtGetChild(1)).getMarking()+")";
		}
		return "";
	}

	@Override
	public FormulaNode clone() {
		ASTExpr result = new ASTExpr(id);
		result.setType((Integer) this.jjtGetValue());
		for(int i=0; i<this.jjtGetNumChildren(); i++)
		{
			result.jjtAddChild(((FormulaNode)this.jjtGetChild(i)).clone(), i);
		}
		
		return result;
	}
}
