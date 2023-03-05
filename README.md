# Administration Of Emag orders

## Description
The idea of the project is to process orders in parallel, that is, to process each 
product individually and in parallel (even within the same order). You can make an 
analogy with the way products are sent (or at least used to be sent) from eMAG orders:
because they can be in different warehouses, often products from the same order can 
leave in different packages through the courier for delivery efficiency. The goal of 
the project is to simulate this product. Thus, at a given time, an order may have some
of its products already shipped, but only when all the products within it are sent, 
can we say that the entire order is shipped.

## First level of threads(ReadOrders.java)
At the first level of threads, I have an AtomicInteger (which I use at level 2) that
I will set to 1 with each thread, which means I have another order. Here, I read the
lines from the orders.txt file. Once I have read the order, I need to count how many
products it has. Therefore, each read order starts other threads (level 2) to read
the orders_products.txt file. But the condition is to use a maximum of P threads. 
For this, before starting the threads, I have a semaphore where I make an acquire to
check if there are still free slots available. In the end, I check if I have more than
0 products, then I make shipped, because the level 2 threads will finish counting.

## Second level of threads
At level 2, I use that AtomicInteger and 2 int variables to help me with the reading
strategy. I have a do-while loop where I read orders until I reach the one I am 
looking for. The value of the AtomicInteger (getAndAdd) is stored in toFind because
I need to know which product I need to search for. When I find a product, I have a
counter that I increment and when the counter equals toFind, it means that it is the
product that needs to be read. When the next thread comes, toFind takes the 
incremented value of the AtomicInteger, which is the next product to be read.

After making the shipped call for the product, I release the semaphore, because a 
thread slot has been freed up from the allowed number of threads.
