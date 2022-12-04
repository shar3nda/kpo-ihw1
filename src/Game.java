import java.util.Scanner;

public class Game {
    private static final String UD_COORDS = "87654321", LR_COORDS = "abcdefgh";

    Board board;

    boolean AI;

    Game(boolean useAI) {
        board = new Board();
        AI = useAI;
    }

    public boolean makeMove() {
        // TODO implement AI and capturing
        board.render();
        System.out.printf("Ход %s!\n", (board.getPlayerColor() == Color.BLACK ? "черных" : "белых"));
        System.out.println("Введите координаты, на которые хотите поставить фишку (например, d6):");
        Coords disk;
        while (true) {
            int i = 0;
            if (board.getValidCells().isEmpty()) {
                System.out.println("Невозможно поставить фишку. Ход переходит к следующему игроку.");
                board.setPlayerColor(board.getEnemyColor());
                return false;
            }
            for (Coords cur : board.getValidCells()) {
                System.out.printf("%d) %s%s\t", ++i, LR_COORDS.charAt(cur.y()), UD_COORDS.charAt(cur.x()));
            }
            Scanner sc = new Scanner(System.in);
            String word = sc.next();
            if (word.length() != 2) {
                System.out.println("Неверный ввод. Попробуйте еще раз");
                continue;
            }

            disk = new Coords(UD_COORDS.indexOf(word.charAt(1)), LR_COORDS.indexOf(word.charAt(0)));
            if (!Board.isInBounds(disk) || !board.getValidCells().contains(disk)) {
                System.out.println("Неверный ввод. Попробуйте еще раз");
                continue;
            }
            break;
        }
        board.placeDisk(disk);
        board.setPlayerColor(board.getEnemyColor());
        return true;
    }

    public boolean start() {
        while (true) {
            if (makeMove()) continue;
            if (makeMove()) continue;
            WinnerInfo w = board.getBoardInfo();
            System.out.printf("Игра окончена. Победили %s со счетом %d:%d.\n",
                    w.winnerName(), w.winnerPoints(), w.loserPoints());
            System.out.println("Сыграть еще раз? [y/n]");
            Scanner sc = new Scanner(System.in);
            String word = sc.next().toLowerCase();
            return word.isEmpty() || word.charAt(0) == 'y';
        }
    }
}
