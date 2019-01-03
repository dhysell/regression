package repository.cc.framework.gw.element.guidewirebatch;

import java.time.LocalDateTime;

public interface IGWBatch {

    boolean start();

    boolean stop();

    boolean isRunning();

    int operationsPerformed();

    boolean isScheduled();

    LocalDateTime lastRunDate();

    long lastRunDuration();

    String failureReason();

}
