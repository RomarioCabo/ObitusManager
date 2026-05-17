package com.br.obitus_manager.domain.obituary_notice.service;

import com.br.obitus_manager.domain.common.PageResponse;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticePhoto;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ObituaryNoticeService {
    ObituaryNoticeResponse upsert(final ObituaryNoticeRequest request, final UUID obituaryNoticeId);

    ObituaryNoticePhoto getPhotoByIdObituaryNoticeId(final UUID obituaryNoticeId);

    PageResponse<ObituaryNoticeResponse> find(
            final String nameDeceased,
            final UUID idCity,
            final LocalDate dateDeceased,
            final Integer page,
            final Integer size,
            final String sort
    );
}
