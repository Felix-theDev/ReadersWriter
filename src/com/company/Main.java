package com.company;
/** The readers-writers problem relates to an object such as a file that is shared between multiple processes. ...
 *  To solve this situation,a writer should get exclusive access to an object
 * i.e. when a writer is accessing the object, no reader or writer may access it.
 * @author Felix Ogbonnaya
 * @since 2020-02-15
 */
import java.util.Random;
import java.util.concurrent.*;
public class Main {

    private static Main thrdsync;
    private static Thread t1, t2;
    private static final Random rand = new Random();
    private static Semaphore sm = new Semaphore(2);
    String text = "Beginning of the Book";
    private void busy() {
        try {
            Thread.sleep(rand.nextInt(1000)+1000);
        } catch (InterruptedException e){}
    }

    void write(String sentence)
    {
        System.out.println(Thread.currentThread().getName() +" started to WRITE");
        text += "\n" + sentence;
        System.out.println(text);
        System.out.println("End of Book\n");
        System.out.println(Thread.currentThread().getName() +" finished WRITING");
    }

    void read() {		System.out.println("\n"+Thread.currentThread().getName() +" started to READ");

        System.out.println("End of Book\n");
    }

    private class Writer implements Runnable {
        Main ts;
        Writer (String name, Main ts) {
            super();
            this.ts=ts;
        }
        public void run() {
            while (true) {
                busy();

                String new_sentence = new String("\tnew line in Book");
                ts.write(new_sentence);

            }
        }
    }
    private class Reader implements Runnable {
        Main ts;
        Reader (String name, Main ts) {
            super();
            this.ts=ts;
        }
        public void run() {
            try {
                while (true) {
                    sm.acquire();
                    sm.release();
                    busy();
                    ts.read();
                }             }
            catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }
    public void startThreads() {
        Main ts = new Main();
        t1 = new Thread(new Writer("Writer # 1", ts));
        t2 = new Thread(new Reader("Reader # 1", ts));

        t1.start();
        t2.start();

    }
    public static void main(String [] args) {
        thrdsync = new Main();
        System.out.println("Lets begin...\n");
        thrdsync.startThreads();
    }
}
