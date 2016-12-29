package labyrinth;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Isamar
 */
/*This class handles the Dynamic Traps located throughout the maze. It is based on
the use of two constructors, three sets and nine methods for dynamic following of the player.
*/
public class DynamicTrap extends LabyrinthNew {

    int[] currentPos = new int[2];
    Character Target;
    public static char icon = '\u0283';
    public static char iconId = '4';
    public static Terminal.Color iconColor = Terminal.Color.MAGENTA;
    
//CONSTRUCTOR #1: Recevices reference to character to follow so it can now its x and y coordinates 
    public DynamicTrap(int x, int y, Character newTarget) {

        Target = newTarget;
        currentPos[0] = x;
        currentPos[1] = y;
    }
//CONSTRUCTOR #2:  To set the icons, color and id of the type upon creation
    public DynamicTrap(int x, int y, Character newTarget, char icon, char iconId, Terminal.Color iconColor) {

        terminal.setCursorVisible(false);
        Target = newTarget;
        currentPos[0] = x;
        currentPos[1] = y;
        this.icon = icon;
        this.iconColor = iconColor;
        this.iconId = iconId;
    }

    public void setColor(Terminal.Color niconColor) {
        iconColor = niconColor;
    }

    public void setIcon(char nicon) {
        icon = nicon;
    }

