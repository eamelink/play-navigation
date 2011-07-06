package navigation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.Yaml;

import play.mvc.Http.Request;
import play.vfs.VirtualFile;

/**
 * Keeper of the bare MenuItems 
 *
 * This class holds a static reference to all the MenuItems. You can retrive a Menu from this class, 
 * which will be automatically wrapped in a ContextedMenuItem, ready to be inserted into your view.
 */
public class Navigation {

	private static Map<String, MenuItem> namedMenuItems;
	private static ThreadLocal<MenuContext> menuContext = new ThreadLocal<MenuContext>();

	public static void init(VirtualFile file) {
		namedMenuItems = new HashMap<String, MenuItem>();
		loadFile(file);
	}
	
	public static void loadFile(VirtualFile file) {
		Yaml yaml = new Yaml();
		Object o = yaml.load(file.inputstream());
		
		if (o instanceof LinkedHashMap<?, ?>) {
			Pattern keyPattern = Pattern.compile("([^(]+)\\(([^)]+)\\)");
	
			LinkedHashMap<Object, Map<?, ?>> objects = (LinkedHashMap<Object, Map<?, ?>>) o;
			for (Object key : objects.keySet()) {
				Matcher matcher = keyPattern.matcher(key.toString().trim());
				if (matcher.matches()) {
					String type = matcher.group(1);
					String name = matcher.group(2);
					if (!type.equals("MenuItem")) {
						throw new RuntimeException("Navigation file contains invalid type " + type);
					}
					
					if(namedMenuItems.containsKey(name)) {
						throw new RuntimeException("Navigation file contains a duplicate navigation item with name " + name);
					}
					
					MenuItem entry = new MenuItem();
					namedMenuItems.put(name, entry);
					
					Map fields = objects.get(key);
					
					if (fields.containsKey("text")) {
						entry.text = (String) fields.get("text");
					}
					if (fields.containsKey("action")) {
						entry.action = (String) fields.get("action");
					}
					if (fields.containsKey("url")) {
						entry.url = (String) fields.get("url");
					}
					if (fields.containsKey("params")) {
						entry.params = (Map<String, String>) fields.get("params");
					}
					if (fields.containsKey("properties")) {
						entry.properties = (Map<String, Object>) fields.get("properties");
					}
					if (fields.containsKey("labels")) {
						entry.labels = new HashSet<String>((Collection<String>) fields.get("labels"));
					}
					if (fields.containsKey("parent")) {
						MenuItem parent = namedMenuItems.get(fields.get("parent"));
						if(parent == null) {
							throw new RuntimeException("Navigation file references a non existing parent " + fields.get("parent"));
						}
						entry.parent = parent;
						parent.children.add(entry);
					}
					
				}
			}
		}
	}
	
	public class NavigationYamlFile {
		public List<MenuItem> menuItems;
	}
	
	public static ContextedMenuItem getMenu(String name) {
		if(!namedMenuItems.containsKey(name)) {
			throw new IllegalArgumentException("Menu '" + name + "' not defined.");
		}
		return new ContextedMenuItem(namedMenuItems.get(name), getMenuContext());
	}
	
	public static void clearMenuContext() {
		menuContext.set(null);
	}
	
	public static MenuContext getMenuContext() {
		MenuContext context = menuContext.get(); 
		if(context == null) {
			context = buildMenuContext();
			menuContext.set(context);
		}
		return context;
	}
	
	protected static MenuContext buildMenuContext() {
		MenuContext menuContext = new MenuContext(Request.current());
		return menuContext;
	}
}