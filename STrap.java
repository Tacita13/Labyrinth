package labyrinth;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Isamar
 */
//Class to implement an object type Static Trap that is in charge of setting the character/unicode, the id and the color
public class STrap {

    public static char icon = '\u00A5';
    public static char iconId = '3';
    public static Terminal.Color iconColor = Terminal.Color.GREEN;

    public void setColor(Terminal.Color ncolor) {
        iconColor = ncolor;
    }

    public void setIcon(char ic) {
        icon = ic;
    }

    public void setId(char id) {
        iconId = id;
    }

}
