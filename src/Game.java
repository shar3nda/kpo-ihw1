import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public class Game {
    private static final String UD_COORDS = "87654321", LR_COORDS = "abcdefgh";
    private final boolean AI;
    private final Stack<Board> boards;

    Game(boolean useAI) {
        boards = new Stack<>();
        boards.push(new Board());
        AI = useAI;
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
        // TODO implement AI
        Board currentBoard = new Board(getBoard());
        currentBoard.render();
        System.out.printf("Ход %s!\n", (currentBoard.getPlayerColor() == Color.BLACK ? "черных" : "белых"));
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

    public boolean start() {
        while (true) {
            if (makeMove()) continue;
            if (makeMove()) continue;
            WinnerInfo w = getBoard().getBoardInfo();
            System.out.printf("Игра окончена. Победили %s со счетом %d:%d.\n",
                    w.winnerName(), w.winnerPoints(), w.loserPoints());
            System.out.println("Сыграть еще раз? [y/n]");
            Scanner sc = new Scanner(System.in);
            String word = sc.next().toLowerCase();
            return word.isEmpty() || word.charAt(0) == 'y';
        }
    }
}
