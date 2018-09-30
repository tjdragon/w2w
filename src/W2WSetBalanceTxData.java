import java.math.BigDecimal;

public final class W2WSetBalanceTxData implements TxData {
	private static final long serialVersionUID = 5717439008924654999L;
	
	public final String member;
	public final BigDecimal amount;
	public final String currency;
	
	public W2WSetBalanceTxData(final String member, final BigDecimal amount, final String currency) {
		this.member = member;
		this.amount = amount;
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "W2WSetBalanceTxData [member=" + member + ", amount=" + amount + ", currency=" + currency + "]";
	}
}
