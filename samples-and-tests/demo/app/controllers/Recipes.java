package controllers;

import play.mvc.Controller;
import play.mvc.With;

@With(MenuInjector.class)
public class Recipes extends Controller {
	public static void index() { render(); }
	public static void pies() { render(); }
	public static void fruitpies() { render(); }
	public static void chocolatepies() { render(); }
}
