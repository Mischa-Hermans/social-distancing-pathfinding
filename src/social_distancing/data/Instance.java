package social_distancing.data;

import java.io.*;
import java.util.*;

/**
 * Represents a single problem instance:
 * - Graph adjacency list
 * - Start/target positions for A and B
 * - Time limit T and distance constraint D
 */
public class Instance {

    public final int[][] graph;
    public final int sA, tA, sB, tB;
    public final int T, D;

    public Instance(int[][] graph, int sA, int tA, int sB, int tB, int T, int D) {
        this.graph = graph;
        this.sA = sA;
        this.tA = tA;
        this.sB = sB;
        this.tB = tB;
        this.T = T;
        this.D = D;
    }

    public static Instance load(String path) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(path));
        ArrayList<String> lines = new ArrayList<>();

        for (String s = br.readLine(); s != null; s = br.readLine()) {
            s = s.trim();
            if (!s.isEmpty()) lines.add(s);
        }
        br.close();

        if (lines.size() < 2)
            throw new IOException("Input must contain at least two non-empty lines.");

        // First line: n m T D
        String[] head = lines.get(0).split("\\s+");
        int n = Integer.parseInt(head[0]);
        int m = Integer.parseInt(head[1]);
        int T = Integer.parseInt(head[2]);
        int D = Integer.parseInt(head[3]);

        // Second line: sA tA sB tB  (1-based in file)
        String[] st = lines.get(1).split("\\s+");
        int sA = Integer.parseInt(st[0]) - 1;
        int tA = Integer.parseInt(st[1]) - 1;
        int sB = Integer.parseInt(st[2]) - 1;
        int tB = Integer.parseInt(st[3]) - 1;

        // Validate number of edge lines
        if (lines.size() != 2 + m)
            throw new IOException("Expected " + m + " edge lines but got " + (lines.size() - 2));

        // Build adjacency list using ArrayList
        ArrayList<Integer>[] tmp = new ArrayList[n];
        for (int i = 0; i < n; i++) tmp[i] = new ArrayList<>();

        for (int i = 2; i < 2 + m; i++) {
            String[] parts = lines.get(i).split("\\s+");
            int a = Integer.parseInt(parts[0]) - 1;
            int b = Integer.parseInt(parts[1]) - 1;
            tmp[a].add(b);
            tmp[b].add(a);
        }

        int[][] graph = new int[n][];
        for (int i = 0; i < n; i++) {
            graph[i] = tmp[i].stream().mapToInt(x -> x).toArray();
        }

        return new Instance(graph, sA, tA, sB, tB, T, D);
    }

}
