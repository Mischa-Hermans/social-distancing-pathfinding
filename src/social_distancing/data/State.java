package social_distancing.data;

/**
 * State of the alternating BFS, consisting of the positions of both players
 * and an indicator of whose turn it is to move next.
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
