package navigation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import play.mvc.Http.Request;

public class MenuContext {
	
	public Set<String> activeActions = new HashSet<String>();
	public Set<String> activeLabels = new HashSet<String>();
	public Map<String, Object> substitutions = new HashMap<String, Object>();
	
	public MenuContext(Request request) {
		addActiveAction(request.action);
		
	}
	
	protected void addActiveAction(String action) {
		activeActions.add(action);
	}
	
	protected void addActiveLabel(String label) {
		activeLabels.add(label);
	}
	
	protected void setActiveActions(Collection<String> actions) {
		activeActions.clear();
		activeActions.addAll(actions);
	}
	
	public boolean hasActiveAction(String action) {
		return activeActions.contains(action);
	}
	
}
