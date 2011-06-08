package navigation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MenuItem {
	public MenuItem parent;
	public String text;
	public String action;
	public String url;
	
	public List<MenuItem> children = new ArrayList<MenuItem>();
	public Set<String> labels = new HashSet<String>();
	public Map<String, String> params = new HashMap<String, String>();
	public Map<String, Object> properties = new HashMap<String, Object>();
	public boolean hasLink() {
		return url != null || action != null;
	}
}