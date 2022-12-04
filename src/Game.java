import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public final class Game {
    private static final String UD_COORDS = "87654321", LR_COORDS = "abcdefgh";
    private final boolean useAI;
    private final Stack<Board> boards;
    private final HumanPlayer humanPlayer;
    private AIPlayer aiPlayer;

    Game(boolean useAI) {
        boards = new Stack<>();
        boards.push(new Board());

        humanPlayer = new HumanPlayer();
        this.useAI = useAI;
        if (this.useAI) {
            aiPlayer = new AIPlayer();
        }
    }

    private boolean rollback() {
        if (boards.size() <= 1) return false;
        boards.pop();
        return true;
    }

    private void addBoard(Board board) {
        this.boards.push(new Board(board));
    }

    public Board getBoard() {
        return boards.peek();
    }

    public boolean makeMove() {
        Board currentBoard = new Board(getBoard());
        currentBoard.render();
        System.out.printf("Ход %s!\n", (currentBoard.getPlayerColor() == Color.BLACK ? "черных" : "белых"));
        boolean moveAvailable;
        if (getBoard().getPlayerColor() == Color.WHITE || !useAI) moveAvailable = humanPlayer.makeMove(currentBoard);
        else moveAvailable = aiPlayer.makeMove(currentBoard);
        return moveAvailable;
    }

    public boolean start() {
        while (true) {
            if (makeMove()) continue;
            if (makeMove()) continue;
            WinnerInfo w = getBoard().getBoardInfo();
            System.out.printf("Игра окончена. Победили %s со счетом %d:%d.\n", w.winnerName(), w.winnerPoints(), w.loserPoints());
            System.out.println("Сыграть еще раз? [y/n]");
            Scanner sc = new Scanner(System.in);
            String word = sc.next().toLowerCase();
            return word.isEmpty() || word.charAt(0) == 'y';
        }
    }

    public abstract static class Player {
        public abstract boolean makeMove(Board currentBoard);
    }

    public final class HumanPlayer extends Player {
        @Override
        public boolean makeMove(Board currentBoard) {
            Coords disk;
            while (true) {
                boolean canMakeMove = true;
                if (currentBoard.getValidCells().isEmpty()) {
                    System.out.println("Невозможно поставить фишку. Ход переходит к следующему игроку.");
                    canMakeMove = false;
                }
                int i = 0;
                for (Coords cur : currentBoard.getValidCells()) {
                    System.out.printf("%d) %s%s\t", ++i, LR_COORDS.charAt(cur.y()), UD_COORDS.charAt(cur.x()));
                }
                System.out.println("Введите координаты, на которые хотите поставить фишку (например, d6), или отмените ход, введя u:");
                Scanner sc = new Scanner(System.in);
                String word = sc.next();
                if (Objects.equals(word, "u")) {
                    if (useAI) rollback();
                    if (!rollback()) {
                        System.out.println("Нельзя отменить ход!");
                        canMakeMove = false;
                    } else {
                        return true;
                    }
                }
                if (!canMakeMove) {
                    currentBoard.setPlayerColor(currentBoard.getEnemyColor());
                    return false;
                }

                if (word.length() != 2) {
                    System.out.println("Неверный ввод. Попробуйте еще раз");
                    continue;
                }

                disk = new Coords(UD_COORDS.indexOf(word.charAt(1)), LR_COORDS.indexOf(word.charAt(0)));
                if (!Board.isInBounds(disk) || !currentBoard.getValidCells().contains(disk)) {
                    System.out.println("Неверный ввод. Попробуйте еще раз");
                    continue;
                }
                break;
            }
            currentBoard.placeDisk(disk);
            currentBoard.setPlayerColor(currentBoard.getEnemyColor());
            addBoard(currentBoard);
            return true;
        }
    }

    public final class AIPlayer extends Player {
        private double getR(Coords attackDiskCoords, HashSet<Coords> toCapture) {
            double result = getSS(attackDiskCoords);
            for (Coords c : toCapture) {
                result += getSi(c);
            }
            return result;
        }

        private double getSi(Coords c) {
            if (isEdgeCell(c)) return 2;
            return 1;
        }

        private double getSS(Coords c) {
            if (isEdgeCell(c)) return 0.8;
            if (isCornerCell(c)) return 0.4;
            return 0;
        }

        private boolean isEdgeCell(Coords c) {
            return (c.x() == 0) || (c.x() == 7) || (c.y() == 0) || (c.y() == 7);
        }

        private boolean isCornerCell(Coords c) {
            return ((c.x() == 0) && (c.y() == 0)) || ((c.x() == 7) && (c.y() == 0))
                    || ((c.x() == 0) && (c.y() == 7)) || ((c.x() == 7) && (c.y() == 7));
        }

        @Override
        public boolean makeMove(Board currentBoard) {
            if (currentBoard.getValidCells().isEmpty()) {
                System.out.println("Невозможно поставить фишку. Ход переходит к следующему игроку.");
                currentBoard.setPlayerColor(currentBoard.getEnemyColor());
                return false;
            }
            double R = -1;
            Coords bestCell = null;
            for (Coords cur : currentBoard.getValidCells()) {
                double currentR = getR(cur, currentBoard.getCaptureList(cur));
                if (currentR > R) {
                    R = currentR;
                    bestCell = cur;
                }
            }
            if (bestCell == null) {
                System.out.println("Невозможно поставить фишку. Ход переходит к следующему игроку.");
                return false;
            }
            currentBoard.placeDisk(bestCell);
            currentBoard.setPlayerColor(currentBoard.getEnemyColor());
            addBoard(currentBoard);
            return true;
        }
    }
}
