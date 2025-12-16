package social_distancing.bfs;

import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * Precomputes which vertex pairs violate the distance constraint.
 * forbidden[u][v] is true iff dist_G(u, v) ≤ D.
 */
public class Forbidden {

    public static boolean[][] compute(int[][] graph, int D) {

        int n = graph.length;
        boolean[][] forbidden = new boolean[n][n];

        // Run a bounded BFS from each vertex to identify all vertices within distance D
        for (int start = 0; start < n; start++) {

            int[] dist = new int[n];
            Arrays.fill(dist, -1);
            dist[start] = 0;

            ArrayDeque<Integer> q = new ArrayDeque<>();
            q.add(start);

            while (!q.isEmpty()) {
                int u = q.poll();

                // We only care whether dist(start, u) ≤ D, so exploration stops at depth D
                if (dist[u] == D) continue;

                for (int v : graph[u]) {
                	// If v has not been reached before, this first path via u gives its minimal distance dist[u] + 1
                    if (dist[v] == -1) {
                        dist[v] = dist[u] + 1;
                        q.add(v);
                    }
                }
            }

            // Mark exactly those vertices whose shortest-path distance from start is ≤ D
            for (int v = 0; v < n; v++) {
                if (dist[v] != -1 && dist[v] <= D)
                    forbidden[start][v] = true;
            }
        }

        return forbidden;
    }
}
