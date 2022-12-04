public class Main {
    public static void main(String[] args) {
        Game game = new Game(false);
        while (true) {
            if (!game.start()) break;
        }
    }
}