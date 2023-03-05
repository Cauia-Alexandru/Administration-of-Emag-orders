import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadOrdersProducts implements Runnable{
    String idComanda;
    String idProdus;
    AtomicInteger readedProduce;
    String line;
    ReadOrders readOrders;
    int toFind;
    int count;

    final BufferedReader reader;

    public ReadOrdersProducts(ReadOrders readOrders) {
        this.readOrders = readOrders;
        readedProduce = readOrders.respectiveProduce;
        toFind = readedProduce.getAndAdd(1);
        count = 0;
        try {
            reader = new BufferedReader(new FileReader(Tema2.folder + "/order_products.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void run() {

        try {
            do {
                line = reader.readLine();
                if (line.split(",")[0].equals(readOrders.idComanda)) {
                    count++;
                }
            } while (count != toFind);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] splitedLine = line.split(",");
        idComanda = splitedLine[0];
        idProdus = splitedLine[1];

        //scriu in fiser out idComanda, idProdus, shipped
        try {
            Tema2.order_products_out.write(idComanda + "," + idProdus + "," + "shipped\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Tema2.sem.release();

    }
}
