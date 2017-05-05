package console_gui;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;

import commands.Command;
import commands.CommandNotFoundCommand;
import commands.CreateCommand;
import commands.DestroyCommand;
import commands.DigCommand;
import commands.DownCommand;
import commands.EastCommand;
import commands.EscapeCurrentLocationCommand;
import commands.ExamineCommand;
import commands.LookCommand;
import commands.NorthCommand;
import commands.QuitCommand;
import commands.RunnableCommand;
import commands.SouthCommand;
import commands.TeleportCommand;
import commands.UnLinkCommand;
import commands.UpCommand;
import commands.WallOffCommand;
import commands.WestCommand;

/**
 * This class deals with handling user input. Which comes in the form of getting commands or just getting entire lines
 * of text from the user.
 * 
 * @author Zachary Chandler
 */
public class UserInputScanner {
    
    /** The commands this scanner recognizes. */
    private static final RunnableCommand[] COMMANDS = {
            new CommandNotFoundCommand(),
            new QuitCommand(),
            new DownCommand(),
            new UpCommand(),
            new NorthCommand(),
            new EastCommand(),
            new SouthCommand(),
            new WestCommand(),
            new LookCommand(),
            new DigCommand(),
            new WallOffCommand(),
            new EscapeCurrentLocationCommand(),
            new UnLinkCommand(),
            new ExamineCommand(),
            new CreateCommand(),
            new DestroyCommand(),
            new TeleportCommand()
    };
    
    /** Commands that have been queued up by the user. */
    private Queue<Command> commands;
    
    /** A map of aliases to commands for fast lookup time. */
    private Map<String, RunnableCommand> commandMap;
    
    /** The scanner we will use to get input. */
    private Scanner input;

    /**
     * Instantiate the class on the given InputStream.
     * 
     * @param in the input stream from the user.
     */
    public UserInputScanner(InputStream in) {
        this.commands = new LinkedList<Command>();
        this.input = new Scanner(in);
        
        this.commandMap = new TreeMap<>();
        
        for (RunnableCommand c : COMMANDS) {
            String[] aliases = c.getAliases();
            for (int i = 0; i < aliases.length; i++) {
                commandMap.put(aliases[i].toUpperCase(), c);
            }
        }
    }
    
    /**
     * Just get the next line from the user.
     * @return the next line of input from the user.
     */
    public String nextLine() {
        return input.nextLine();
    }
    
    /**
     * If the user has any Queued commands, wise input prompters can use this information to avoid giving prompts to 
     * commands that have already been scanned.
     * @return if there are any commands waiting to be run.
     */
    public boolean hasQueuedCommand() {
        return !commands.isEmpty();
    }
    
    /**
     * @return the next command from the user.
     */
    public Command getNextCommand() {
        Command result;
        
        if (commands.isEmpty()) {
            String[] semiSplit = input.nextLine().split("(\\s*;+\\s*)+");
            
            for (int i = 0; i < semiSplit.length; i++) {
                Command c = translateCommmand(semiSplit[i]);
                
                if (c != null) {
                    commands.add(c);
                }
            }
        }
        
        result = commands.poll();
        
        return result;
    }
    
    /**
     * Translates a given string to a command.
     * @param s the string to parse.
     * @return the resulting command.
     */
    public Command translateCommmand(String s) {
        Command result = null;

        String[] args = s.split("\\s+");
        
        if (args.length > 0) {
            RunnableCommand runner = commandMap.get(args[0].toUpperCase());
            
            if (runner == null) {
                runner = commandMap.get(CommandNotFoundCommand.LOOK_UP_STRING);
            }
            
            result = new Command(args, runner);
        }
        
        return result;
    }
}
