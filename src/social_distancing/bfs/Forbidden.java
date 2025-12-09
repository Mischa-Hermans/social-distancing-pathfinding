package social_distancing.bfs;

import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * Computes the forbidden-pair matrix: forbidden[u][v] = true
 * if the graph-distance between u and v is ≤ D.
 *
 * This is used to enforce the distancing rule during alternating BFS.
 */
public class Forbidden {

    public static boolean[][] compute(int[][] graph, int D) {

        int n = graph.length;
        boolean[][] forbidden = new boolean[n][n];

        // BFS from every vertex to compute all reachable nodes within distance D
        for (int start = 0; start < n; start++) {

            int[] dist = new int[n];
            Arrays.fill(dist, -1);
            dist[start] = 0;

            ArrayDeque<Integer> q = new ArrayDeque<>();
            q.add(start);

            while (!q.isEmpty()) {
                int u = q.poll();

                // Do not explore beyond D levels
                if (dist[u] == D) continue;

                for (int v : graph[u]) {
                    if (dist[v] == -1) {
                        dist[v] = dist[u] + 1;
                        q.add(v);
                    }
                }
            }

            // Mark all vertices within D as forbidden pairs (u = start)
            for (int v = 0; v < n; v++) {
                if (dist[v] != -1 && dist[v] <= D)
                    forbidden[start][v] = true;
            }
        }

        return forbidden;
    }
}
