package labyrinth;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Isamar
 */
//Class Character is in charge of getting the character ccordinates to identify enemies and keys, handle damage, move and check for exits.
public class Character extends LabyrinthNew {
// indacates wether the player can exis the maze, depedent on nrkeys being 0
    boolean canExit = false;
    public int[] currentPos = new int[2];
//Constructor to locate current position of the player
    public Character(int x, int y) {

        super();
        currentPos[0] = x;
        currentPos[1] = y;
    }
/*METHOD #1: Checks if the character is moving towards a key, if it is a key the numbrt of
 keys available is decreased, but if there are no keys left the player can exit the maze and canExit is true*/
    public void checkKey(int x, int y) {

        if (field[x][y] == key) {
            nrKeys -= 1;
            Key(nrKeys);

            if (nrKeys <= 0) {
                canExit = true;
            }
        }
    }
//METHOD #2: Checks if the character is moving towards an enemy or vice versa, if so will return true
    public boolean checkEnemy(int x, int y) {
        return field[x][y] == StaticTrap || field[x][y] == DynamicTrap || field[x][y] == idStaticTrap || field[x][y] == idDynamicTrap;
    }
/*METHOD #3: The player takes damage so the lives are decreased and newly displayed.
 Also player briefly changes color to red when comming in contact with a trap/enemy */
    public void HandleDamage() throws InterruptedException {
        nrLives -= 1;
        Lives(nrLives);
        screen.putString(currentPos[1], currentPos[0], player + "", Terminal.Color.RED, Terminal.Color.BLACK);
        screen.refresh();
        Thread.sleep(500);
        screen.putString(currentPos[1], currentPos[0], player + "", Terminal.Color.YELLOW, Terminal.Color.BLACK);
        screen.refresh();

    }
/*METHOD #4: Enables the movement of the player trought the maze, puts the actual 
 Puts the actual character in the position as long as it is free (no walls or traps). 
  Also manages the actual exit route once requirements are fullfilled.*/
    public boolean move(int x, int y) {

        boolean value = false;
        terminal.setCursorVisible(false);
        if ((x >= 0 && x < height) && (y >= 0 && y < width)) {
            if (field[x][y] != wall) {
                checkKey(x, y);

                if (cOut == field[x][y]) {
                    // if moving to an exit
                    if (canExit) {
                        // if all keys are obtained move to exit and win
                        value = true;
                        field[currentPos[0]][currentPos[1]] = free;
                        field[x][y] = player;
                        screen.putString(currentPos[1], currentPos[0], free + "", Terminal.Color.YELLOW, Terminal.Color.BLACK);

                        screen.putString(y, x, player + "", Terminal.Color.YELLOW, Terminal.Color.BLACK);
                        screen.refresh();
                    }//if missing keys do nothing 

                } else if (checkEnemy(x, y)) {
                    //add feedback to enemy collision 
                    try {
                        HandleDamage();
                    } catch (Exception e) {
                        nrLives -= 1;
                    }
                } else {
                    field[currentPos[0]][currentPos[1]] = free;
                    field[x][y] = player;
                    screen.putString(currentPos[1], currentPos[0], free + "", Terminal.Color.YELLOW, Terminal.Color.BLACK);

                    screen.putString(y, x, player + "", Terminal.Color.YELLOW, Terminal.Color.BLACK);
                    screen.refresh();

                    currentPos[0] = x;
                    currentPos[1] = y;
                }
            }
        }
        return value;
    }
//METHOD #5: Function sets the availabilty of an exit to true
    public boolean Door(int x, int y) {
        return canExit && cOut == field[x][y];
    }

}
