package com.br.obitus_manager.domain.obituary_notice.service.impl;

import com.br.obitus_manager.application.exception.EntityNotFoundException;
import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import com.br.obitus_manager.domain.obituary_notice.service.ObituaryNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObituaryNoticeServiceImpl implements ObituaryNoticeService {

    private final DatabaseProvider databaseProvider;

    @Override
    public ObituaryNoticeResponse upsert(final ObituaryNoticeRequest request, final UUID obituaryNoticeId) {
        ObituaryNoticeResponse obituaryNoticeResponse = databaseProvider.saveObituaryNotice(request, obituaryNoticeId);
        if (obituaryNoticeResponse == null) {
            throw new EntityNotFoundException("Cidade informada não encontrada");
        }

        return obituaryNoticeResponse;
    }

    @Override
    public byte[] getPhotoByIdObituaryNoticeId(final UUID obituaryNoticeId) {
        return databaseProvider.getPhotoByIdObituaryNoticeId(obituaryNoticeId);
    }
}
