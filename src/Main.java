import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        while (true) {
            System.out.println("Играть с компьютером? [y/n]");
            Scanner sc = new Scanner(System.in);
            String word = sc.next().toLowerCase();
            if (!game.start(word.isEmpty() || word.charAt(0) == 'y')) {
                break;
            }
        }
        System.out.printf("Лучший результат: %d очков", game.bestScore);
    }
}