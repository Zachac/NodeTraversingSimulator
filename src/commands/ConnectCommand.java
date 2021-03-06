package commands;

import console.Helper;
import console.User;
import model.Direction;
import model.RoomNode;

/**
 * A command to connect two rooms together. The current room, and another room given in the form of a room number. If
 * possible they will be connect bi-directionally. If that isn't possible only the path in the direction of the current
 * room will be set. If there is already a room in the given direction the command will fail.
 * 
 * @author Zachary Chandler
 */
public class ConnectCommand extends Command {

    @Override
    public String[] getAliases() {
        return new String[] {"CONNECT"};
    }

    @Override
    public String getPreferredName() {
        return "connect";
    }
    
    @Override
    public void runCommand(User info, String[] args) {
        boolean error = false;
        
        if (args.length < 3) {
            info.print("Not enough arguments, ");
            error = true;
        } else if (args.length > 3) {
            info.println("Too many arguments, ");
            error = true;
        } else {
            Direction direction = Direction.translateDirection(args[1]);
            Direction oppositeDirection = Direction.getOppositeDirection(direction);
            
            Integer choice = Helper.safeParseInt(args[2]);
            
            if (direction == null) {
                info.print("Direction not found, ");
                error = true;
            } else if (info.getCurrentRoom().getDirection(direction) != null) {
                info.print("There is already a room that way, ");
                error = true;
            }
            
            if (choice == null) {
                info.print("Invalid room number format, ");
                error = true;
            }

            if (!error) {
                RoomNode room = info.u.rooms.getRoom(choice);
                
                if (room == null) {
                    info.print("Room not found, ");
                    error = true;
                } else {
                    info.getCurrentRoom().setDirection(direction, room);
                    
                    if (room.getDirection(oppositeDirection) == null) {
                        room.setDirection(oppositeDirection, info.getCurrentRoom());
                    }
                    
                    String message = Helper.buildString(info.getUsername(), " connected ",
                            info.getCurrentRoom().getName(), " to ", room.getName());
                    info.chat.printlnToRoom(message);
                }
            }
        }
        
        if (error) {
            info.print("see 'help connect' for more details.\n");
        }
    }

    @Override
    public String getShortHelpDescription() {
        return "Connects to rooms togeather, bi-directionally if possible.";
    }

}
