package modelCheckCTL.util;

import modelCheckCTL.model.KripkeModel;

public abstract class FormulaNode extends SimpleNode {

	public FormulaNode(int i) {
		super(i);
	}

	public FormulaNode(eg1 p, int i) {
		super(p, i);
	}
	
	abstract public boolean check(KripkeModel.State state);
	abstract public void mark(KripkeModel model);
	abstract public String getMarking();
	
	public int getChildIndex(FormulaNode child)
	{
		for(int i=0; i<children.length; i++)
		{
			if(children[i] == child) return i;
		}
		
		return -1;
	}
	
	public void encapChild(FormulaNode cap, int child1, int child2)
	{
		cap.jjtSetParent(this);
		this.jjtGetChild(child1).jjtSetParent(cap);
		cap.jjtAddChild(this.jjtGetChild(child1), 0);
		this.jjtAddChild(cap, child1);
		
		if(child2 != -1)
		{
			cap.jjtAddChild(this.jjtGetChild(child2), 1);
			this.jjtGetChild(child1).jjtSetParent(cap);
		}
	}
	
	public void negateChild(int child)
	{
		ASTExpr node = new ASTExpr(eg1TreeConstants.JJTEXPR);
		node.setType(eg1Constants.NOT);
		encapChild(node,child,-1);
	}
	public void orChildWith(int child, FormulaNode n)
	{
		ASTExpr node = new ASTExpr(eg1TreeConstants.JJTEXPR);
		node.setType(eg1Constants.OR);
		node.jjtAddChild(n, 1);
		encapChild(node,child,-1);
	}
	
	@Override
	public void jjtAddChild(Node n, int i) {
	    super.jjtAddChild(n, i);
	    ((FormulaNode)n).added();
	}
	public void added()
	{
	}
	
	public FormulaNode clone()
	{
		return null;
	}
}
