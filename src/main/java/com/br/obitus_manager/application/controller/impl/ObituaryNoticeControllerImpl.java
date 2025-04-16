package com.br.obitus_manager.application.controller.impl;

import com.br.obitus_manager.application.controller.ObituaryNoticeController;
import com.br.obitus_manager.application.util.ControllerUtils;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeRequest;
import com.br.obitus_manager.domain.obituary_notice.ObituaryNoticeResponse;
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

        byte[] photo = obituaryNoticeService.getPhotoByIdObituaryNoticeId(obituaryNoticeId);

        if (photo != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ObituaryNoticeResponse>> find(String nameDeceased, UUID idCity, LocalDate dateDeceased) {
        return ResponseEntity.ok(obituaryNoticeService.find(nameDeceased, idCity, dateDeceased));
    }
}
