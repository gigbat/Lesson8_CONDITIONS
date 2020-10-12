import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();
    static int account = 0;

    public static void main(String[] args) {
        new AccountMinus().start();
        new AccountPlus().start();
    }

    static class AccountPlus extends Thread {
        @Override
        public void run() {
            lock.lock();
            account += 10;
            condition.signal();
            lock.unlock();
        }
    }

    static class AccountMinus extends Thread {
        @Override
        public void run() {
            lock.lock();
            if (account < 10) {
                try {
                    System.out.println("Account = " + account);
                    condition.await();
                    System.out.println("Account = " + account);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            account -= 10;
            lock.unlock();
        }
    }
}
