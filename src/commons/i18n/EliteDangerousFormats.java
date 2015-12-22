/**
 * 
 */
package commons.i18n;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Naeregwen
 *
 */
public class EliteDangerousFormats {

	public static DateFormat frenchDateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
	public static DateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
}
