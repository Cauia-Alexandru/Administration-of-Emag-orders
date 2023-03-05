import java.io.*;
import java.util.concurrent.Semaphore;

public class Tema2 {
    static String folder;
    static int P;
    static BufferedReader reader;
    static BufferedWriter orders_out;
    static BufferedWriter order_products_out;
    static Semaphore sem;

    public static void main(String[] args) {
        folder = args[0];
        P = Integer.parseInt(args[1]);
        sem = new Semaphore(Tema2.P);
        {
            try {
                orders_out = new BufferedWriter(new FileWriter("orders_out.txt"));
                order_products_out = new BufferedWriter(new FileWriter("order_products_out.txt"));
                reader = new BufferedReader(new FileReader(folder + "/orders.txt"));
                Thread[] threads = new Thread[P];
                for (int i = 0; i < P; i++){
                    threads[i] = new Thread(new ReadOrders());
                    threads[i].start();
                }
                for (int j = 0; j < P; j++){
                    threads[j].join();
                }
                orders_out.close();
                order_products_out.close();

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }
}