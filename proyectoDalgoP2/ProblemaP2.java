//Autores: 1. Nicolas Hernandez - 202322148
//         2. William Pollock - 202221321

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class ProblemaP2 {
    
    static final int inf = Integer.MAX_VALUE/2; //se divide por dos para que no haya overflow

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuilder out = new StringBuilder();

        int casos = sc.nextInt();
        for (int c = 0; c < casos; c++){
            int n = sc.nextInt();
            int E = sc.nextInt();
            sc.nextLine();
            
            boolean[] robot = new boolean[n+1];

            String linea = sc.nextLine().trim();
            if (!linea.isEmpty()){
                for (String token: linea.split(" ")){
                    if (!token.isEmpty()){
                        robot[Integer.parseInt(token)] = true;
                    }
                }
            }

            int[] poder = new int[n+1];
            linea = sc.nextLine().trim();
            if (!linea.isEmpty()){
                String[] token = linea.split(" ");
                for (int i = 0; i + 1 < token.length; i += 2){
                    int p = Integer.parseInt(token[i]);
                    int k = Integer.parseInt(token[i +1]);
                    poder[p] = k;
                }
            }

            String resultado = solucion(n, E, robot, poder);
            out.append(resultado).append('\n');
        }
        System.out.print(out.toString());
    }

    private static String solucion(int n, int E, boolean[] robot, int[] poder){
        TreeSet<Integer> libres = new TreeSet<>();
        for (int i = 1;i<n; i++){
            if (!robot[i]) libres.add(i);
        }
        int[] mejorEnergia = new int[n+1];
        Arrays.fill(mejorEnergia, inf);
        int[] parent = new int[n+1];
        String[] acc = new String[n+1];
        int[] numAcciones = new int[n+1];

        ArrayDeque<Integer> adq = new ArrayDeque<>();
        mejorEnergia[0] = 0;
        parent[0] = -1;
        adq.add(0);

        while (!adq.isEmpty()){
            int u = adq.poll();
            if (u==n) return reconstruirRuta(parent, acc, n);
            int ene = mejorEnergia[u];
            int sigAccion = numAcciones[u] +1;

            for (int dir = -1; dir <= 1; dir +=2){
                int v = u + dir;
                if (v >= 0 && v <= n && !robot[v] && ene < mejorEnergia[v]){
                    mejorEnergia[v] = ene;
                    parent[v] = u;
                    acc[v] = dir == 1 ? "C+" : "C-";
                    numAcciones[v] = sigAccion;
                    adq.add(v);
                }
            }
            int k = poder[u];
            if (k > 0){
                for (int dir = -1; dir <= 1; dir +=2){
                    int v = u + dir * k;
                    if (v>=0 && v <=n && !robot[v] && ene < mejorEnergia[v]){
                        mejorEnergia[v] = ene;
                        parent[v] = u;
                        acc[v] = dir ==1 ? "S+" : "S-";
                        numAcciones[v] = sigAccion;
                        adq.add(v);
                    }
                }
            }
            int EResta = E - ene;
            if (EResta == 0) continue;

            int min = Math.max(0, u - EResta);
            int max = Math.min(n, u + EResta);
            Integer v = libres.ceiling(min);
            while (v != null && v <= max){
                int costo = Math.abs(v-u);
                int nuevoE = ene + costo;
                if (nuevoE< mejorEnergia[v]){
                    mejorEnergia[v] = nuevoE;
                    parent[v] = u;
                    acc[v] = "T" + (v-u);
                     numAcciones[v] = sigAccion;
                     adq.add(v);
                }
                Integer siguiente = libres.higher(v);
                libres.remove(v);
                v = siguiente;
            }

        }
        return "NO SE PUEDE";
    }

    private static String reconstruirRuta(int[] parent, String[] acc, int goal){
        ArrayDeque<String> queue = new ArrayDeque<>();
        int cur = goal;
        while(parent[cur] != -1){
            queue.push(acc[cur]);
            cur = parent[cur];
        }
        return queue.size() + " " + String.join(" ", queue);
    }

}
