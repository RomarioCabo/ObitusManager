package com.br.obitus_manager.domain.obituary_notice.service;

import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;

import java.util.UUID;

public interface ObituaryNoticeService {
    ObituaryNoticeResponse upsert(final ObituaryNoticeRequest request, final UUID obituaryNoticeId);

    byte[] getPhotoByIdObituaryNoticeId(final UUID obituaryNoticeId);
}
