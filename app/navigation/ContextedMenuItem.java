package navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

import play.mvc.Router;

/**
 * Wrapper for MenuItem and MenuContext
 * 
 * This class wraps a bare MenuItem and a MenuContext and has methods that are useful in your menu renderer
 */
public class ContextedMenuItem {
	public MenuItem menuItem;
	public MenuContext menuContext;
	
	private List<ContextedMenuItem> children;
	private List<ContextedMenuItem> visibleChildren;
	private Boolean hasActiveDescendant = null;
	
	public ContextedMenuItem(MenuItem menuItem, MenuContext menuContext) {
		if(menuItem == null) throw new NullPointerException("menuItem null");
		if(menuContext == null) throw new NullPointerException("menuContext null");
		this.menuItem = menuItem;
		this.menuContext = menuContext;
	}
	
	public boolean isActive() {
		return menuItem.action != null && menuContext.hasActiveAction(menuItem.action);
	}
	
	public boolean hasActiveDescendant() {
		if(hasActiveDescendant == null) {
			if(isActive()) {
				hasActiveDescendant = true;
			} else {
				for(ContextedMenuItem visibleChild : getVisibleChildren()) {
					if(visibleChild.hasActiveDescendant()) {
						hasActiveDescendant = true;
					}
				}
				if(hasActiveDescendant == null) {
					hasActiveDescendant = false;
				}
			}
		}
		return hasActiveDescendant;
	}
	
	public boolean isVisible() {
		if(menuItem.labels.isEmpty()) {
			return true;
		} else {
			return CollectionUtils.containsAny(menuItem.labels, menuContext.activeLabels);
		}
	}
	
	public List<ContextedMenuItem> getChildren() {
		if(children == null) {
			children = new ArrayList<ContextedMenuItem>(menuItem.children.size());
			for(MenuItem childMenuItem : menuItem.children) {
				children.add(new ContextedMenuItem(childMenuItem, menuContext));
			}
		}
		return children;
	}
	
	public List<ContextedMenuItem> getVisibleChildren() {
		if(visibleChildren == null) {
			visibleChildren = new ArrayList<ContextedMenuItem>();
			for(ContextedMenuItem menuItem : getChildren()) {
				if(menuItem.isVisible()) {
					visibleChildren.add(menuItem);
				}
			}
		}
		return visibleChildren;
	}
	
	public String getLink() {
		if(menuItem.url != null) {
			return menuItem.url;
		}
		if(menuItem.action == null) {
			return null;
		} else {
			return Router.reverse(menuItem.action, getSubstitutedParams()).url;
		}
	}
	
	public String getText() {
		return menuItem.text;
	}
	
	public boolean hasLink() {
		return menuItem.hasLink();
	}
	
	public Object getProperty(String propertyName) {
		return menuItem.properties.get(propertyName);
	}
	
	protected Map<String, Object> getSubstitutedParams() {
        Map<String, Object> substitutedParams = new HashMap<String, Object>();
        for(Entry<String, String> param : menuItem.params.entrySet()) {
        	String value = param.getValue();
        	for(Entry<String, Object> substitution : menuContext.substitutions.entrySet()) {
                value = value.replace(":" + substitution.getKey(), substitution.getValue().toString());
            }
        	substitutedParams.put(param.getKey(), value);
        }
        return substitutedParams;
    }
	
	@Override
	public String toString() {
		// TODO: Improve
		return "ContextedMenuItem " + menuItem.text; 
	}
}
