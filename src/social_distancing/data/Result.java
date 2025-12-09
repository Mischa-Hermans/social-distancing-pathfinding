package social_distancing.data;

/**
 * Holds the complete result of the alternating BFS:
 * - k = minimal number of full steps (A-moves) needed to reach the goal
 * - dist[u][v][turn] = minimal half-step distance to (u, v, turn)
 * - pu, pv, pturn store parent pointers for path reconstruction.
 *
 * If no valid solution exists within T, k = T+1 and dist = null.
 */
public class Result {

    public final int k;
    public final long[][][] dist;
    public final int[][][] pu;
    public final int[][][] pv;
    public final int[][][] pturn;

    /**
     * Successful BFS results.
     */
    public Result(int k,
                  long[][][] dist,
                  int[][][] pu,
                  int[][][] pv,
                  int[][][] pturn)
    {
        this.k = k;
        this.dist = dist;
        this.pu = pu;
        this.pv = pv;
        this.pturn = pturn;
    }

    /**
     * Failure case
     */
    private Result(int k) {
        this.k = k;
        this.dist = null;
        this.pu = null;
        this.pv = null;
        this.pturn = null;
    }

    public static Result failure(int k) {
        return new Result(k);
    }
}
