

package com.mp.util;
import java.io.*;
 
public class JDK_VersionTest {
    public static void main(String[] args) throws IOException {
        checkClassVersion("/JAVA/workspae_juno/push_mpv3_lib/src/com/mp/util/CampLogProcess.class");
    }                      
 
    private static void checkClassVersion(String filename)
        throws IOException
    {
        DataInputStream in = new DataInputStream
         (new FileInputStream(filename));
 
        int magic = in.readInt();
        if(magic != 0xcafebabe) {
          System.out.println(filename + " is not a valid class!");;
        }
        int minor = in.readUnsignedShort();
        int major = in.readUnsignedShort();
        System.out.println(filename + ": " + major + " . " + minor);
        in.close();
    }
}
 