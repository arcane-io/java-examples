package ccm.examples.threads;

/**
 * @link https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html .
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public class SimpleConcurrencyExample {

    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();

        Thread alpha = new Thread(new SimpleTask(sharedResource), "alpha");
        Thread beta = new Thread(new SimpleTask(sharedResource), "beta");

        alpha.start();
        beta.start();
        System.out.println(sharedResource.getTime() + " | STARTED " + Thread.activeCount());
    }

    public static class SharedResource {
        private int count;

        public /*synchronized*/ int getCount() {
            return count;
        }

        public long getTime() {
            return System.currentTimeMillis();
        }

        /**
         * 1. Call the <code>print()</code> method without <code>synchronized</code> .
         * 2. Call the <code>print()</code> method with <code>synchronized</code> .
         * 3. Call the <code>print()</code> method with <code>synchronized</code> and <code>Thread.yield()</code>.
         *
         * Run the program multiple times with the same settings and check what happens .
         *
         * @throws InterruptedException
         */
        public /*synchronized*/ void print() throws InterruptedException {
            String tempCount = String.format("(%03d)", count++);
            if (Thread.currentThread().getName().equals("alpha")) {
                //Thread.yield();
                Thread.sleep(2000);
            }
            System.out.println(getTime() + " | " +
                    tempCount + " | " +
                    "[" + Thread.currentThread().getId() + "]" +
                    "[" + Thread.currentThread().getName() + "]");
        }
    }

    public static class SimpleTask implements Runnable {
        private static final int MAX_COUNT = 20;
        private final SharedResource resource;

        public SimpleTask(final SharedResource sharedResource) {
            this.resource = sharedResource;
        }

        public void run() {
            while (resource.getCount() < SimpleTask.MAX_COUNT) {
                try {
                    resource.print();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
