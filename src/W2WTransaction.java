import java.io.Serializable;

public final class W2WTransaction implements Serializable {
	private static final long serialVersionUID = 5197207079622422896L;

	public final TxType type;
	public final TxData data;
	
	public W2WTransaction() {
		type = TxType.UNKNOWN;
		data = null;
	}

	public W2WTransaction(TxType type, TxData data) {
		super();
		this.type = type;
		this.data = data;
	}

	@Override
	public String toString() {
		return "W2WTransaction [type=" + type + ", data=" + data + "]";
	}
}
