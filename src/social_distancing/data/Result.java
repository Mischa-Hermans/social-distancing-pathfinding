package social_distancing.data;

/**
 * Stores the outcome of the alternating BFS.
 * k is the minimum number of full steps needed to reach the targets.
 * dist and parent pointers are used only to reconstruct the paths.
 *
 * If no solution exists within T steps, k = T + 1 and all arrays are null.
 */
public class Result {

    public final int k;
    public final long[][][] dist;
    public final int[][][] pu;
    public final int[][][] pv;
    public final int[][][] pturn;


    // Successful BFS results.
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

    // Failure case
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
