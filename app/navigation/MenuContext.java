package navigation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import navigation.annotations.ActiveNavigation;
import navigation.annotations.ActiveNavigations;

import play.mvc.Http.Request;

/**
 * Provide context for a MenuItem
 * 
 * a MenuContext holds a list of active actions, active labels and substitutions, so that the renderer
 * knows which MenuItem is active and which should be visible.
 */
public class MenuContext {
	
	public Set<String> activeActions = new HashSet<String>();
	public Set<String> activeLabels = new HashSet<String>();
	public Map<String, Object> substitutions = new HashMap<String, Object>();
	
	public MenuContext(Request request) {
		addActiveAction(request.action);
		addActiveAnnotatedActions(request);
	}
	
	public void addActiveAction(String action) {
		activeActions.add(action);
	}
	
	public void setActiveAction(String action) {
	    activeActions.clear();
	    addActiveAction(action);
	}
	
    public void setActiveActions(Collection<String> actions) {
        activeActions.clear();
        activeActions.addAll(actions);
    }
    
    public boolean hasActiveAction(String action) {
        return activeActions.contains(action);
    }
	
	protected void addActiveAnnotatedActions(Request request) {
	    if(request.invokedMethod == null) return;
	    ActiveNavigation single = request.invokedMethod.getAnnotation(ActiveNavigation.class);
	    if(single != null) {
	        addActiveAction(single.value());
	    }
	    ActiveNavigations multiple = request.invokedMethod.getAnnotation(ActiveNavigations.class);
	    if(multiple != null) {
	        for(ActiveNavigation activeNavigation : multiple.value()) {
	            addActiveAction(activeNavigation.value());
	        }
	    }
	}
	
	public void addActiveLabel(String label) {
		activeLabels.add(label);
	}
	
	public void setActiveLabel(String label) {
	    activeLabels.clear();
	    addActiveLabel(label);
	}
	
	public void setActiveLabels(Collection<String> labels) {
	    activeLabels.clear();
	    activeLabels.addAll(labels);
	}
	
}