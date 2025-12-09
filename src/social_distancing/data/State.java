package social_distancing.data;

/**
 * Represents a BFS state in the product space (u, v, turn):
 *
 * - u : position of player A
 * - v : position of player B
 * - turn : 0 if A moves next, 1 if B moves next
 *
 * This is used only to store queue elements during alternating BFS.
 */
public class State {

    public final int u;
    public final int v;
    public final int turn;

    public State(int u, int v, int turn) {
        this.u = u;
        this.v = v;
        this.turn = turn;
    }
}
