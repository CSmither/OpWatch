package org.smither.opwatch.server.profanity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smither.opwatch.server.NotificationService;
import org.smither.opwatch.server.signEvents.SignEvent;
import org.smither.opwatch.server.signs.Sign;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class RegexService {

    private static final Logger log = LoggerFactory.getLogger(RegexService.class);

    private static final List<Pattern> rxs = new ArrayList<>();

    private NotificationService notificationService;

    public RegexService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    private void profAlert(Sign sign) {
        notificationService.sendIRC(String.format("OPWATCH TRIGGERED!! Sign %s: \"%s, %s, %s, %s\" at %s %s %d,%d,%d placed by %s",
                sign.getId().toString().substring(0, 8), sign.getLine1(), sign.getLine2(), sign.getLine3(), sign.getLine4(),
                sign.getServer(), sign.getWorld(), sign.getX(), sign.getY(), sign.getZ(), sign.getPlacer()));
        log.info("! ! ! SIGN TRIGGERED OPWATCH ! ! !");
    }

    private void profAlert(SignEvent signEvent) {
        notificationService.sendIRC(String.format("OPWATCH TRIGGERED!! Event for sign %s: \"%s, %s, %s, %s\" at %s %s %d,%d,%d placed by %s, last changed by %s",
                signEvent.getSign().getId().toString().substring(0, 8), signEvent.getLine1(), signEvent.getLine2(), signEvent.getLine3(), signEvent.getLine4(),
                signEvent.getSign().getServer(), signEvent.getSign().getWorld(), signEvent.getSign().getX(), signEvent.getSign().getY(), signEvent.getSign().getZ(), signEvent.getSign().getPlacer(), signEvent.getActioner()));
        log.info("! ! ! SIGN TRIGGERED OPWATCH ! ! !");
    }

    public boolean profanityCheck(Sign sign) {
        boolean triggered = profanityCheck(String.join(sign.getLine1(), sign.getLine2(), sign.getLine3(), sign.getLine4()).toLowerCase());
        if (triggered) {
            profAlert(sign);
        }
        return triggered;
    }

    public boolean profanityCheck(SignEvent signEvent) {
        boolean triggered = profanityCheck(String.join(signEvent.getLine1(), signEvent.getLine2(), signEvent.getLine3(), signEvent.getLine4()).toLowerCase());
        if (triggered) {
            profAlert(signEvent);
        }
        return triggered;
    }

    private boolean profanityCheck(String string) {
        return rxs.parallelStream().anyMatch(rx -> rx.matcher(string).find());
    }
}
