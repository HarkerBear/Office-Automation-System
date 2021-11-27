package utils;

import junit.framework.TestCase;
import org.junit.Test;

public class MD5UtilsTest extends TestCase {

    @Test
    public void testMd5Digest() {
        System.out.println(MD5Utils.md5Digest("test",111));
        System.out.println(MD5Utils.md5Digest("test",222));
        System.out.println(MD5Utils.md5Digest("test",333));
        System.out.println(MD5Utils.md5Digest("test",444));
        System.out.println(MD5Utils.md5Digest("test",555));
        System.out.println(MD5Utils.md5Digest("test",666));
        System.out.println(MD5Utils.md5Digest("test",777));
        System.out.println(MD5Utils.md5Digest("test",888));
        System.out.println(MD5Utils.md5Digest("test",999));
        System.out.println(MD5Utils.md5Digest("test",1111));
    }
}