    public void setId(char nid) {
        iconId = nid;
    }
//METHOD #1: First checks for possible free routes and then handles the possibility of having hit the target player to the left
    public boolean collisionLeft() {

        if (currentPos[1] - 1 > 0) {
            char next = field[currentPos[0]][currentPos[1] - 1];
            if (next == idWall || next == idStaticTrap || next == idDynamicTrap || next == idKey || next == idIn || next == idOut) {
                return true;
            }
            if (next == wall || next == StaticTrap || next == DynamicTrap || next == key || next == cIn || next == cOut) {
                return true;
            }
            if (next == player || next == idPlayer) {
                try {
                    me.HandleDamage();
                } catch (Exception a) {
                    nrLives -= 1;
                }
                return true;
            }
            return false;
        }
        return true;
    }
//METHOD #2: First checks for possible free routes and then handles the possibility of having hit the target player up
    public boolean collisionUp() {

        if (currentPos[0] - 1 > 0) {
            char next = field[currentPos[0] - 1][currentPos[1]];
            if (next == idWall || next == idStaticTrap || next == idDynamicTrap || next == idKey || next == idIn || next == idOut) {
                return true;
            }
            if (next == wall || next == StaticTrap || next == DynamicTrap || next == key || next == cIn || next == cOut) {
                return true;
            }
            if (next == player || next == idPlayer) {
                try {
                    me.HandleDamage();
                } catch (Exception a) {
                    nrLives -= 1;
                }
                return true;
            }
            return false;
        }
        return true;
    }
//METHOD #3: First checks for possible free routes and then handles the possibility of having hit the target player to the right
    public boolean collisionRight() {
        if (currentPos[1] + 1 < width) {
            char next = field[currentPos[0]][currentPos[1] + 1];
            if (next == idWall || next == idStaticTrap || next == idDynamicTrap || next == idKey || next == idIn || next == idOut) {

                return true;
            }
            if (next == wall || next == StaticTrap || next == DynamicTrap || next == key || next == cIn || next == cOut) {
                return true;
            }
            if (next == player || next == idPlayer) {
                try {
                    me.HandleDamage();
                } catch (Exception a) {
                    nrLives -= 1;
                }
                return true;
            }
            return false;
        }
        return true;
    }
//METHOD #4: First checks for possible free routes and then handles the possibility of having hit the target player down
    public boolean collisionDown() {

        if (currentPos[0] + 1 < height) {
            char next = field[currentPos[0] + 1][currentPos[1]];
            if (next == idWall || next == idStaticTrap || next == idDynamicTrap || next == idKey || next == idIn || next == idOut) {
                return true;
            }
            if (next == wall || next == StaticTrap || next == DynamicTrap || next == key || next == cIn || next == cOut) {
                return true;
            }
            if (next == player || next == idPlayer) {
                try {
                    me.HandleDamage();
                } catch (Exception a) {
                    nrLives -= 1;
                }
                return true;
            }

            return false;
        }
        return true;
    }
//METHOD #5: Tries to move left. If successful returs true and positions the enemy.
    public boolean moveLeft() {

        int newX, newY;
        if (!collisionLeft()) {
            newX = currentPos[0];
            newY = currentPos[1] - 1;
            field[currentPos[0]][currentPos[1]] = free;
            screen.putString(currentPos[1], currentPos[0], "" + free, Terminal.Color.WHITE, Terminal.Color.BLACK);
            field[newX][newY] = DynamicTrap;
            currentPos[0] = newX;
            currentPos[1] = newY;
            screen.putString(newY, newX, "" + DynamicTrap, Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
            return true;
        }
        return false;
    }
//METHOD #6: Tries to move up. If successful returs true and positions the enemy.
    public boolean moveUp() {

        int newX, newY;
        if (!collisionUp()) {
            newX = currentPos[0] - 1;
            newY = currentPos[1];
            field[currentPos[0]][currentPos[1]] = free;
            screen.putString(currentPos[1], currentPos[0], "" + free, Terminal.Color.WHITE, Terminal.Color.BLACK);
            field[newX][newY] = DynamicTrap;
            currentPos[0] = newX;
            currentPos[1] = newY;
            screen.putString(newY, newX, "" + DynamicTrap, Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
            return true;
        }
        return false;
    }
//METHOD #7: Tries to move right. If successful returs true and positions the enemy.
    public boolean moveRight() {
        int newX, newY;
        if (!collisionRight()) {
            newX = currentPos[0];
            newY = currentPos[1] + 1;
            field[currentPos[0]][currentPos[1]] = free;
            screen.putString(currentPos[1], currentPos[0], "" + free, Terminal.Color.WHITE, Terminal.Color.BLACK);
            field[newX][newY] = DynamicTrap;
            currentPos[0] = newX;
            currentPos[1] = newY;
            screen.putString(newY, newX, "" + DynamicTrap, Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();

            return true;
        }
        return false;

    }
//METHOD #8: Tries to move down. If successful returs true and positions the enemy.
    public boolean moveDown() {
        int newX, newY;
        if (!collisionDown()) {

            newX = currentPos[0] + 1;
            newY = currentPos[1];
            field[currentPos[0]][currentPos[1]] = free;
            screen.putString(currentPos[1], currentPos[0], "" + free, Terminal.Color.WHITE, Terminal.Color.BLACK);
            field[newX][newY] = DynamicTrap;
            currentPos[0] = newX;
            currentPos[1] = newY;
            screen.putString(newY, newX, "" + DynamicTrap, Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();

            return true;
        }
        return false;
    }
//METHOD #9: Indicate if the player has moved, if false will try alternate movement  
    public void dynamicMove() {

        boolean moved = false;
      //Player is to the left
        if (me.currentPos[1] < currentPos[1]) {
            moved = moveLeft();
            if (!moved) {
                moved = moveUp();
            }
            if (!moved) {
                moved = moveDown();
            }
            if (!moved) {
                moveRight();
            }
        } //Player is to the right 
        else if (me.currentPos[1] > currentPos[1]) {
            moved = moveRight();
            if (!moved) {
                moved = moveUp();
            }
            if (!moved) {
                moved = moveDown();
            }
            if (!moved) {
                moveLeft();
            }
        } //Player is above
        else if (me.currentPos[0] < currentPos[0]) {
            moved = moveUp();
            if (!moved) {
                moved = moveRight();
            }
            if (!moved) {
                moved = moveLeft();
            }
            if (!moved) {
                moveDown();
            }
        } // Player is down
        else if (me.currentPos[0] > currentPos[0]) {
            moved = moveDown();
            if (!moved) {
                moved = moveRight();
            }
            if (!moved) {
                moved = moveLeft();
            }
            if (!moved) {
                moveUp();
            }
        }
    }
}
