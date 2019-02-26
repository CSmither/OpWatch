package org.smither.opwatch.server.signs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.smither.opwatch.server.profanity.RegexService;
import org.smither.opwatch.server.signEvents.SignEventService;
import org.smither.opwatch.utils.sharedDTO.SignPostDTO;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SignServiceTest {

    private SignService service;

    @Mock
    private RegexService regexService;

    @Mock
    private SignDAO signDAO;

    @Mock
    private SignEventService signEventService;

    private Sign sign;

    @Before
    public void setup() {
        sign = new Sign("", "", "", "", LocalDateTime.now(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 0, 0, 0, "Test", false);
        ArgumentCaptor<Sign> arg = ArgumentCaptor.forClass(Sign.class);
        when(signDAO.findByServerAndWorldAndXAndYAndZ(any(), anyString(), anyInt(), anyInt(), anyInt())).thenReturn(Collections.emptyList());
        when(signDAO.save(arg.capture())).thenAnswer(signDAO -> arg.getValue());
        when(signDAO.findById(eq(sign.getId()))).thenReturn(Optional.of(sign));

        service = new SignService(signDAO, regexService, signEventService);
    }

    @Test
    public void testSignCreate() {
        SignPostDTO dto = SignPostDTO.builder()
                .id(UUID.randomUUID())
                .line1("New sign")
                .line2("")
                .line3("")
                .line4("")
                .placer(UUID.randomUUID())
                .server(UUID.randomUUID())
                .world("Test")
                .x(0)
                .y(0)
                .z(0)
                .time(LocalDateTime.now())
                .build();
        Sign sign = service.createSign(dto);
        assertEquals(dto.getId(), sign.getId());
        assertEquals(dto.getTime(), sign.getTime());
        assertEquals(dto.getServer(), sign.getServer());
    }

    @Test
    public void testGetSign() {
        Optional<Sign> fetchedSign = service.getSign(sign.getId());
        assertTrue(fetchedSign.isPresent());
        assertEquals(sign, fetchedSign.get());
    }

}