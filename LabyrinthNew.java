package labyrinth;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
/**
 *
 * @author Isamar
 */

/*This program is designed as a labyrinth game with the minimum requirements of having a player
that moves through the labyrinth, avoiding traps and colllecting items until he can get out 
of the maze and win the game. The game embodies three layers Terminal, Screen and GUI Interface.
As a whole it contains the majority of the most important variables, two classes, thirteen methods
and the main*/
public class LabyrinthNew {

    public static SwingTerminal terminal = TerminalFacade.createSwingTerminal();
    static final Properties save = new Properties();
    static final String saveFileName = "savefile";
    static boolean loading = false;
    static LinkedList<DynamicTrap> listofTraps = new LinkedList();

    //static StaticTrap sTrap=new StaticTrap('\u00A5','3',Terminal.Color.GREEN);
    static final char idWall = Wall.iconId;
    static final char idIn = Entry.iconId;
    static final char idOut = Exit.iconId;
    static final char idStaticTrap = STrap.iconId;
    static final char idDynamicTrap = '4';
    static final char idKey = Keys.iconId;
    static final char idPlayer = '6';

    static final char wall = Wall.icon;
    static final char free = ' ';
    static final char cIn = Entry.icon;
    static final char cOut = Exit.icon;
    static final char DynamicTrap = '\u0283';
    static final char StaticTrap = STrap.icon;
    static final char key = Keys.icon;
    static final char player = 'A';
    static final char WallCase = '\u25b2';
    static final char cInCase = '=';
    static final char cOutCase = '\u263A';
    static final char[] fieldElements = {wall, cIn, cOut, StaticTrap, DynamicTrap, key, player, free};
    
    static int nrLives = 3;
    static boolean winning;
    static Character me;
    static int width, height, nrIn, nrOut, nrKeys, nrStaticTraps, nrDynamicTraps;
    static char[][] field;
    static GUIScreen mainmenu = TerminalFacade.createGUIScreen();
    private static String seed;
    public static Screen screen = new Screen(terminal);
    private static Timer timer;

//CLASS #1: Where the tasks will be run on a timer
    static class ToDo extends TimerTask {

