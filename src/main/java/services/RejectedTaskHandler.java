package services;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

class RejectedTaskHandler implements RejectedExecutionHandler
{
    /**
     * Executed in case the thread pool is shutdown and a new request is received
     * @param task
     * @param executor
     */
    @Override
    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor)
    {
        String drink = ((DrinkMaker) task).getDrink();
        System.out.println("Machine OFF: Creation of drink " + drink + " rejected");
    }
}