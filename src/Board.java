import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.Math.max;
import static java.lang.Math.min;

public final class Board {
    private final Cell[][] cells;
    private Color playerColor;
    private Color enemyColor;
    private ArrayList<Coords> validCells;

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

    Board(Board other) {
        cells = new Cell[8][8];
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                cells[i][j] = new Cell(other.cells[i][j]);
            }
        }
        playerColor = other.playerColor;
        enemyColor = other.enemyColor;
        validCells = other.validCells;
    }

    public static boolean isInBounds(Coords c) {
        return (c.x() >= 0) && (c.x() <= 7) && (c.y() >= 0) && (c.y() <= 7);
    }

    public WinnerInfo getBoardInfo() {
        int blackCount = 0;
        int whiteCount = 0;
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.color == Color.WHITE) {
                    ++whiteCount;
                } else if (cell.color == Color.BLACK) {
                    ++blackCount;
                }
            }
        }
        return new WinnerInfo(whiteCount > blackCount ? Color.WHITE : Color.BLACK,
                max(whiteCount, blackCount),
                min(whiteCount, blackCount));
    }

    public ArrayList<Coords> getValidCells() {
        return validCells;
    }

    private void flipDisk(Coords c) {
        cells[c.x()][c.y()].flipColor();
    }

    public void placeDisk(Coords c) {
        if (cells[c.x()][c.y()].color != Color.CAN_PLACE) {
            throw new IllegalArgumentException("Невозможно поставить фишку в данное место!");
        }
        cells[c.x()][c.y()].color = getPlayerColor();
        capture(c);
    }

    private void capture(Coords newDisk) {
        HashSet<Coords> toFlip = getCaptureList(newDisk);
        for (Coords disk : toFlip) {
            flipDisk(disk);
        }
    }

    public HashSet<Coords> getCaptureList(Coords attackDisk) {
        ArrayList<Coords> enemySells = findNearEnemySells(attackDisk);
        HashSet<Coords> toFlip = new HashSet<>();
        for (Coords enemyDisk : enemySells) {
            HashSet<Coords> current = new HashSet<>();
            int dx = enemyDisk.x() - attackDisk.x(), dy = enemyDisk.y() - attackDisk.y();
            if (dx == 0 && dy == 0) {
                continue;
            }
            int x_new = attackDisk.x(), y_new = attackDisk.y();
            while (isInBounds(new Coords(x_new += dx, y_new += dy))) {
                if (cells[x_new][y_new].color == Color.EMPTY || cells[x_new][y_new].color == Color.CAN_PLACE) {
                    break;
                }
                if (cells[x_new][y_new].color == getPlayerColor()) {
                    toFlip.addAll(current);
                    break;
                }
                current.add(new Coords(x_new, y_new));
            }
        }
        return toFlip;
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
                if (cells[i][j].color == Color.WHITE || cells[i][j].color == Color.BLACK) {
                    continue;
                }
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
        if (enemySells.isEmpty()) {
            return false;
        }
        for (Coords e : enemySells) {
            int dx = e.x() - c.x(), dy = e.y() - c.y();
            if (dx == 0 && dy == 0) {
                continue;
            }
            int x_new = c.x(), y_new = c.y();
            while (isInBounds(new Coords(x_new += dx, y_new += dy))) {
                if (cells[x_new][y_new].color == Color.EMPTY || cells[x_new][y_new].color == Color.CAN_PLACE) {
                    break;
                }
                if (cells[x_new][y_new].color == getPlayerColor()) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<Coords> findNearEnemySells(Coords c) {
        ArrayList<Coords> res = new ArrayList<>();
        for (int dx : new int[]{-1, 0, 1}) {
            for (int dy : new int[]{-1, 0, 1}) {
                int x_new = c.x() + dx, y_new = c.y() + dy;
                if ((dx == 0) && (dy == 0) || !isInBounds(new Coords(x_new, y_new))) {
                    continue;
                }
                if (cells[x_new][y_new].color == getEnemyColor()) {
                    res.add(new Coords(x_new, y_new));
                }
            }
        }
        return res;
    }

    public void render() {
        System.out.println();
        System.out.println();
        System.out.println();
        findValidMoves();
        System.out.println("● - белые, ◯ - черные, × - доступные для хода клетки");

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
