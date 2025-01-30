package org.cws.web.software.engineer.task.sync.scheduler.manager;

/**
 * Simple interface to manage (enable/disable) task scheduling 
 */
public interface SchedulingManager {

    void enable();

    void disable();
}
