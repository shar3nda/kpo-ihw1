import java.util.Scanner;

public class Game {
    private static final String UD_COORDS = "87654321", LR_COORDS = "abcdefgh";

    Board board;

    Game() {
        board = new Board();
    }

    public void makeMove() {
        // TODO implement AI and capturing
        System.out.printf("Ход %s!\n", (board.getPlayerColor() == Color.BLACK ? "черных" : "белых"));
        board.render();
        System.out.println("Введите координаты, на которые хотите поставить фишку (например, d6):");
        int x, y;
        while (true) {
            int i = 0;
            for (Coords c : board.getValidCells()) {
                System.out.printf("%d) %s%s\t", ++i, LR_COORDS.charAt(c.y()), UD_COORDS.charAt(c.x()));
            }
            Scanner sc = new Scanner(System.in);
            String word = sc.next();
            if (word.length() != 2) {
                System.out.println("Неверный ввод. Попробуйте еще раз");
                continue;
            }

            x = UD_COORDS.indexOf(word.charAt(1));
            y = LR_COORDS.indexOf(word.charAt(0));
            if (x == -1 || y == -1 || !board.getValidCells().contains(new Coords(x, y))) {
                System.out.println("Неверный ввод. Попробуйте еще раз");
                continue;
            }
            break;
        }
        board.placeDisk(x, y);
        board.setPlayerColor(board.getEnemyColor());
    }

    public void start() {
        while (true) {
            makeMove();
        }
    }
}
