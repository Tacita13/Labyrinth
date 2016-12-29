
package labyrinth;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Isamar
 */
//Class to implement an object type exit that is in charge of setting the character/unicode, the id and the color
public class Exit {

    public static char icon = '\u263A';
    public static char iconId = '2';
    public static Terminal.Color iconColor = Terminal.Color.BLUE;

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
