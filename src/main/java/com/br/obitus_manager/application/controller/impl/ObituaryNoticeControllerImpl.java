package com.br.obitus_manager.application.controller.impl;

import com.br.obitus_manager.application.controller.ObituaryNoticeController;
import com.br.obitus_manager.application.util.ControllerUtils;
import com.br.obitus_manager.domain.common.PageResponse;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticePhoto;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
import com.br.obitus_manager.domain.util.ImageMimeUtils;
import com.br.obitus_manager.domain.obituary_notice.service.ObituaryNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ObituaryNoticeControllerImpl extends ControllerUtils implements ObituaryNoticeController {

    private final ObituaryNoticeService obituaryNoticeService;

    @Override
    public ResponseEntity<ObituaryNoticeResponse> save(ObituaryNoticeRequest request) {
        log.info("Saving ObituaryNotice: {}", request);

        ObituaryNoticeResponse response = obituaryNoticeService.upsert(request, null);

        log.info("ObituaryNotice saved: {}", response);

        return ResponseEntity.created(generateUriBuild(response.getIdObituaryNotice())).body(response);
    }

    @Override
    public ResponseEntity<ObituaryNoticeResponse> update(ObituaryNoticeRequest request, UUID obituaryNoticeId) {
        log.info("Updating ObituaryNotice: {}", request);

        ObituaryNoticeResponse response = obituaryNoticeService.upsert(request, obituaryNoticeId);

        log.info("ObituaryNotice Updated: {}", response);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<byte[]> getPhoto(UUID obituaryNoticeId) {
        ObituaryNoticePhoto photo = obituaryNoticeService.getPhotoByIdObituaryNoticeId(obituaryNoticeId);

        if (photo != null && photo.getBytes() != null && photo.getBytes().length > 0) {
            String contentType = photo.getContentType();
            if (contentType == null || contentType.isBlank()) {
                contentType = ImageMimeUtils.detectFromBytes(photo.getBytes());
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            return new ResponseEntity<>(photo.getBytes(), headers, HttpStatus.OK);
        }

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PageResponse<ObituaryNoticeResponse>> find(
            String nameDeceased,
            UUID idCity,
            LocalDate dateDeceased,
            Integer page,
            Integer size,
            String sort
    ) {
        return ResponseEntity.ok(
                obituaryNoticeService.find(nameDeceased, idCity, dateDeceased, page, size, sort));
    }
}
