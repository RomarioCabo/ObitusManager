package com.br.obitus_manager.domain.obituary_notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObituaryNoticePhoto {
    private final byte[] bytes;
    private final String contentType;
}
