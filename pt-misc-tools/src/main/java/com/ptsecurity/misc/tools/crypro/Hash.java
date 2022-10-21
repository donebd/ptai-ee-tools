package com.ptsecurity.misc.tools.crypro;

import lombok.NonNull;
import lombok.SneakyThrows;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class Hash {
    @SneakyThrows
    public static String md5(@NonNull final String value) {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(value.getBytes());
        return DatatypeConverter.printHexBinary(md5.digest()).toUpperCase();
    }
}
