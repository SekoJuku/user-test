package kz.edu.astanait.usertest.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtils {

    public static byte[] compressImage(byte[] data) {
        return Base64.encodeBase64(data);
    }

    public static byte[] decompressImage(byte[] data) {
        return Base64.decodeBase64(data);
    }
}