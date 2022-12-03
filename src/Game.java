public class Game {
    Board board;
    public void render() {
        board.render();
    }
    Game() {
        board = new Board();
    }
}
