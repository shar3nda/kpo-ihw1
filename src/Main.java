public class Main {
    public static void main(String[] args) {
        Game game = new Game(true);
        while (true) {
            if (!game.start()) break;
        }
    }
}