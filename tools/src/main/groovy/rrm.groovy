import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.PosixParser
import org.eclipse.jgit.api.FetchCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PullCommand
import org.eclipse.jgit.transport.FetchResult
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import robowiki.rdk.util.RdkUtils

def dirOpt = new Option("d", "dir", true, "Repository directory");
dirOpt.setArgs(1);
dirOpt.setOptionalArg(true);
dirOpt.setArgName("dir ");

def userOpt = new Option("u", "user", true, "GitHub user");
userOpt.setArgs(1);
userOpt.setOptionalArg(true);
userOpt.setArgName("user ");

def passOpt = new Option("p", "pass", true, "GitHub password");
passOpt.setArgs(1);
passOpt.setOptionalArg(true);
passOpt.setArgName("pass ");

options = new Options();
options.addOption(dirOpt)
options.addOption(userOpt)
options.addOption(passOpt)

def parser = new PosixParser();
def cmd = parser.parse(options, args);

def File botsDir = new File(cmd.getOptionValue('d', 'bots'))

final Git git = Git.open(botsDir)

if (!git.status().call().isClean()) {
    println('Repository is not clean!')
    return
}

print('Fetch remote repository... ')
final FetchCommand fetchCommand = git.fetch()
final credentialsProvider = new UsernamePasswordCredentialsProvider(cmd.getOptionValue('u'), cmd.getOptionValue('p'))
fetchCommand.setCredentialsProvider(credentialsProvider)
fetchCommand.setRemote('origin')
fetchCommand.call()
println('Done')

println('Download duel rumble bots')
bdl.main(['-d', botsDir.getAbsolutePath(), '-r', RdkUtils.duelRumble] as String[])
println('Download melee rumble bots')
bdl.main(['-d', botsDir.getAbsolutePath(), '-r', RdkUtils.meleeRumble] as String[])
println('Download teams rumble bots')
bdl.main(['-d', botsDir.getAbsolutePath(), '-r', RdkUtils.teamsRumble] as String[])
println('Download twin duel rumble bots')
bdl.main(['-d', botsDir.getAbsolutePath(), '-r', RdkUtils.twinDuelRumble] as String[])

if (git.status().call().isClean()) {
    println('Nothing to commit')
    return
}

def addCmd = git.add()
addCmd.addFilepattern('.')
addCmd.call()

print('Commit new bots... ')
def commitCmd = git.commit()
commitCmd.setMessage("Update ${new Date()}")
commitCmd.call()
println('Done')

print('Push new bots... ')
def pushCmd = git.push()
pushCmd.setCredentialsProvider(credentialsProvider)
pushCmd.add('master')
pushCmd.setPushTags()
pushCmd.setRemote('origin')
pushCmd.call()
println('Done')