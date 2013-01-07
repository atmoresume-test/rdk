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

options = new Options();
options.addOption(dirOpt)
options.addOption(rumbleOpt)
options.addOption(botOpt)

def parser = new PosixParser();
def cmd = parser.parse(options, args);

final rumble = cmd.getOptionValue('r', 'duel')
def rrBotsList = BotList.loadParticipants(RdkUtils.rumbleUrls[rumble])

def challengeName = "${rumble}-${new Date().format('yyyy-MM-dd')}"
new File("${cmd.getOptionValue('d', '.')}/${challengeName}.rrc").withWriter('UTF-8') {
    it << "${challengeName}\n"
    it << 'PERCENT_SCORE\n'
    it << '35 rounds\n'
    it << 'Group 1 {\n'
    def writer = it
    rrBotsList.each {
        writer << "  ${it.name}\n"
    }
    it << '}'
}