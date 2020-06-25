package modelCheckCTL.view;

import modelCheckCTL.controller.Controller;
import modelCheckCTL.model.Model;

public class View {
	private Controller controller;
	private Model model;
	private MainGui gui;
	
	public View(Controller c, Model m)
	{
		controller = c;
		model = m;
		gui = new MainGui(m);
	}
}
