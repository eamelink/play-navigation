package navigation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * For future reference if this is broken: 
 * http://groups.google.com/group/play-framework/browse_thread/thread/09e9aeb8afdaf4fb?fwc=1
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD) 
public @interface ActiveNavigations {
	ActiveNavigation[] value() default {};
}
