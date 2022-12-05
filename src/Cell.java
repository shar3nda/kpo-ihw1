public final class Cell {
    Color color;

    Cell() {
        color = Color.EMPTY;
    }

    Cell(Cell cell) {
        color = switch (cell.color) {
            case BLACK -> Color.BLACK;
            case WHITE -> Color.WHITE;
            case CAN_PLACE -> Color.CAN_PLACE;
            default -> Color.EMPTY;
        };
    }

    Cell(Color color) {
        this.color = color;
    }

    void flipColor() {
        if (color == Color.WHITE) {
            color = Color.BLACK;
        } else if (color == Color.BLACK) {
            color = Color.WHITE;
        }
    }

    @Override
    public String toString() {
        return switch (color) {
            case BLACK -> "◯";
            case WHITE -> "●";
            case CAN_PLACE -> "×";
            default -> " ";
        };
    }
}
