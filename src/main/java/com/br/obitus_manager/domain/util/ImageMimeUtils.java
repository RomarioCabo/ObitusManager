package com.br.obitus_manager.domain.util;

import org.springframework.http.MediaType;

/**
 * Detecção de MIME de imagens por magic bytes ou prefixo data URL.
 */
public final class ImageMimeUtils {

    public static final String IMAGE_JPEG = MediaType.IMAGE_JPEG_VALUE;
    public static final String IMAGE_PNG = MediaType.IMAGE_PNG_VALUE;
    public static final String IMAGE_WEBP = "image/webp";
    public static final String FALLBACK = MediaType.APPLICATION_OCTET_STREAM_VALUE;

    private ImageMimeUtils() {
    }

    /**
     * Extrai MIME do prefixo {@code data:image/png;base64,} quando presente.
     */
    public static String parseMimeFromDataUrlPrefix(String base64) {
        if (base64 == null || !base64.startsWith("data:")) {
            return null;
        }
        int semi = base64.indexOf(';');
        if (semi <= 5) {
            return null;
        }
        return base64.substring(5, semi).trim().toLowerCase();
    }

    /**
     * Detecta o Content-Type a partir dos bytes da imagem (PNG, JPEG, WEBP).
     */
    public static String detectFromBytes(byte[] bytes) {
        if (bytes == null || bytes.length < 12) {
            return FALLBACK;
        }
        // PNG: 89 50 4E 47
        if (bytes[0] == (byte) 0x89 && bytes[1] == 0x50 && bytes[2] == 0x4E && bytes[3] == 0x47) {
            return IMAGE_PNG;
        }
        // JPEG: FF D8
        if (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8) {
            return IMAGE_JPEG;
        }
        // WEBP: RIFF....WEBP
        if (bytes[0] == 0x52 && bytes[1] == 0x49 && bytes[8] == 0x57 && bytes[9] == 0x45) {
            return IMAGE_WEBP;
        }
        return FALLBACK;
    }

    public static boolean isSupportedImageMime(String mime) {
        if (mime == null || mime.isBlank()) {
            return false;
        }
        String m = mime.toLowerCase();
        return IMAGE_JPEG.equals(m) || IMAGE_PNG.equals(m) || IMAGE_WEBP.equals(m);
    }
}
