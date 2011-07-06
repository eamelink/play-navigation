package controllers;

import org.apache.commons.lang.StringUtils;

import navigation.Navigation;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * Have a menu automatically injected in your renderArgs
 * 
 * This MenuInjector has an @Before annotated action that reads comma separated menu names from the navigation.defaultMenus 
 * configuration parameter in your conf/application.conf file, and injects those menus into your renderArgs.
 */
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