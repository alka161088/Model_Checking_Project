package modelCheckCTL.controller;

import java.io.File;
import java.io.InputStream;

import modelCheckCTL.model.Model;
import modelCheckCTL.view.View;

public class Controller {
	
	private Model model;
	private View view;
	
	public Controller()
	{
		model = new Model(this);
		view = new View(this, model);
	}
	public void setModel(Model m)
	{
		model = m;
	}
	public void setView(View v)
	{
		view = v;
	}
	
	public static void main(String[] args) {
		Controller control = new Controller();
		
	}
}
