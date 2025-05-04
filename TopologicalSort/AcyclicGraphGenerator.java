import java.io.*;
import java.util.*;

public class AcyclicGraphGenerator {
    public static void main(String[] args) {
        int i = 1;
        Random random = new Random();

        while (i < 51) {
            String filename = "test" + i + ".txt";
            int numberV = random.nextInt(3901) + 100; // 100-4000
            int minE = (int) Math.round(numberV / 1.5);
            int maxE = (int) Math.round(numberV * 1.5);
            int numberE = random.nextInt(maxE - minE + 1) + minE;

            try {
                generateAcyclicGraphFile(filename, numberV, numberE);
                i++;
            } catch (IllegalArgumentException e) {
                // Пропускаем если создается цикл
                continue;
            } catch (IOException e) {
                System.err.println("Ошибка при записи файла: " + e.getMessage());
                break;
            }
        }
    }

    public static void generateAcyclicGraphFile(String filename, int numberV, int numberE)
            throws IOException, IllegalArgumentException {
        List<Integer> listV = new ArrayList<>();
        for (int i = 1; i <= numberV; i++) {
            listV.add(i);
        }

        List<Edge> listE = new ArrayList<>();
        Random random = new Random();

        while (listE.size() < numberE) {
            int v1 = listV.get(random.nextInt(numberV));
            int v2 = listV.get(random.nextInt(numberV));

            if (v1 != v2 && !containsEdge(listE, v1, v2)) {
                if (createsCycle(listE, v1, v2)) {
                    throw new IllegalArgumentException(
                            "Добавление ребра (" + v1 + ", " + v2 + ") создает цикл");
                }
                listE.add(new Edge(v1, v2));
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(numberV + " " + numberE);
            for (Edge e : listE) {
                writer.println(e.v1 + " " + e.v2);
            }
        }
    }

    private static boolean createsCycle(List<Edge> edges, int v1, int v2) {
        Set<Integer> visited = new HashSet<>();
        return dfs(edges, visited, v1, v2);
    }

    private static boolean dfs(List<Edge> edges, Set<Integer> visited, int current, int target) {
        if (current == target) {
            return true;
        }

        visited.add(current);

        for (Edge e : edges) {
            if (e.v1 == current && !visited.contains(e.v2)) {
                if (dfs(edges, visited, e.v2, target)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean containsEdge(List<Edge> edges, int v1, int v2) {
        for (Edge e : edges) {
            if ((e.v1 == v1 && e.v2 == v2) || (e.v1 == v2 && e.v2 == v1)) {
                return true;
            }
        }
        return false;
    }

    static class Edge {
        int v1;
        int v2;

        public Edge(int v1, int v2) {
            this.v1 = v1;
            this.v2 = v2;
        }
    }
}