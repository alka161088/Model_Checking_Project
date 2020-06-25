package modelCheckCTL.util;

import modelCheckCTL.model.KripkeModel;
import modelCheckCTL.model.KripkeModel.State;

public class ASTConnective extends FormulaNode {
	private final int EX = 0;
	private final int EU = 1;
	private final int AF = 2;
	private int type;
	private boolean negate = false;
	private boolean ored = false;
	private FormulaNode orWith;
	private final String[] typeNames = {"EX","EU","AF"};
	
	public ASTConnective(int i) {
		super(i);
		type = EX;
	}

	public ASTConnective(eg1 p, int i) {
		super(p, i);
		type = EX;
	}
	
	@Override
	public String toString()
	{
		return typeNames[type];
	}
	
	public void setType(int path, int temporal)
	{
		if(path == eg1Constants.ALL)
		{
			switch(temporal)
			{
			case eg1Constants.NEXT:
				type = EX;
				negateSelf();
				negateChild(0);
				break;
			case eg1Constants.UNTIL:
				type = EU;
				negateChild(0);
				negateChild(1);
				
				ASTExpr andNode = new ASTExpr(eg1TreeConstants.JJTEXPR);
				andNode.setType(eg1Constants.AND);
				andNode.jjtAddChild(this.jjtGetChild(0), 0);
				this.jjtAddChild(this.jjtGetChild(1), 0);
				andNode.jjtAddChild(((FormulaNode)this.jjtGetChild(0)).clone(), 1);
				this.jjtAddChild(andNode, 1);
				
				ASTExpr notNode = new ASTExpr(eg1TreeConstants.JJTEXPR);
				notNode.setType(eg1Constants.NOT);
				ASTConnective addition = new ASTConnective(id);
				addition.type = AF;
				addition.jjtAddChild((Node) ((FormulaNode)((FormulaNode)this.jjtGetChild(0)).jjtGetChild(0)).clone(), 0);
				notNode.jjtAddChild(addition, 0);
				orWith(notNode);
				negateSelf();
				break;
			case eg1Constants.FUTURE:
				type = AF;
				break;
			case eg1Constants.GLOBALY:
				type = EU;
				negateSelf();
				negateChild(0);
				this.jjtAddChild(this.jjtGetChild(0), 1);
				this.jjtAddChild(new ASTTrue(0), 0);
			};
			
		}
		else //E...
		{
			switch(temporal)
			{
			case eg1Constants.NEXT:
				type = EX;
				break;
			case eg1Constants.UNTIL:
				type = EU;
				break;
			case eg1Constants.FUTURE:
				type = EU;
				this.jjtAddChild(this.jjtGetChild(0), 1);
				this.jjtAddChild(new ASTTrue(0), 0);
				break;
			case eg1Constants.GLOBALY:
				type = AF;
				negateSelf();
				negateChild(0);
			};
		}
	}

	@Override
	public boolean check(State state) {
		return state.isMarked(getMarking());
	}

	@Override
	public void mark(KripkeModel model) {
		String marking = getMarking();
		boolean changed;
		switch(type)
		{
		case EX:
			for(int i=0; i<model.getNumStates(); i++)
			{
				if(preCheckE(model.getState(i), (FormulaNode)jjtGetChild(0)) && !model.getState(i).isMarked(marking))
				{
					model.getState(i).mark(marking);
				}
			}
			break;
		case EU:
			for(int i=0; i<model.getNumStates(); i++)
			{
				if(((FormulaNode)jjtGetChild(1)).check(model.getState(i))) model.getState(i).mark(marking);
			}
			changed = true;
			while(changed)
			{
				changed = false;
				for(int i=0; i<model.getNumStates(); i++)
				{
					if((preCheckE(model.getState(i), this) && ((FormulaNode)jjtGetChild(0)).check(model.getState(i)))
							&& !model.getState(i).isMarked(marking))
					{
						model.getState(i).mark(marking);
						changed = true;
					}
				}
			}
			break;
		case AF:
			for(int i=0; i<model.getNumStates(); i++)
			{
				if(preCheckA(model.getState(i), (FormulaNode)jjtGetChild(0)) || 
						(model.getState(i).getRelationships().length==0 && ((FormulaNode)jjtGetChild(0)).check(model.getState(i))))
						{
							model.getState(i).mark(marking);
						}
			}
			
			changed = true;
			while(changed)
			{
				changed = false;
				for(int i=0; i<model.getNumStates(); i++)
				{
					if(preCheckA(model.getState(i), this) && !model.getState(i).isMarked(marking))
					{
						System.out.println(model.getState(i).getName()+": "+preCheckA(model.getState(i), this)+", "+((FormulaNode)jjtGetChild(0)).check(model.getState(i)));
						model.getState(i).mark(marking);
						changed = true;
					}
				}
			}
			break;
		}
	}
	
	private boolean preCheckE(State state, FormulaNode phi)
	{
		State[] relations = state.getRelationships();
		for(int i=0; i<relations.length; i++)
		{
			if(phi.check(relations[i])) return true;
		}
		return false;
	}
	
	private boolean preCheckA(State state, FormulaNode phi)
	{
		State[] relations = state.getRelationships();
		if(relations.length == 0) return false;
		for(int i=0; i<relations.length; i++)
		{
			if(!phi.check(relations[i]) && state != relations[i]) return false;
		}
		return true;
	}
	
	@Override
	public String getMarking() {
		switch(type)
		{
		case EX:
			return "EX"+((FormulaNode)jjtGetChild(0)).getMarking();
		case EU:
			return "E["+((FormulaNode)jjtGetChild(0)).getMarking()+"U"+((FormulaNode)jjtGetChild(1)).getMarking()+"]";
		case AF:
			return "AF"+((FormulaNode)jjtGetChild(0)).getMarking();	
		}
		return "";
	}
	
	@Override
	public void added()
	{
		if(negate)
		{
			negate = false;
			((FormulaNode)this.jjtGetParent()).negateChild(0);
		}
		if(ored)
		{
			ored = false;
			((FormulaNode)this.jjtGetParent()).orChildWith(0, orWith);
		}
	}

	private void negateSelf()
	{
		negate = true;
	}
	private void orWith(FormulaNode n)
	{
		orWith = n;
		ored = true;
	}
	
	@Override
	public FormulaNode clone() {
		ASTConnective result = new ASTConnective(id);
		result.type = this.type;
		
		for(int i=0; i<this.jjtGetNumChildren(); i++)
		{
			result.jjtAddChild(((FormulaNode)this.jjtGetChild(i)).clone(), i);
		}
		
		return result;
	}
}
