import java.lang.reflect.Array;

public class Cell {
    Color color;
    void flipColor() {
        if (color == Color.WHITE)
            color = Color.BLACK;
        else if (color == Color.BLACK)
            color = Color.WHITE;
    }
    Cell() {
        color = Color.EMPTY;
    }
    Cell(Color color) {
        this.color = color;
    }
    @Override
    public String toString() {
        if (color == Color.BLACK)
            return "◯";
        else if (color == Color.WHITE)
            return "●";
        else if (color == Color.CAN_PLACE)
            return "◌";
        return " ";
    }
}
