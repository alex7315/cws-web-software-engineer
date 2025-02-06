package org.cws.web.software.engineer.task.sync.scheduler.runner;

/**
 * Interface to extend {@link Runnable} 
 * with possibility to get count of running tasks 
 */
public interface JobRunner extends Runnable {

    int getRunCounter();
}
