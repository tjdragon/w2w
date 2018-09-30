
public class W2WFXTxData implements TxData {
	private static final long serialVersionUID = 4439605894328239482L;

	public final CcyPair ccyPair;
	public final double rate;
	
	public W2WFXTxData(final CcyPair ccyPair, final double rate) {
		this.ccyPair = ccyPair;
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "W2WFXTxData [ccyPair=" + ccyPair + ", rate=" + rate + "]";
	}
}
