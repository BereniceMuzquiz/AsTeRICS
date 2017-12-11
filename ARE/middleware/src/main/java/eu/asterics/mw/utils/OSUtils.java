/*
 *    AsTeRICS - Assistive Technology Rapid Integration and Construction Set
 *
 *
 *        d8888      88888888888       8888888b.  8888888 .d8888b.   .d8888b.
 *       d88888          888           888   Y88b   888  d88P  Y88b d88P  Y88b
 *      d88P888          888           888    888   888  888    888 Y88b.    
 *     d88P 888 .d8888b  888   .d88b.  888   d88P   888  888         "Y888b. 
 *    d88P  888 88K      888  d8P  Y8b 8888888P"    888  888            "Y88b.
 *   d88P   888 "Y8888b. 888  88888888 888 T88b     888  888    888       "888
 *  d8888888888      X88 888  Y8b.     888  T88b    888  Y88b  d88P Y88b  d88P
 * d88P     888  88888P' 888   "Y8888  888   T88b 8888888 "Y8888P"   "Y8888P"
 *
 *
 *                    homepage: http://www.asterics.org
 *
 *     This project has been partly funded by the European Commission,
 *                      Grant Agreement Number 247730
 *  
 *  
 *         Dual License: MIT or GPL v3.0 with "CLASSPATH" exception
 *         (please refer to the folder LICENSE)
 *
 */

package eu.asterics.mw.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;

import eu.asterics.mw.are.AREProperties;
import eu.asterics.mw.services.AstericsErrorHandling;

/**
 * Helper class to find OS ARE is running on.
 * 
 * @author Martin Deinhofer [deinhofe@technikum-wien.at] Date: May 28, 2015
 */
public class OSUtils {
    private static Logger logger = AstericsErrorHandling.instance.getLogger();
    public static final String LINUX = "linux";
    public static final String MACOSX = "macosx";
    public static final String WINDOWS = "windows";
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static final String ARE_OPEN_URL_CMD_KEY_PREFIX = "ARE.openURL.cmd.";

    static {
        AREProperties.instance.getProperty(ARE_OPEN_URL_CMD_KEY_PREFIX + LINUX, "sensible-browser", "Default Linux command to start a browser.");
        AREProperties.instance.getProperty(ARE_OPEN_URL_CMD_KEY_PREFIX + WINDOWS, "explorer", "Default Windows command to start a browser.");
        AREProperties.instance.getProperty(ARE_OPEN_URL_CMD_KEY_PREFIX + MACOSX, "open", "Default Mac OSX command to start a browser.");
    }

    public static enum OS_NAMES {
        ALL, WINDOWS, LINUX, MACOSX;
    }

    /**
     * Is the ARE running on any windows?
     * 
     * @return true: yes
     */
    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    /**
     * Is the ARE running on an Mac OS X?
     * 
     * @return true: yes
     */
    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    /**
     * Is the ARE runningn on Linux or Unix?
     * 
     * @return true: yes
     */
    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
    }

    /**
     * This is a synonym of {@link #isUnix()};
     * 
     * @return
     */
    public static boolean isLinux() {
        return isUnix();
    }

    /**
     * Returns the operating system name according to AsTeRICS convention.
     * 
     * @return
     */
    public static String getOsName() {
        if (isWindows()) {
            return WINDOWS;
        } else if (isMac()) {
            return MACOSX;
        } else if (isUnix()) {
            return LINUX;
        }
        return "unknown";
    }

    /**
     * Starts the given application using its applicationPath, arguments and workingDirectory but only if the current platform equals to executeOnPlatform or if
     * executeOnPlatform equals to {@link OS_NAMES#ALL}.
     * 
     * @param applicationPath
     * @param arguments
     * @param workingDirectory
     * @param executeOnPlatform
     * @return : The Process oject of the started application.
     * @throws IOException
     */
    public static Process startApplication(String applicationPath, String arguments, String workingDirectory, OS_NAMES executeOnPlatform) throws IOException {
        return startApplication(applicationPath + " " + arguments, workingDirectory, executeOnPlatform);
    }

    /**
     * Starts the given application using applicationPathAndArguments and the given workingDirectory but only if the current platform equals to
     * executeOnPlatform or if executeOnPlatform equals to {@link OS_NAMES#ALL}.
     * 
     * @param applicationPathAndArguments
     * @param workingDirectory
     * @param executeOnPlatform
     * @return
     * @throws IOException
     */
    public static Process startApplication(String applicationPathAndArguments, String workingDirectory, OS_NAMES executeOnPlatform) throws IOException {
        if (executeOnPlatform != null && getOsName().equalsIgnoreCase(executeOnPlatform.toString()) || executeOnPlatform.equals(OS_NAMES.ALL)) {
            List<String> command = new ArrayList<String>();

            StringTokenizer st = new StringTokenizer(applicationPathAndArguments);
            while (st.hasMoreTokens()) {
                String act = st.nextToken();
                command.add(act);
                logger.fine("adding argument: " + act);
            }
            if (command.size() == 0) {
                return null;
            }
            // The command can be an absolute path, so ensure that it has proper system separators (\ or \\ or /)
            command.set(0, FilenameUtils.separatorsToSystem(command.get(0)));

            ProcessBuilder builder = new ProcessBuilder(command);
            if (workingDirectory != null && !"".equals(workingDirectory)) {
                workingDirectory = FilenameUtils.separatorsToSystem(workingDirectory);
                logger.fine("Setting workingDirectory to: " + workingDirectory);
                builder.directory(new File(workingDirectory));
            }

            logger.fine("Starting command: " + command);
            return builder.start();
        }
        return null;
    }

    /**
     * Opens the given URL with the pre-configured browser start command for the current platform but only if the current platform equals to executeOnPlatform
     * or if executeOnPlatform equals to {@link OS_NAMES#ALL}.
     * 
     * @param urlToOpen
     * @param executeOnPlatform
     * @return
     * @throws IOException
     */
    public static Process openURL(String urlToOpen, OS_NAMES executeOnPlatform) throws IOException {
        if (executeOnPlatform != null && getOsName().equalsIgnoreCase(executeOnPlatform.toString()) || executeOnPlatform.equals(OS_NAMES.ALL)) {
            String browserStartCmd = AREProperties.instance.getProperty(ARE_OPEN_URL_CMD_KEY_PREFIX + getOsName());
            return startApplication(browserStartCmd, urlToOpen, null, executeOnPlatform);
        }
        return null;
    }

}
