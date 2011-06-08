package controllers;

import navigation.Navigation;
import navigation.annotations.ActiveNavigation;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(MenuInjector.class)
public class Articles extends Controller {
	
	@Before
	public static void setArticleOfTheDayId() {
		Navigation.getMenuContext().substitutions.put("id", "123");	
	}
	
	public static void index() { render(); }
	public static void search() { render(); }
	
	@ActiveNavigation("Articles.search")
	public static void searchResults() { render(); }
	
	public static void show() { render(); }
}
