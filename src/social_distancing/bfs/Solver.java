package social_distancing.bfs;

import java.util.ArrayDeque;
import java.util.ArrayList;

import social_distancing.data.Instance;
import social_distancing.data.Result;
import social_distancing.data.Paths;
import social_distancing.data.State;

/**
 * Implements the alternating-move BFS in the product state space (u, v, turn).
 * Ensures that forbidden pairs (dist ≤ D) are avoided.
 */
public class Solver {

    private final int[][] graph;
    private final boolean[][] forbidden;

    public Solver(int[][] graph, int D) {
        this.graph = graph;
        this.forbidden = Forbidden.compute(graph, D);
    }

    /**
     * Run alternating BFS to find the minimum k such that
     * A reaches tA and B reaches tB without violating forbidden states.
     *
     * State = (u, v, turn)
     * turn = 0 → A moves
     * turn = 1 → B moves
     */
    public Result solve(int sA, int sB, int tA, int tB, int T) {

        int n = graph.length;

        // If initial configuration already violates distancing
        if (forbidden[sA][sB]) {
            return Result.failure(T + 1);
        }

        final long INF = Long.MAX_VALUE / 4;

        long[][][] dist = new long[n][n][2];
        int[][][] pu = new int[n][n][2];
        int[][][] pv = new int[n][n][2];
        int[][][] pturn = new int[n][n][2];

        // Initialize dist + parent pointers
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

            // Successful only after a full step → turn == 0
            if (u == tA && v == tB && turn == 0) {
                return new Result((int)(d / 2), dist, pu, pv, pturn);
            }

            if (turn == 0) {
                // A moves
                // A can stay
                int u2 = u;
                if (!forbidden[u2][v] && dist[u2][v][1] > d + 1) {
                    dist[u2][v][1] = d + 1;
                    pu[u2][v][1] = u;
                    pv[u2][v][1] = v;
                    pturn[u2][v][1] = turn;
                    q.add(new State(u2, v, 1));
                }
                // A can move to any neighbor
                for (int nxt : graph[u]) {
                    u2 = nxt;
                    if (!forbidden[u2][v] && dist[u2][v][1] > d + 1) {
                        dist[u2][v][1] = d + 1;
                        pu[u2][v][1] = u;
                        pv[u2][v][1] = v;
                        pturn[u2][v][1] = turn;
                        q.add(new State(u2, v, 1));
                    }
                }

            } else {
                // B moves
                // B can stay
                int v2 = v;
                if (!forbidden[u][v2] && dist[u][v2][0] > d + 1) {
                    dist[u][v2][0] = d + 1;
                    pu[u][v2][0] = u;
                    pv[u][v2][0] = v;
                    pturn[u][v2][0] = turn;
                    q.add(new State(u, v2, 0));
                }
                // B can move to any neighbor
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

        // No valid solution within T
        return Result.failure(T + 1);
    }


    /**
     * Reconstruct final paths for A and B
     * using parent-pointer arrays stored in the Result.
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

        // Reverse to chronological order
        java.util.Collections.reverse(seq);

        // Extract full-step states (turn == 0)
        ArrayList<Integer> pathA = new ArrayList<>();
        ArrayList<Integer> pathB = new ArrayList<>();

        for (int[] st : seq) {
            if (st[2] == 0) {
                pathA.add(st[0]);
                pathB.add(st[1]);
            }
        }

        return new Paths(pathA, pathB);
    }
}
