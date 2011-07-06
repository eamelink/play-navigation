package navigation;

import java.util.LinkedList;
import java.util.List;

import play.Logger;
import play.Play;
import play.Play.Mode;
import play.PlayPlugin;
import play.vfs.VirtualFile;

/**
 * Initialize and reload the navigation structure.
 */
public class NavigationPlugin extends PlayPlugin {
	
	// Timestamp the navigation was last loaded
	long lastLoaded = -1;
	List<VirtualFile> navigationFiles;

	@Override
	public void onConfigurationRead() {
		navigationFiles = new LinkedList<VirtualFile>();
		navigationFiles.add(VirtualFile.fromRelativePath("conf/navigation.yml"));
		detectChange();
	}
	
	@Override
	public void detectChange() {
        if (Play.mode == Mode.PROD && lastLoaded > 0) {
            return;
        }
        
        boolean reload = false;
        for(VirtualFile navigationFile : navigationFiles) {
        	if(navigationFile.lastModified() > lastLoaded) {
        		lastLoaded = navigationFile.lastModified();
        		reload = true;
        	}
        }
        
        if(reload) {
        	Logger.info("Reloading navigation file");
        	// TODO: Support multiple files
        	Navigation.init(navigationFiles.get(0));
        }
    }
	
	@Override
	public void beforeInvocation() {
		Navigation.clearMenuContext();
	}
}