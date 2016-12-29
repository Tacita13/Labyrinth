
package labyrinth;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Isamar
 */
//Class to implement an object type key that is in charge of setting the character/unicode, the id and the color
public class Keys {

    public static char icon = '\u274A';
    public static char iconId = '5';
    public static Terminal.Color iconColor = Terminal.Color.CYAN;

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
