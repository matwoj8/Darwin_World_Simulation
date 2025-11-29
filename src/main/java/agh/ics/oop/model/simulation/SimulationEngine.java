package agh.ics.oop.model.simulation;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine implements Runnable{
    private final ArrayList<Simulation> simulations;
    private final ArrayList<Thread> threads = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public SimulationEngine(ArrayList<Simulation> simulations) {
        this.simulations = simulations;
    }

    public void runSync() {
        for (Simulation simulation : simulations) {
            simulation.start();
        }
    }

    public void runAsync() {
        for (Simulation simulation : simulations) {
            simulation.start();
        }
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }

    public void runAsyncInThreadPool(){
        for(Simulation simulation : simulations){
            executorService.submit(simulation);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Thread started.");
    }

    public boolean isRunning() {
        for (Simulation simulation : this.simulations) {
            if (simulation.isRunning()) {
                return true;
            }
        }
        return false;
    }

    public void stop() {
        for (Simulation simulation : this.simulations) {
            simulation.pauseSimulation();
        }
    }

    public ArrayList<Simulation> getSimulations() {
        return simulations;
    }

    public void start() {
        for (Simulation simulation : this.simulations) {
            simulation.resumeSimulation();
        }
    }
}
