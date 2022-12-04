import java.util.ArrayList;

public class Board {
    private final Cell[][] cells;
    private Color playerColor;
    private Color enemyColor;
    private ArrayList<Coords> validCells;
    public ArrayList<Coords> getValidCells() {
        return validCells;
    }
    public void placeDisk(int x, int y) {
        if (cells[x][y].color != Color.CAN_PLACE) {
            throw new IllegalArgumentException("Невозможно поставить фишку в данное место!");
        }
        cells[x][y].color = getPlayerColor();
    }

    Board() {
        setPlayerColor(Color.BLACK);

        cells = new Cell[8][8];
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                cells[i][j] = new Cell();
            }
        }
        cells[3][3] = new Cell(Color.WHITE);
        cells[4][4] = new Cell(Color.WHITE);
        cells[3][4] = new Cell(Color.BLACK);
        cells[4][3] = new Cell(Color.BLACK);
    }

    private static boolean isInBounds(Coords c) {
        return (c.x() >= 0) && (c.x() <= 7) && (c.y() >= 0) && (c.y() <= 7);
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color color) {
        playerColor = color;
        enemyColor = (playerColor == Color.WHITE ? Color.BLACK : Color.WHITE);
    }

    public Color getEnemyColor() {
        return enemyColor;
    }

    private void findValidMoves() {
        validCells = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (cells[i][j].color == Color.WHITE || cells[i][j].color == Color.BLACK) continue;
                if (canCapture(new Coords(i, j))) {
                    cells[i][j].color = Color.CAN_PLACE;
                    validCells.add(new Coords(i, j));
                } else {
                    cells[i][j].color = Color.EMPTY;
                }
            }
        }

    }

    private boolean canCapture(Coords c) {
        ArrayList<Coords> enemySells = findNearEnemySells(c);
        if (enemySells.isEmpty()) return false;
        for (Coords e : enemySells) {
            int dx = e.x() - c.x(), dy = e.y() - c.y();
            if (dx == 0 && dy == 0) continue;
            int x_new = c.x(), y_new = c.y();
            while (isInBounds(new Coords(x_new += dx, y_new += dy))) {
                if (cells[x_new][y_new].color == getPlayerColor()) return true;
            }
        }
        return false;
    }

    private ArrayList<Coords> findNearEnemySells(Coords c) {
        ArrayList<Coords> res = new ArrayList<>();
        for (int dx : new int[]{-1, 0, 1}) {
            for (int dy : new int[]{-1, 0, 1}) {
                int x_new = c.x() + dx, y_new = c.y() + dy;
                if ((dx == 0) && (dy == 0) || !isInBounds(new Coords(x_new, y_new))) continue;
                if (cells[x_new][y_new].color == getEnemyColor()) res.add(new Coords(x_new, y_new));
            }
        }
        return res;
    }

    public void render() {
        findValidMoves();
        System.out.println("● - белые, ◯ - черные, ◌ - доступные для хода клетки");

        System.out.println("  ┏━━━┯━━━┯━━━┯━━━┯━━━┯━━━┯━━━┯━━━┓");
        for (int i = 0; i < 8; ++i) {
            System.out.printf("%d ┃ ", 8 - i);
            for (int j = 0; j < 8; ++j) {
                System.out.printf("%s%s", cells[i][j], (j != 7 ? " │ " : " ┃\n"));
            }
            if (i != 7) {
                System.out.println("  ┠───┼───┼───┼───┼───┼───┼───┼───┨");
            }
        }
        System.out.println("  ┗━━━┷━━━┷━━━┷━━━┷━━━┷━━━┷━━━┷━━━┛");
        System.out.println("    a   b   c   d   e   f   g   h  ");
    }
}
