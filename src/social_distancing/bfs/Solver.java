package social_distancing.bfs;

import java.util.ArrayDeque;
import java.util.ArrayList;

import social_distancing.data.Instance;
import social_distancing.data.Result;
import social_distancing.data.Paths;
import social_distancing.data.State;

/**
 * Alternating BFS over the product state space (u, v, turn),
 * where turn indicates which player moves next. 
 * Ensures that forbidden pairs (dist ≤ D) are avoided.
 */
public class Solver {

    private final int[][] graph;
    private final boolean[][] forbidden;

    public Solver(int[][] graph, int D) {
        this.graph = graph;
        // Precompute forbidden vertex pairs to allow O(1) distance checks during BFS
        this.forbidden = Forbidden.compute(graph, D);
    }

    /**
     * Finds the minimum number of full steps k such that both players
     * reach their targets without ever being in a forbidden state.
     */
    public Result solve(int sA, int sB, int tA, int tB, int T) {

        int n = graph.length;

        if (forbidden[sA][sB]) {
            return Result.failure(T + 1);
        }

        final long INF = Long.MAX_VALUE / 4;
        
	     // dist[u][v][turn]: minimum half-steps to reach (u, v, turn)
	     // pu, pv, pturn store the previous (u, v, turn) state for reconstruction
	     long[][][] dist = new long[n][n][2];
	     int[][][] pu = new int[n][n][2];
	     int[][][] pv = new int[n][n][2];
	     int[][][] pturn = new int[n][n][2];


        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                dist[u][v][0] = INF;
                dist[u][v][1] = INF;
                pu[u][v][0] = pu[u][v][1] = -1;
            }
        }

        // Starting state: A at sA, B at sB, A moves first
        dist[sA][sB][0] = 0;

        ArrayDeque<State> q = new ArrayDeque<>();
        q.add(new State(sA, sB, 0));

        while (!q.isEmpty()) {
            State st = q.poll();
            int u = st.u;
            int v = st.v;
            int turn = st.turn;

            long d = dist[u][v][turn];

            // Stop search beyond 2T half-steps
            if (d > 2L * T) continue;

            // Only accept a solution after both players have completed a full step
            if (u == tA && v == tB && turn == 0) {
                return new Result((int)(d / 2), dist, pu, pv, pturn);
            }

            if (turn == 0) {
            	// A moves; no distance check needed since B has not moved yet
            	
                // A stays
                int u2 = u;
                if (dist[u2][v][1] > d + 1) {
                    dist[u2][v][1] = d + 1;
                    pu[u2][v][1] = u;
                    pv[u2][v][1] = v;
                    pturn[u2][v][1] = turn;
                    q.add(new State(u2, v, 1));
                }
                
                // A moves to a neighbor
                for (int nxt : graph[u]) {
                    u2 = nxt;
                    if (dist[u2][v][1] > d + 1) {
                        dist[u2][v][1] = d + 1;
                        pu[u2][v][1] = u;
                        pv[u2][v][1] = v;
                        pturn[u2][v][1] = turn;
                        q.add(new State(u2, v, 1));
                    }
                }

            } else {
            	// B moves; distance constraint must be enforced after B’s move
            	
                // B stays
                int v2 = v;
                if (!forbidden[u][v2] && dist[u][v2][0] > d + 1) {
                    dist[u][v2][0] = d + 1;
                    pu[u][v2][0] = u;
                    pv[u][v2][0] = v;
                    pturn[u][v2][0] = turn;
                    q.add(new State(u, v2, 0));
                }
                
                // B moves to a neighbor
                for (int nxt : graph[v]) {
                    v2 = nxt;
                    if (!forbidden[u][v2] && dist[u][v2][0] > d + 1) {
                        dist[u][v2][0] = d + 1;
                        pu[u][v2][0] = u;
                        pv[u][v2][0] = v;
                        pturn[u][v2][0] = turn;
                        q.add(new State(u, v2, 0));
                    }
                }
            }
        }

        // No valid solution within T full steps
        return Result.failure(T + 1);
    }

    /**
     * Reconstructs the paths by following parent pointers
     * and keeping only states after full steps (turn == 0).
     */
    public Paths reconstruct(Result r, Instance ins) {

        if (r.dist == null) return null;

        int tA = ins.tA;
        int tB = ins.tB;

        int u = tA, v = tB, turn = 0;

        ArrayList<int[]> seq = new ArrayList<>();

        // Follow parent pointers backward through states
        while (true) {
            seq.add(new int[]{u, v, turn});

            int puv = r.pu[u][v][turn];
            if (puv == -1) break;

            int pvv = r.pv[u][v][turn];
            int pt  = r.pturn[u][v][turn];

            u = puv;
            v = pvv;
            turn = pt;
        }

        java.util.Collections.reverse(seq);

        ArrayList<Integer> pathA = new ArrayList<>();
        ArrayList<Integer> pathB = new ArrayList<>();

        // Only states after B’s move correspond to completed time steps
        for (int[] st : seq) {
            if (st[2] == 0) {
                pathA.add(st[0]);
                pathB.add(st[1]);
            }
        }

        return new Paths(pathA, pathB);
    }
}
