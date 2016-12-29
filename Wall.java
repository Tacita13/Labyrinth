
package labyrinth;

/**
 *
 * @author Isamar
 */
//Class to implement an object type wall that is in charge of setting the character/unicode and the id 
public class Wall {

    public static char icon = '\u25b2';
    public static char iconId = '0';

    public void setIcon(char ic) {
        icon = ic;
    }

    public void setId(char id) {
        iconId = id;
    }
}
