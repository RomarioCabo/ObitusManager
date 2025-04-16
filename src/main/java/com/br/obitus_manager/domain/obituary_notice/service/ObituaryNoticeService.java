package com.br.obitus_manager.domain.obituary_notice.service;

import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ObituaryNoticeService {
    ObituaryNoticeResponse upsert(final ObituaryNoticeRequest request, final UUID obituaryNoticeId);

    byte[] getPhotoByIdObituaryNoticeId(final UUID obituaryNoticeId);

    List<ObituaryNoticeResponse> find(final String nameDeceased, final UUID idCity, LocalDate dateDeceased);
}
