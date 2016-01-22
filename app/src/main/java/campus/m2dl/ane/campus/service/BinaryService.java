package campus.m2dl.ane.campus.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by Alexandre on 22/01/2016.
 */
public class BinaryService {

    public static byte[] toBinary(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;

        byte[] objBinary = null;

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);

            objBinary = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        return objBinary;
    }

    public static Object toObject(byte[] objBinary) {
        Object obj = null;

        ByteArrayInputStream bis = new ByteArrayInputStream(objBinary);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            obj = in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        return obj;
    }
}
