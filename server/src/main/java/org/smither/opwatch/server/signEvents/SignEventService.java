package org.smither.opwatch.server.signEvents;

import org.smither.opwatch.server.profanity.RegexService;
import org.smither.opwatch.server.signs.Sign;
import org.smither.opwatch.server.signs.SignDAO;
import org.smither.opwatch.utils.sharedDTO.SignEventPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SignEventService {

    private SignEventDAO signEventDAO;

    private SignDAO signDAO;

    private RegexService regexService;

    @Autowired
    public SignEventService(
            SignEventDAO signEventDAO, RegexService regexService, SignDAO signDAO) {
        this.signEventDAO = signEventDAO;
        this.regexService = regexService;
        this.signDAO = signDAO;
    }

    public SignEvent createSignEvent(SignEventPostDTO sep) {
        List<Sign> related =
                signDAO.findByServerAndWorldAndXAndYAndZ(
                        sep.getServer(), sep.getWorld(), sep.getX(), sep.getY(), sep.getZ());
        Sign sign = related.size() > 0 ? related.get(0) : null;
        SignEvent signEvent =
                signEventDAO.save(
                        new SignEvent(
                                UUID.randomUUID(),
                                sep.getEventType(),
                                sep.getNewLine1(),
                                sep.getNewLine2(),
                                sep.getNewLine3(),
                                sep.getNewLine4(),
                                sep.getDateTime(),
                                sign,
                                sep.getEditor(),
                                sep.getReason()));
        regexService.profanityCheck(signEvent);
        return signEvent;
    }

    public Collection<SignEvent> getEventsForSign(UUID sign) {
        return signEventDAO.findBySignId(sign);
    }

    public List<SignEvent> getEvents() {
        return signEventDAO.findAll();
    }

    public Optional<SignEvent> getSignEvent(UUID signEventId) {
        return signEventDAO.findById(signEventId);
    }
}
