package org.smither.opwatch.server.signEvents;

import org.smither.opwatch.server.profanity.RegexService;
import org.smither.opwatch.server.signs.Sign;
import org.smither.opwatch.server.signs.SignService;
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

    private RegexService regexService;
    private SignService signService;

    @Autowired
    public SignEventService(
            SignEventDAO signEventDAO, RegexService regexService, SignService signService) {
        this.signEventDAO = signEventDAO;
        this.regexService = regexService;
        this.signService = signService;
    }

    public SignEvent createSignEvent(SignEventPostDTO sep) {
        List<Sign> related =
                signService.getSignsWithServerWithWorldWithXWithYWithZ(
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
        return signEventDAO.findBySign(sign);
    }

    public List<SignEvent> getEvents() {
        return signEventDAO.findAll();
    }

    public Optional<SignEvent> getSignEvent(UUID signEventId) {
        return signEventDAO.findById(signEventId);
    }
}
