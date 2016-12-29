
package labyrinth;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Isamar
 */
//Class to implement an object type entry that is in charge of setting the character/unicode, the id and the color
public class Entry {

    public static char icon = '=';
    public static char iconId = '1';
    public static Terminal.Color iconColor = Terminal.Color.WHITE;

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
