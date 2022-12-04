public class Cell {
    Color color;

    Cell() {
        color = Color.EMPTY;
    }

    Cell(Color color) {
        this.color = color;
    }

    void flipColor() {
        if (color == Color.WHITE)
            color = Color.BLACK;
        else if (color == Color.BLACK)
            color = Color.WHITE;
    }

    @Override
    public String toString() {
        if (color == Color.BLACK)
            return "◯";
        else if (color == Color.WHITE)
            return "●";
        else if (color == Color.CAN_PLACE)
            return "×";
        return " ";
    }
}
