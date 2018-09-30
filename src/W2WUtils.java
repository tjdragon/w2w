import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class W2WUtils {
	public static byte[] toBytes(final Object s) throws Exception {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(s);
		out.flush();
		final byte[] data = bos.toByteArray();
		bos.close();
		return data;
	}
	
	public static Object fromBytes(final byte[] data) throws Exception {
		final ByteArrayInputStream bis = new ByteArrayInputStream(data);
		final ObjectInput in = new ObjectInputStream(bis);
		final Object o = in.readObject(); 
		in.close();
		return o;
	}
}