        @Override
        public void run() {
            //thing that runs
            listofTraps.stream().forEach((d) -> {
                d.dynamicMove();
            });
        }
    }
//METHOD #1: In charge of clearing the enemies from the field when needed
    public static void clearEnemies() {
        listofTraps.stream().map((d) -> {
            terminal.moveCursor(d.currentPos[1], d.currentPos[0]);
            return d;
        }).forEach((_item) -> {
            terminal.putCharacter(free);
        });
    }
//METHOD #2: Starts the scheduled timer in miliseconds
    public static void startTimer(int milisec) {
        timer = new Timer();
        timer.schedule(new ToDo(), 0, milisec);

    }
//METHOF #3: Stops the timer after using startTimer()
    public static void stopTimer() {
        timer.cancel();
    }
//METHOD #4: Displays the current number of lives the player has left
    public static void Lives(int lives) {
        if (lives == 3) {
            screen.putString(6, 0, " lives: 3 ", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
        } else if (lives == 2) {
            screen.putString(6, 0, " lives: 2 ", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
        } else if (lives == 1) {
            terminal.moveCursor(7, 0);
            screen.putString(6, 0, " lives: 1 ", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
        }
    }
//METHOD #5: Displays the current number of stars the player has collected
    public static void Key(int key) {
        if (key == 5) {
            screen.putString(16, 0, "Keys: 0/5 ", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
        } else if (key == 4) {
            screen.putString(16, 0, "Keys: 1/5 ", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
        } else if (key == 3) {
            screen.putString(16, 0, "Keys: 2/5 ", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
        } else if (key == 2) {
            screen.putString(16, 0, "Keys: 3/5 ", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
        } else if (key == 1) {
            screen.putString(16, 0, "Keys: 4/5 ", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
        } else if (key == 0) {
            screen.putString(16, 0, "Keys: 5/5 ", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.refresh();
        }
    }
////METHOD #6: Displays the main field and the icons: Static trap, exit, key and entry
    public static void printField(int lives) {
        terminal.applyForegroundColor(Terminal.Color.YELLOW);

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {

                if (field[i][j] == StaticTrap || field[i][j] == StaticTrap) {
                    screen.putString(j, i, field[i][j] + "", STrap.iconColor, Terminal.Color.BLACK);
                } else if (field[i][j] == cOut || field[i][j] == cOut) {
                    screen.putString(j, i, field[i][j] + "", Exit.iconColor, Terminal.Color.BLACK);
                } else if (field[i][j] == key || field[i][j] == key) {
                    screen.putString(j, i, field[i][j] + "", Keys.iconColor, Terminal.Color.BLACK);
                } else if (field[i][j] == cIn || field[i][j] == cIn) {
                    screen.putString(j, i, field[i][j] + "", Entry.iconColor, Terminal.Color.BLACK);
                } else {
                    screen.putString(j, i, field[i][j] + "", Terminal.Color.YELLOW, Terminal.Color.BLACK);
                }
            }
        }
    }
    
//          >>>>>>>>>>>>      GUI LAYER     <<<<<<<<<<<<
    
//CLASS #2: In charge of holding the main window which holds in itself the different panels/menus/buttons, etc.
    public static class MainWindow extends Window {

        Panel pauseMenuPanel = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        Panel mainMenuPanel = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        Panel legendPanel = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        Panel legendPanel2 = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        Panel endPanel = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);

//The main constructor that holds the legend, all the possible buttons, the added panels, and the logic for win/lose 
        public MainWindow(String type) {
            super("The Sands of Time");

            legendPanel.addComponent(new Label(" "));
            legendPanel.addComponent(new Label("Sand dunes ............. \u25b2"));
            legendPanel.addComponent(new Label("Player ................. A"));
            legendPanel.addComponent(new Label("Cactus ................. \u00A5"));
            legendPanel.addComponent(new Label("Enemy snake ............ \u0283"));
            legendPanel.addComponent(new Label("Tiny star .............. \u274A"));
            legendPanel.addComponent(new Label("Exit ................... \u263A"));
            legendPanel.addComponent(new Label(" "));

            Button backButton = new Button("Back", () -> {
                removeComponent(legendPanel);
                addComponent(pauseMenuPanel);
            });
            legendPanel.addComponent(backButton);

            Button backToMainMenuButton = new Button("Back to Main Menu", () -> {
                removeComponent(endPanel);
                addComponent(mainMenuPanel);
            });
            Button backToMainMenuButton2 = new Button("Back to Main Menu", () -> {
                removeComponent(legendPanel2);
                addComponent(mainMenuPanel);
            });

            Button exitButton = new Button("Exit", () -> {
                close();
                System.exit(1);
            });

            Button ResumeButton = new Button("Resume", () -> {
                close();
            });

            Button saveButton = new Button("Save & Exit", () -> {
                writeSaveFile();
                close();
                System.exit(1);
            });

            Button legendButton = new Button("Legend", () -> {
                getLegend();
            });
            Button legendMainButton = new Button("Legend", () -> {
                getLegend2();
            });
            legendPanel2.addComponent(new Label(" "));
            legendPanel2.addComponent(new Label("Sand dunes ............. \u25b2"));
            legendPanel2.addComponent(new Label("Player ................. A"));
            legendPanel2.addComponent(new Label("Cactus.................. \u00A5"));
            legendPanel2.addComponent(new Label("Enemy snake ............ \u0283"));
            legendPanel2.addComponent(new Label("Tiny star .............. \u274A"));
            legendPanel2.addComponent(new Label("Exit ................... \u263A"));
            legendPanel2.addComponent(new Label(" "));
            legendPanel2.addComponent(backToMainMenuButton2);

            pauseMenuPanel.addComponent(ResumeButton);
            pauseMenuPanel.addComponent(saveButton);
            pauseMenuPanel.addComponent(legendButton);
            pauseMenuPanel.addComponent(exitButton);

            Button StartButton = new Button("Start Game", () -> {
                close();
            });

            Button LoadButton = new Button("Load", () -> {
                File f = new File(saveFileName);
                if (f.exists() && !f.isDirectory()) {
                    loading = true;
                    close();
                } else {
                    addComponent(new Label("ERROR: Save File Not Found", Terminal.Color.RED));
                }
            });

            mainMenuPanel.addComponent(StartButton);
            mainMenuPanel.addComponent(LoadButton);
            mainMenuPanel.addComponent(legendMainButton);
            mainMenuPanel.addComponent(exitButton);

//Main Window recieves a String type as parameter to work out the corresponding actions between showing main menu, winning or loosing
            if ("main".equals(type)) {
                addComponent(mainMenuPanel);

            } else if ("win".equals(type)) {
                endPanel.addComponent(new Label("     You Won! "));
                endPanel.addComponent(new Label("  "));
                endPanel.addComponent(backToMainMenuButton);
                endPanel.addComponent(exitButton);
                addComponent(endPanel);
                type = "";
                winning = false;
                nrLives = 3;

            } else if ("lose".equals(type)) {

                endPanel.addComponent(new Label("     You Lost "));
                endPanel.addComponent(new Label("  "));
                endPanel.addComponent(backToMainMenuButton);
                endPanel.addComponent(exitButton);
                addComponent(endPanel);
                type = "";
                winning = false;
                nrLives = 3;

            } else {
                addComponent(pauseMenuPanel);
            }

        }
//Recieve the legends for the corresponding Menu
        public void getLegend() {
            removeComponent(pauseMenuPanel);
            addComponent(legendPanel);
        }

        public void getLegend2() {
            removeComponent(mainMenuPanel);
            addComponent(legendPanel2);
        }
    }
//          >>>>>>>>   HERE ENDS GUI LAYER  <<<<<<<<<
    
//METHOD #7: Calls the Main Menu, aka. as the first
    public static void startMainMenu() {
        mainmenu.getScreen().startScreen();
        MainWindow mainWindow = new MainWindow("main");
        mainmenu.showWindow(mainWindow, GUIScreen.Position.CENTER);
        mainmenu.getScreen().stopScreen();
    }
//METHOD #8: Calls the Main Menu when the game is paused
    public static void pauseGame(Screen resumescreen) {
        resumescreen.stopScreen();
        mainmenu.getScreen().startScreen();
        mainmenu.showWindow(new MainWindow("pause"), GUIScreen.Position.CENTER);
        mainmenu.getScreen().stopScreen();
        resumescreen.startScreen();
        int i = 1;
        startTimer(1000);
        printField(nrLives);
    }
//METHOD #9: Gives the corresponding message and following actions after winning
    public static void winGame() throws InterruptedException {
        mainmenu.getScreen().startScreen();
        MainWindow mainWindow = new MainWindow("win");
        mainmenu.showWindow(mainWindow, GUIScreen.Position.CENTER);
        mainmenu.getScreen().stopScreen();
        stopTimer();
        clearEnemies();
        startGame();
    }
//METHOD #10: Gives the corresponding message and following actions after loosing
    public static void loseGame() throws InterruptedException {
        mainmenu.getScreen().startScreen();
        MainWindow mainWindow = new MainWindow("lose");
        mainmenu.showWindow(mainWindow, GUIScreen.Position.CENTER);
        mainmenu.getScreen().stopScreen();
        stopTimer();
        clearEnemies();
        startGame();
    }

// METHOD #11: Starts the actual game and runs all methods depending on their application
    public static void startGame() throws InterruptedException {
        nrKeys = 0;
        terminal.queryTerminalSize();
        
//Starts the actual screen to start the game
        screen.startScreen();
        terminal.getTerminalSize();
        if (screen.resizePending()) {
            screen.refresh();
        }
        try {
            if (loading == true) {
                readSaveFile();

            } else {

//Generate maze and other values randomly
                Generate.seed = Generate.init();
                Generate.compute(Generate.seed);
                Generate.write(Generate.seed);
            }

        } catch (Exception e) {
        }

        winning = false;
        printField(nrLives);

//Start trap timers
        startTimer(1000);
//Show the number of keys and Lives
        Key(nrKeys);
        Lives(nrLives);
//Receive input while game is active
        while (!winning && nrLives > 0) {
            terminal.setCursorVisible(false);
            Key inputkey = terminal.readInput();
//Recieves, reads and runs after key input            
            while (inputkey == null) {
                Thread.sleep(5);
                inputkey = terminal.readInput();
            }

            if (inputkey.getKind() == com.googlecode.lanterna.input.Key.Kind.ArrowRight) {
                winning = me.move(me.currentPos[0], me.currentPos[1] + 1);

            } else if (inputkey.getKind() == com.googlecode.lanterna.input.Key.Kind.ArrowLeft) {
                winning = me.move(me.currentPos[0], me.currentPos[1] - 1);

            } else if (inputkey.getKind() == com.googlecode.lanterna.input.Key.Kind.ArrowUp) { 
                winning = me.move(me.currentPos[0] - 1, me.currentPos[1]);

            } else if (inputkey.getKind() == com.googlecode.lanterna.input.Key.Kind.ArrowDown) {
                winning = me.move(me.currentPos[0] + 1, me.currentPos[1]);

            } else if (inputkey.getKind() == inputkey.getKind().Escape) {
                stopTimer();
                clearEnemies();
                pauseGame(screen);
            }
        } 
//End of game loop
//Tells if you won or you lost
        if (winning) {
            screen.clear();
            screen.stopScreen();
            winGame();

        } else if (nrLives <= 0) {
            screen.clear();
            screen.stopScreen();
            loseGame();
        }   
    }

//METHOD #12: Loads saved data and throws error message if non is found
    public static void readSaveFile() {
        File f = new File(saveFileName);
        try {
            try (FileInputStream fileInput = new FileInputStream(f)) {
                save.load(fileInput);
            }

        } catch (IOException e) {
            System.out.println("save file not found");
        }
//Sets the saved integer values to load them again
        height = Integer.valueOf(save.getProperty("Height"));
        width = Integer.valueOf(save.getProperty("Width"));
        nrLives = Integer.valueOf(save.getProperty("Lives"));
        nrKeys = Integer.valueOf(save.getProperty("Keys"));
        field = new char[height][width];
        me = new Character(Integer.valueOf(save.getProperty("PlayerX")), Integer.valueOf(save.getProperty("PlayerY")));

        String coordinates = "";
        String item;
        int c = 0;
        Lives(nrLives);
        Key(nrKeys);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                   
                coordinates = j + "," + i;
//returns 7=freespace if no match is found in save file for that coordinate
                item = save.getProperty(coordinates, "7");
 // this means it is an empty space
                if (item.equals("" + idDynamicTrap)) {
                    listofTraps.add(new DynamicTrap(i, j, me));
                    
                } else if (item.equals("" + idStaticTrap)) {
                }

                field[i][j] = fieldElements[Integer.valueOf(item)];
            }
        }
    }        
    
//METHOD #13: Saves the game, as in the cooordinates of every id of every character
    public static void writeSaveFile() {

        save.setProperty("Width", "" + width);
        save.setProperty("Height", "" + height);
        save.setProperty("PlayerX", "" + me.currentPos[0]);
        save.setProperty("PlayerY", "" + me.currentPos[1]);
        save.setProperty("Keys", "" + nrKeys);
        save.setProperty("Lives", "" + nrLives);
        int c = 0;

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                String coordinates = y + "," + x;
                char next = field[x][y];

                if (next == StaticTrap) {
                    save.setProperty(coordinates, "" + idStaticTrap);
                } else if (next == wall) {
                    save.setProperty(coordinates, "" + idWall);
                } else if (next == cIn) {
                    save.setProperty(coordinates, "" + idIn);
                } else if (next == DynamicTrap) {
                    save.setProperty(coordinates, "" + idDynamicTrap);
                } else if (next == key) {
                    save.setProperty(coordinates, "" + idKey);
                } else if (next == player) {
                    save.setProperty(coordinates, "" + idPlayer);
                }else if (next == cOut) {
                    save.setProperty(coordinates, "" + idOut);
                }
                if (next >= '1' && next <= '9') {
                    save.setProperty(coordinates, "" + next);
                }
                if (next == free) {
                    c++;
                }
            }
        }

        try {
            try (FileOutputStream outFile = new FileOutputStream(new File(saveFileName))) {
                save.store(outFile, "Properties (Seed=" + Generate.seed + ")");
                outFile.close();
            }
        } catch (Exception e) {
        }
    }
//The Main, runs as Main should do, everything. Where everything here means the Main Menu and the actual game
    public static void main(String[] args) throws InterruptedException {

        startMainMenu();
        startGame();

    }
}
