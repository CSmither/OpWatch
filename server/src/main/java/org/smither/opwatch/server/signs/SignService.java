package org.smither.opwatch.server.signs;

import com.google.common.collect.Lists;
import org.smither.opwatch.server.profanity.RegexService;
import org.smither.opwatch.server.signEvents.SignEventService;
import org.smither.opwatch.utils.SignChangeType;
import org.smither.opwatch.utils.sharedDTO.SignEventPostDTO;
import org.smither.opwatch.utils.sharedDTO.SignPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SignService {

    private SignDAO signDAO;

    private RegexService regexService;
    private SignEventService signEventService;

    @Autowired
    SignService(SignDAO signDAO, RegexService regexService) {
        this.signDAO = signDAO;
        this.regexService = regexService;
    }

    public Sign createSign(SignPostDTO sp) {
        signDAO.findByServerAndWorldAndXAndYAndZ(sp.getServer(),sp.getWorld(),sp.getX(),sp.getY(),sp.getZ()).forEach(sign ->{
            SignEventPostDTO sepDto = new SignEventPostDTO();
            sepDto.setServer(sp.getServer());
            sepDto.setWorld(sp.getWorld());
            sepDto.setX(sp.getX());
            sepDto.setY(sp.getY());
            sepDto.setZ(sp.getZ());
            sepDto.setReason("Sign created in position");
            sepDto.setEditor("Invalid Data");
            sepDto.setDateTime(LocalDateTime.now());
            sepDto.setEventType(SignChangeType.delete);
            signEventService.createSignEvent(sepDto);
        });
        Sign newSign = new Sign(sp.getLine1(), sp.getLine2(), sp.getLine3(), sp.getLine4(), sp.getTime(), sp.getPlacer(),
                sp.getId(), sp.getServer(), sp.getX(), sp.getY(), sp.getZ(), sp.getWorld(), false
        );
        regexService.profanityCheck(newSign);
        return signDAO.save(newSign);
    }

    public Optional<Sign> getSign(UUID signID) {
        return signDAO.findById(signID);
    }

    public List<Sign> getSigns() {
        return Lists.newArrayList(signDAO.findAll());
    }

    public List<Sign> getSignsWithPlacer(UUID placer) {
        return signDAO.findByPlacer(placer);
    }

    public List<Sign> getSignsWithServerWithWorldWithXWithYWithZ(UUID server, String world, int x, int y, int z) {
        return signDAO.findByServerAndWorldAndXAndYAndZ(server,world,x,y,z);
    }

}
