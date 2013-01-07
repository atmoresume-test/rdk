import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.PosixParser
import robowiki.rdk.util.BotList
import robowiki.rdk.util.RdkUtils

def dirOpt = new Option("d", "dir", true, "Destination directory");
dirOpt.setArgs(1);
dirOpt.setOptionalArg(true);
dirOpt.setArgName("dir ");

def rumbleOpt = new Option("r", "rumble", true, "RoboRumble type (duel, melee, teams, twinDuel)");
dirOpt.setArgs(1);
dirOpt.setOptionalArg(true);
dirOpt.setArgName("rumble ");

def botOpt = new Option("b", "bot", true, "Bot name regexp pattern");
dirOpt.setArgs(1);
dirOpt.setOptionalArg(true);
dirOpt.setArgName("bot ");

def chlgOpt = new Option("c", "challenge", true, "Path to rrc file");
chlgOpt.setArgs(1);
chlgOpt.setOptionalArg(true);
chlgOpt.setArgName("challenge ");

options = new Options();
options.addOption(dirOpt)
options.addOption(rumbleOpt)
options.addOption(botOpt)
options.addOption(chlgOpt)

def parser = new PosixParser();
def cmd = parser.parse(options, args);

def botsList = [];

if (cmd.hasOption('c')) {
    def inGroup = false
    new File(cmd.getOptionValue('c')).eachLine { line ->
        if (line.endsWith('{')) {
            inGroup = true;
        } else if (line.endsWith('}')) {
            inGroup = false;
        } else if (inGroup) {
            final name = line.trim()
            botsList << [name: name,
             url: "${RdkUtils.repositoryMirrorUrl}/${RdkUtils.getJarName(name)}"]
        }
    }
} else {
    botsList = BotList.loadParticipants(RdkUtils.rumbleUrls[cmd.getOptionValue('r', 'duel')])
}

def botNamePattern = cmd.getOptionValue('b', '.*')

File dir = new File(cmd.getOptionValue('d', 'bots'))

if (!dir.exists()) {
    dir.mkdirs()
}

def botsToDownLoad = []
botsList.each {

    if (it.name ==~ botNamePattern) {
        botsToDownLoad << it
    }

}

int cnt = 0;
println()
botsToDownLoad.each {
    print "Download bot ${it.name} (${cnt + 1}/${botsToDownLoad.size()})... "
    try {
        downloadBot(dir, it)
    } catch (IOException e) {
        println(" Failed: " + e.toString())
        println(" Url: " + it.url)
    }
    cnt++
}

// todo: download in parallel
def downloadBot(File dir, Map<String, String> bot) {
    final jarName = RdkUtils.getJarName(bot.name)
    def botFile = new File(dir, jarName)
    if (botFile.exists()) {
        println('Exists!')
        return
    }
    try {
        def out = new BufferedOutputStream(new FileOutputStream(botFile))
        out << new URL(bot.url).openStream()
        out.close()
        println('Done')
    } catch (Throwable t) {
        botFile.delete()
        println('Download from original url failed, try to download from mirror')
        try {
            def out = new BufferedOutputStream(new FileOutputStream(botFile))
            out << new URL("${RdkUtils.repositoryMirrorUrl}/${jarName}").openStream()
            out.close()
            println('Done')
        } catch (Throwable ignored) {
            botFile.delete()
            throw t
        }
    }
}

println()