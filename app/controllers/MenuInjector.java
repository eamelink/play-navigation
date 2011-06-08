package controllers;

import org.apache.commons.lang.StringUtils;

import navigation.Navigation;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

public class MenuInjector extends Controller {
	@Before
	public static void injectDefaultMenus() {
		for(String menuName : Play.configuration.getProperty("navigation.defaultMenus", "main").toString().split(",")) {
			if(!StringUtils.isBlank(menuName)) {
				renderArgs.put(menuName + "Menu", Navigation.getMenu(StringUtils.trim(menuName)));
			}
		}
	}
}