package t4.sers.util;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {
    public static String hash(String value) {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(value));
    }
}