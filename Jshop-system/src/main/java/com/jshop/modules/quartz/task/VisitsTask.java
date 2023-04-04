package com.jshop.modules.quartz.task;

import com.jshop.modules.monitor.service.VisitsService;
import org.springframework.stereotype.Component;

/**
 * @author jack胡
 */
@Component
public class VisitsTask {

    private final VisitsService visitsService;

    public VisitsTask(VisitsService visitsService) {
        this.visitsService = visitsService;
    }

    public void run(){
        visitsService.save();
    }
}
