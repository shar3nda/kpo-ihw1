public class Board {
    private final Cell[][] playingField;
    private Color playerColor;
    private Color enemyColor;
    public void setPlayerColor(Color color) {
        playerColor = color;
        enemyColor = (playerColor == Color.WHITE ? Color.BLACK : Color.WHITE);
    }
    public Color getPlayerColor() {
        return playerColor;
    }
    public Color getEnemyColor() {
        return enemyColor;
    }

    private void findValidMoves() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (playingField[i][j].color == Color.WHITE || playingField[i][j].color == Color.BLACK)
                    continue;
                if (canCapture(i, j))
                    playingField[i][j].color = Color.CAN_PLACE;
                else
                    playingField[i][j].color = Color.EMPTY;
            }
        }

    }

    private boolean canCapture(int i, int j) {
        if (!isNearEnemyCell(i, j))
            return false;
        // TODO check if enemy disks can be captured
        return true;
    }

    private boolean isNearEnemyCell(int i, int j) {
        for (int dx = -1; dx < 2; ++dx) {
            for (int dy = -1; dy < 2; ++dy) {
                int new_i = i + dx, new_j = j + dy;
                if ((dx == 0) && (dy == 0) || !isInBounds(new_i, new_j))
                    continue;
                if (i == 2 && j == 2) {
                    if (playingField[new_i][new_j].color == getEnemyColor())
                        return true;
                }
                if (playingField[new_i][new_j].color == getEnemyColor())
                    return true;
            }
        }
        return false;
    }

    private static boolean isInBounds(int x, int y) {
        return (x >= 0) && (x <= 7) && (y >= 0) && (y <= 7);
    }

    public void render() {
        findValidMoves();
        System.out.println("● - белые, ◯ - черные, ◌ - доступные для хода клетки");
        System.out.printf("Ход %s!\n", (playerColor == Color.BLACK ? "черных" : "белых"));
        System.out.println("  ┏━━━┯━━━┯━━━┯━━━┯━━━┯━━━┯━━━┯━━━┓");
        for (int i = 0; i < 8; ++i) {
            System.out.printf("%d ┃ ", 8 - i);
            for (int j = 0; j < 8; ++j) {
                System.out.printf("%s%s", playingField[i][j], (j != 7 ? " │ " : " ┃\n"));
            }
            if (i != 7) {
                System.out.println("  ┠───┼───┼───┼───┼───┼───┼───┼───┨");
            }
        }
        System.out.println("  ┗━━━┷━━━┷━━━┷━━━┷━━━┷━━━┷━━━┷━━━┛");
        System.out.println("    a   b   c   d   e   f   g   h  ");
    }

    Board() {
        setPlayerColor(Color.BLACK);

        playingField = new Cell[8][8];
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                playingField[i][j] = new Cell();
            }
        }
        playingField[3][3] = new Cell(Color.WHITE);
        playingField[4][4] = new Cell(Color.WHITE);
        playingField[3][4] = new Cell(Color.BLACK);
        playingField[4][3] = new Cell(Color.BLACK);
    }
}
