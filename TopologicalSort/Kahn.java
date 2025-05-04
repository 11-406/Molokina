import java.io.*;
import java.util.*;

public class Kahn {

    static class Graph {
        private final int V;
        private final List<List<Integer>> adj;

        public Graph(int V) {
            this.V = V;
            adj = new ArrayList<>(V);
            for (int i = 0; i < V; i++) {
                adj.add(new LinkedList<>());
            }
        }

        public void addEdge(int v, int w) {
            // Проверяем допустимость индексов
            if (v < 0 || v >= V || w < 0 || w >= V) {
                throw new IllegalArgumentException(
                        String.format("Invalid edge: %d->%d (valid range 0-%d)", v, w, V-1));
            }
            adj.get(v).add(w);
        }

        public List<Integer> topologicalSort(Measurement measurement) {
            int[] inDegree = new int[V];

            // Вычисляем степени входа с проверкой индексов
            for (int u = 0; u < V; u++) {
                for (int v : adj.get(u)) {
                    if (v < 0 || v >= V) {
                        throw new IllegalStateException(
                                String.format("Invalid vertex %d in adjacency list of %d", v, u));
                    }
                    inDegree[v]++;
                    measurement.incrementIterations();
                }
            }

            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < V; i++) {
                if (inDegree[i] == 0) {
                    queue.add(i);
                }
                measurement.incrementIterations();
            }

            List<Integer> topOrder = new ArrayList<>();
            while (!queue.isEmpty()) {
                int u = queue.poll();
                topOrder.add(u);

                for (int v : adj.get(u)) {
                    if (--inDegree[v] == 0) {
                        queue.add(v);
                    }
                    measurement.incrementIterations();
                }
                measurement.incrementIterations();
            }

            if (topOrder.size() != V) {
                throw new IllegalStateException("Graph contains a cycle");
            }

            return topOrder;
        }
    }

    static class Measurement {
        private long startTime;
        private long endTime;
        private long iterations;

        public void start() {
            startTime = System.nanoTime();
            iterations = 0;
        }

        public void stop() {
            endTime = System.nanoTime();
        }

        public void incrementIterations() {
            iterations++;
        }

        public long getTimeNano() {
            return endTime - startTime;
        }

        public long getIterations() {
            return iterations;
        }
    }

    private static Graph readGraphFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] firstLine = reader.readLine().split(" ");
            int V = Integer.parseInt(firstLine[0]);
            int E = Integer.parseInt(firstLine[1]);

            Graph graph = new Graph(V);

            for (int i = 0; i < E; i++) {
                String[] edge = reader.readLine().split(" ");
                int v = Integer.parseInt(edge[0]);
                int w = Integer.parseInt(edge[1]);

                // Преобразуем 1-based индексы в 0-based
                int src = v - 1;
                int dest = w - 1;

                if (src < 0 || src >= V || dest < 0 || dest >= V) {
                    throw new IOException(
                            String.format("Invalid edge in file: %d->%d (valid range 1-%d)",
                                    v, w, V));
                }
                graph.addEdge(src, dest);
            }

            return graph;
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <input-file>");
            return;
        }

        try {
            Graph graph = readGraphFromFile(args[0]);
            Measurement measurement = new Measurement();

            measurement.start();
            List<Integer> sortedOrder = graph.topologicalSort(measurement);
            measurement.stop();

            // Преобразуем обратно в 1-based индексы для вывода
            List<Integer> oneBasedOrder = new ArrayList<>();
            for (int vertex : sortedOrder) {
                oneBasedOrder.add(vertex + 1);
            }

            System.out.println("Topological order (Kahn's algorithm):");
            System.out.println(oneBasedOrder);
            System.out.println("Execution time (ns): " + measurement.getTimeNano());
            System.out.println("Iterations count: " + measurement.getIterations());

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Graph error: " + e.getMessage());
        }
    }
}