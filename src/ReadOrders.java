import com.sun.tools.javac.Main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadOrders implements Runnable {
    String idComanda;
    int nrProduse;
    String line;
    Thread[] threads;
    AtomicInteger respectiveProduce = new AtomicInteger(1);

    public ReadOrders() {

    }


    @Override
    public void run() {
        try {
            while((line = Tema2.reader.readLine()) != null) {

                respectiveProduce.set(1);
                String[] splitedLine = line.split(",");
                idComanda = splitedLine[0];
                nrProduse = Integer.parseInt(splitedLine[1]);
                threads = new Thread[nrProduse];

                for (int i = 0; i < nrProduse; i++) {
                    Tema2.sem.acquire();
                    threads[i] = new Thread(new ReadOrdersProducts(this));
                    threads[i].start();
                }
                for (int j = 0; j < nrProduse; j++) {
                    threads[j].join();
                }

                if (nrProduse != 0)
                    Tema2.orders_out.write(line + ",shipped\n");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
