package tennis.managers;

import java.util.Date;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Manages the log messages for information, debugging and errors.
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class Log {
	private static final String TAG = "LOG";
	private static FileHandle logFile;
	private static final String DEBUG_S = "DEBUG";
	private static final String INFO_S = "INFO";
	private static final String ERROR_S = "ERROR";
	private static final String serverLogFileName = "tennis.log";

	/**
	 * Initialize Log Service
	 */
	public static void init() {
		logFile = Gdx.files.local(serverLogFileName);
	}

	/**
	 * Displays a debug message in console and write to file
	 * 
	 * @param msg
	 *            Message to be displayed
	 */
	public static void debug(String msg) {
		Gdx.app.debug(DEBUG_S + " " + TAG, msg);
		if (Gdx.app.getLogLevel() <= DEBUG)
			writeToFile(DEBUG_S, TAG, msg);
	}

	/**
	 * Displays an info message in console and write to file
	 * 
	 * @param msg
	 *            Message to be displayed
	 */
	public static void info(String msg) {
		Gdx.app.log(INFO_S + " " + TAG, msg);
		if (Gdx.app.getLogLevel() <= INFO)
			writeToFile(INFO_S, TAG, msg);
	}

	/**
	 * Displays an error message in console and write to file
	 * 
	 * @param msg
	 *            Message to be displayed
	 */
	public static void error(String msg) {
		Gdx.app.log(ERROR_S + " " + TAG, msg);
		if (Gdx.app.getLogLevel() <= ERROR)
			writeToFile(ERROR_S, TAG, msg);
	}

	public static void setLevel(int level) {
		Gdx.app.setLogLevel(level);
	}

	private static StringBuilder sb = new StringBuilder();

	@SuppressWarnings("deprecation")
	private static void writeToFile(String level, String tag, String msg) {
		if (logFile != null) {
			sb.delete(0, sb.length());
			sb.append(new Date().toLocaleString());
			sb.append(" [").append(level).append("] ").append(tag).append(": ")
					.append(msg).append("\n");
			try {
				logFile.writeString(sb.toString(), true);
			} catch (Exception e) {
				logFile = null;
				Log.error(e.toString());
				throw new GdxRuntimeException(e);
			}
		}
	}

	public static final int DEBUG = Application.LOG_DEBUG;
	public static final int INFO = Application.LOG_INFO;
	public static final int ERROR = Application.LOG_ERROR;
}
