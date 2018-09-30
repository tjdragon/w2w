import java.math.BigDecimal;

public final class W2WTransferTxData implements TxData {
	private static final long serialVersionUID = 4613557366515313239L;
	
	public final String fromMember;
	public final String toMember;
	public final BigDecimal amount;
	public final String fromCurrency;
	public final String toCurrency;
	
	public W2WTransferTxData(final String fromMember, 
			                 final String toMember, 
			                 final BigDecimal amount, 
			                 final String fromCurrency,
			                 final String toCurrency) {
		this.fromMember = fromMember;
		this.toMember = toMember;
		this.amount = amount;
		this.fromCurrency = fromCurrency;
		this.toCurrency = toCurrency;
	}

	@Override
	public String toString() {
		return "W2WTransferTxData [fromMember=" + fromMember + ", toMember=" + toMember + ", amount=" + amount
				+ ", fromCurrency=" + fromCurrency + ", toCurrency=" + toCurrency + "]";
	}
}
