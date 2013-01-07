package robowiki.rdk.util

class RdkUtils {

    public static String repositoryMirrorUrl = 'https://github.com/aleksey-zhidkov/rc-repo-mirror/raw/master/'

    public static String duelRumble = 'duel'
    public static String meleeRumble = 'melee'
    public static String teamsRumble = 'teams'
    public static String twinDuelRumble = 'twinDuel'

    public static Map<String, String> rumbleUrls = [
            duel: 'http://robowiki.net/wiki/RoboRumble/Participants',
            melee: 'http://robowiki.net/wiki/RoboRumble/Participants/Melee',
            teams: 'http://robowiki.net/wiki/RoboRumble/Participants/Teams',
            twinDuel: 'http://robowiki.net/wiki/RoboRumble/Participants/TwinDuel'
    ];

    public static String getJarName(String robotName) {
        robotName.replaceAll(' ', '_') + ".jar"
    }

    public static String duelRumbleUrl() { rumbleUrls[duelRumble] }
}
