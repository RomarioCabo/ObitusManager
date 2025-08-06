package com.br.obitus_manager.domain.anti_flood.service.impl;

import com.br.obitus_manager.application.exception.BadRequestException;
import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.anti_flood.AntiFloodDto;
import com.br.obitus_manager.domain.anti_flood.service.AntiFloodService;
import com.br.obitus_manager.domain.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AntiFloodServiceImpl implements AntiFloodService {

    private final DatabaseProvider databaseProvider;

    @Override
    public void validateAttempt(String email) {
        final LocalDateTime now = LocalDateTime.now();
        AntiFloodDto antiFlood = databaseProvider.findByEmail(email);

        if (antiFlood == null) {
            saveNewAttempt(email);
            return;
        }

        // Se ainda está no tempo de bloqueio, bloqueia
        if (antiFlood.isBlocked() && antiFlood.getBlockEnd().isAfter(now)) {
            throw new BadRequestException("Email bloqueado até: "
                    + Util.formatterDate(antiFlood.getBlockEnd(), "dd/MM/yyyy HH:mm:ss"));
        }

        // Se passou o tempo de bloqueio, subir nível de bloqueio e resetar tentativas
        if (antiFlood.isBlocked() && antiFlood.getBlockEnd().isBefore(now)) {
            antiFlood.setAttempts(0);
            antiFlood.setBlockStart(null);
            antiFlood.setBlockEnd(null);
        }

        // Aumenta tentativa
        antiFlood.setAttempts(antiFlood.getAttempts() + 1);

        // Se chegou na 3ª tentativa, bloqueia de acordo com o nível
        if (antiFlood.getAttempts() >= 3) {
            applyBlock(antiFlood, now);
            antiFlood.setAttempts(0); // Reinicia após aplicar o bloqueio
            antiFlood.setBlockedLevel(antiFlood.getBlockedLevel() + 1);
        }

        save(antiFlood);
    }

    private void saveNewAttempt(String email) {
        AntiFloodDto newEntry = AntiFloodDto.builder()
                .email(email)
                .attempts(1)
                .blockedLevel(0)
                .build();

        databaseProvider.saveAntiFlood(newEntry.getId(), email, newEntry.getAttempts(), null, null,
                newEntry.getBlockedLevel());
    }

    private void applyBlock(AntiFloodDto antiFlood, LocalDateTime now) {
        int level = antiFlood.getBlockedLevel();
        antiFlood.setBlockStart(now);

        if (level == 0) {
            antiFlood.setBlockEnd(now.plusMinutes(5));
        } else if (level == 1) {
            antiFlood.setBlockEnd(now.plusMinutes(10));
        } else {
            antiFlood.setBlockEnd(now.plusHours(24));
            antiFlood.setBlockedLevel(0); // reiniciar ciclo após 3 bloqueios
        }
    }

    private void save(AntiFloodDto dto) {
        databaseProvider.saveAntiFlood(
                dto.getId(),
                dto.getEmail(),
                dto.getAttempts(),
                dto.getBlockStart(),
                dto.getBlockEnd(),
                dto.getBlockedLevel()
        );
    }
}
