package org.smither.opwatch.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void sendIRC(String message) {
        logger.error("IRC Notifications not yet implemented");
        logger.error("###############################################################################################");
        logger.error(message);
        logger.error("###############################################################################################");
    }
}
