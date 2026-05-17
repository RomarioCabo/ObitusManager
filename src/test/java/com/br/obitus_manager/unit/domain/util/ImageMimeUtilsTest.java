package com.br.obitus_manager.unit.domain.util;

import com.br.obitus_manager.domain.util.ImageMimeUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

class ImageMimeUtilsTest {

    @Test
    void detectPngFromMagicBytes() {
        byte[] png = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals(IMAGE_PNG_VALUE, ImageMimeUtils.detectFromBytes(png));
    }

    @Test
    void detectJpegFromMagicBytes() {
        byte[] jpeg = new byte[]{(byte) 0xFF, (byte) 0xD8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals(IMAGE_JPEG_VALUE, ImageMimeUtils.detectFromBytes(jpeg));
    }

    @Test
    void parseMimeFromDataUrlPrefix() {
        String raw = "data:image/webp;base64,AAAA";
        assertEquals("image/webp", ImageMimeUtils.parseMimeFromDataUrlPrefix(raw));
    }
}
