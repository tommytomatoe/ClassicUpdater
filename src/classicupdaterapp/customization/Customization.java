package classicupdaterapp.customization;

public class Customization {
    //The String from the build.prop before the Version
    public static final String RO_MOD_START_STRING = "Classic";
    //Minimum Supported Version (So the User has to install google apps and so before)
    public static final String MIN_SUPPORTED_VERSION_STRING = RO_MOD_START_STRING + "4.1.99";
    //Updateinstructions for the min supported Version
    public static final String UPDATE_INSTRUCTIONS_URL = "http://forum.xda-developers.com/showthread.php?t=1312414";
    //DB filename
    public static final String DATABASE_FILE = "classicupdater.db";
    //DownloadDirectory
    public static final String DOWNLOAD_DIR = "download";
    //MUST be the first package name.
    public static final String PACKAGE_FIRST_NAME = "classicupdaterapp";
    //Filename for Instance save
    public static final String STORED_STATE_FILENAME = "classicupdater.state";
    //Android Board type
    public static final String BOARD = "ro.product.board";
    //Name of the Current Rom
    public static final String SYS_PROP_MOD_VERSION = "ro.modversion";
    //Screenshotsupport?
    public static final Boolean Screenshotsupport = true;
}
