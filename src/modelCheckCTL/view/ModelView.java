package modelCheckCTL.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import modelCheckCTL.model.KripkeModel;
import modelCheckCTL.model.KripkeModel.State;


public class ModelView extends JComponent {
	private KripkeModel model;
	final int stateWidth = 60;
	
	public ModelView()
	{
		setBorder(BorderFactory.createLineBorder(Color.black));
		model = null;
	}
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(500,400);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(Color.white);
	    g.fillRect(0, 0, this.getWidth(), this.getHeight());
	    	
	    if(model != null)
	    {
	    	Point[] points = new Point[model.getNumStates()];
	    	Graphics2D g2D = (Graphics2D) g;
	    	g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    	g2D.setStroke(new BasicStroke(3F));
	    	int x, y;
	    	for(int i=0; i<model.getNumStates(); i++)
	    	{
	    		x = (int)(((this.getWidth()-stateWidth)/2-stateWidth)*Math.sin(2*Math.PI*((float)i/model.getNumStates())))+this.getWidth()/2;
	    		y = (int)(((this.getHeight()-stateWidth)/2-stateWidth)*-Math.cos(2*Math.PI*((float)i/model.getNumStates())))+this.getHeight()/2;
	    		points[i] = new Point(x, y);
	    	}
	    	KripkeModel.State relations[];
	    	Point s1, s2;
	    	for(int i=0; i<model.getNumStates(); i++)
	    	{
	    		s1 = points[i];
	    		relations = model.getState(i).getRelationships();
	    		for(int j=0; j<relations.length; j++)
	    		{	
	    			if(relations[j] == model.getState(i))
	    			{
	    				this.drawRecursiveArrow(g, s1.x, s1.y, (float)(2*Math.PI*((float)i/model.getNumStates())));
	    			}
	    			else
	    			{
	    				s2 = points[model.getStateIndex(relations[j].getName())];
	    				this.drawArrow(g, s1.x, s1.y, s2.x, s2.y);
	    			}
	    		}
	    	}
	    	for(int i=0; i<model.getNumStates(); i++)
	    	{
	    		drawState(g, model.getState(i), points[i].x, points[i].y);
	    	}
	    	
	    }
	}
	private void drawState(Graphics g, KripkeModel.State state, int x, int y)
	{
		int x2 = x-stateWidth/2;
		int y2 = y-stateWidth/2;
		g.setColor(Color.BLUE);
		g.drawOval(x2, y2, stateWidth, stateWidth);
		g.setColor(Color.BLACK);
		g.drawString(state.getName(), x2+(stateWidth*2/3), y2);
		String atoms = state.getAtomString();
		g.drawString(atoms, x2+((stateWidth-g.getFontMetrics().stringWidth(atoms))/2), y2+(stateWidth/2));
	}
	private void drawArrow(Graphics g, int fromX, int fromY, int toX, int toY)
	{
		Graphics2D g2D = (Graphics2D)g;
		Stroke oldStroke = g2D.getStroke();
		g2D.setStroke(new BasicStroke(2L));
		g2D.setColor(Color.DARK_GRAY);
		
		int xLength = fromX-toX;
		int yLength = fromY-toY;
		int h = (int) Math.sqrt((fromX-toX)*(fromX-toX)+(fromY-toY)*(fromY-toY));
		int adjustedFromX = (int)(fromX-((float)(xLength)/h)*(stateWidth/2));
		int adjustedFromY = (int)(fromY-((float)(yLength)/h)*(stateWidth/2));
		int adjustedToX = (int)(toX+((float)(xLength)/h)*(stateWidth/2));
		int adjustedToY = (int)(toY+((float)(yLength)/h)*(stateWidth/2));
		//draw line
		g.drawLine(adjustedFromX, adjustedFromY, adjustedToX, adjustedToY);
		
		//draw arrow
		if(yLength>0)
			this.drawArrowPoint(g, (float)(-Math.acos((float)xLength/h)+Math.PI), adjustedToX, adjustedToY);
		else this.drawArrowPoint(g, (float)(Math.acos((float)xLength/h)+Math.PI), adjustedToX, adjustedToY);
		g2D.setStroke(oldStroke);
	}
	private void drawRecursiveArrow(Graphics g, int x, int y, float radian)
	{
		Graphics2D g2D = (Graphics2D)g;
		Stroke oldStroke = g2D.getStroke();
		g2D.setStroke(new BasicStroke(2L));
		g2D.setColor(Color.DARK_GRAY);
		
		int arcX = (int)(x-stateWidth/2);
		int arcY = (int)(y-stateWidth/2);
		int altX = (int)(Math.sin(radian)*(3*stateWidth/4));
		int altY = (int)(Math.cos(radian)*(3*stateWidth/4));
		arcX += altX;
		arcY -= altY;
		
		g.drawArc(arcX, arcY, stateWidth, stateWidth, (int)((7*Math.PI/4-radian)*180/Math.PI), 270);
		this.drawArrowPoint(g, (float)((5*Math.PI)/4-radian), x+(int)(Math.sin(radian+Math.PI/4)*stateWidth/2), y-(int)(Math.cos(radian+Math.PI/4)*stateWidth/2));
		
		g2D.setStroke(oldStroke);
	}
	private void drawArrowPoint(Graphics g, float radians, int x, int y)
	{
		final int arrowLength = 10;
		
		g.drawLine(x, y, (int)((float)Math.cos(radians+.707)*-arrowLength)+x, (int)((float)Math.sin(radians+.707)*arrowLength)+y);
		g.drawLine(x, y, (int)((float)Math.cos(radians-.707)*-arrowLength)+x, (int)((float)Math.sin(radians-.707)*arrowLength)+y);
	}
	
	public void setModel(KripkeModel m)
	{
		model = m;
		this.repaint();
	}
}
