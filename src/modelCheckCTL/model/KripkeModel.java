package modelCheckCTL.model;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelCheckCTL.util.FormulaNode;


public class KripkeModel {
	private String title;
	private State[] states;
	
	public class State {
		private String name;
		private LinkedList<String> atoms;
		private LinkedList<State> relations;
		private LinkedList<String> marks;
		
		public State() {
			name = "n/a";
			atoms = new LinkedList<String>();
			relations = new LinkedList<State>();
			marks = new LinkedList<String>();
		}
		public State(String n) {
			name = n;
			atoms = new LinkedList<String>();
			relations = new LinkedList<State>();
			marks = new LinkedList<String>();
		}
		public String getName() {return name;}
		public String[] getAtoms()
		{
			String[] result = new String[atoms.size()];
			for(int i=0; i<atoms.size(); i++)
			{
				result[i] = atoms.get(i);
			}
			return result;
		}
		public String getAtomString()
		{
			String result = new String();
			if(!atoms.isEmpty())
			{
				result += atoms.getFirst();
				for(int i=1; i<atoms.size(); i++)
				{
					result += ","+atoms.get(i);
				}
			}
			return result;
		}
		public State[] getRelationships()
		{
			State[] result = new State[relations.size()];
			for(int i=0; i<relations.size(); i++)
			{
				result[i] = relations.get(i);
			}
			return result;
		}
		
		public void addRelationship(State s)
		{
			relations.add(s);
		}
		public void addAtom(String atom)
		{
			atoms.add(atom);
		}
		public boolean hasAtom(String atom)
		{
			if(atoms.indexOf(atom) >= 0) return true;
			else return false;
		}
		public void mark(String mark)
		{
			if(!isMarked(mark))
				marks.add(mark);
		}
		public boolean isMarked(String mark)
		{
			if(marks.indexOf(mark) >= 0) return true;
			else return false;
		}
		public String toMarkings()
		{
			return marks.toString();
		}
	}
	
	public KripkeModel()
	{
		title = "n/a";
	}
	
	public int loadModel(File file)
	{
		FileInputStream stream;
		try {
			stream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		try {
			title = reader.readLine();
			
			//Find states
			LinkedList<String> stateList = new LinkedList<String>();
			String line = reader.readLine();
			Pattern pattern = Pattern.compile(Pattern.quote("{")+"([a-zA-Z0-9_-]+)");
			Matcher matcher = pattern.matcher(line);
			
			if(matcher.find()) stateList.add(matcher.group(1));
			else return 0;
			
			pattern = Pattern.compile(",([a-zA-Z0-9_-]+)");
			matcher.usePattern(pattern);
			while(matcher.find())
			{
				stateList.add(matcher.group(1));
			}
			states = new State[stateList.size()];
			for(int i=0; i<stateList.size(); i++)
			{
				states[i] = new State(stateList.get(i));
			}
			
			//Find relations
			line = reader.readLine();
			pattern = Pattern.compile(Pattern.quote("{(")+"([a-zA-Z0-9_-]+),([a-zA-Z0-9_-]+)"+Pattern.quote(")"));
			matcher = pattern.matcher(line);
			if(matcher.find()) addRelationship(matcher.group(1), matcher.group(2));
			
			pattern = Pattern.compile(Pattern.quote("(")+"([a-zA-Z0-9_-]+),([a-zA-Z0-9_-]+)"+Pattern.quote(")"));
			matcher.usePattern(pattern);
			while(matcher.find())
			{
				addRelationship(matcher.group(1), matcher.group(2));
			}
			
			//Find atoms
			line = reader.readLine();
			pattern = Pattern.compile(Pattern.quote("{(")+"|"+Pattern.quote("),(")+"|"+Pattern.quote(")}"));
			String[] atoms = pattern.split(line);
			pattern = Pattern.compile("[,]?([a-z][a-z0-9]*)");
			for(int i=1; i<atoms.length; i++)
			{
				matcher = pattern.matcher(atoms[i]);
				while(matcher.find())
				{
					states[i-1].addAtom(matcher.group(1));
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		
		return 1;
	}
	private void addRelationship(String s1, String s2)
	{
		int S1, S2;
		S1=this.getStateIndex(s1);
		S2=this.getStateIndex(s2);
		if(S1 != -1 && S2 != -1)
		{
			this.getState(S1).addRelationship(this.getState(S2));
		}
	}
	
	public void clear()
	{
		title = "n/a";
	}
	public String getTitle()
	{
		return title;
	}
	public String[] getStates()
	{
		String[] result = new String[states.length];
		for(int i = 0; i<states.length; i++)
		{
			result[i] = states[i].getName();
		}
		return result;
	}
	public State getState(int i)
	{
		return states[i];
	}
	public int getNumStates()
	{
		return states.length;
	}
	public int getStateIndex(String name)
	{
		for(int i = 0; i<states.length; i++)
		{
			if(states[i].getName().equals(name)) return i;
		}
		return -1;
	}
	public boolean checkFormula(FormulaNode formula, String state)
	{
		mark(formula);
		return formula.check(getState(getStateIndex(state)));
	}
	private void mark(FormulaNode formula)
	{
		for(int i=0; i<formula.jjtGetNumChildren(); i++)
		{
			mark((FormulaNode)formula.jjtGetChild(i));
		}
		formula.mark(this);
	}
	public void printMarkings()
	{
		for(int i=0; i<states.length; i++)
		{
			System.out.println(states[i].getName()+": "+states[i].toMarkings());
		}
	}
}
