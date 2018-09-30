import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class W2WState implements Serializable {
	private static final long serialVersionUID = 1L;

	// Balances of members to currency
	Map<String, Map<String, BigDecimal>> balances;
	// Ccy Pair Quotes for Transfer
	Map<CcyPair, Double> quotes;
	
	public void init() {
		balances = Collections.synchronizedMap(new HashMap<>());
		quotes = Collections.synchronizedMap(new HashMap<>());
	}
	
	public synchronized void update(final W2WSetBalanceTxData balanceData) {
		if (!balances.containsKey(balanceData.member)) {
			balances.put(balanceData.member, new HashMap<>());
		}
		balances.get(balanceData.member).put(balanceData.currency, balanceData.amount);
	}
	
	public synchronized void transfer(final W2WTransferTxData transferTx) {
		if (balances.containsKey(transferTx.fromMember)) {
			var fromBalance = balances.get(transferTx.fromMember);
			if (fromBalance.containsKey(transferTx.fromCurrency)) {
				var initialAmount = fromBalance.get(transferTx.fromCurrency);
				if (initialAmount.compareTo(transferTx.amount) >= 0) {
					System.out.println("Transferring " + transferTx.amount + " from " + transferTx.fromMember + " to " + transferTx.toMember);
					
					var conversionRate = getConversionRate(transferTx.fromCurrency, transferTx.toCurrency);
					System.out.println("Conversion rate from " + transferTx.fromCurrency + " to " + transferTx.toCurrency + " is " + conversionRate);
					if (Double.isNaN(conversionRate)) {
						System.err.println("Cannot convert");
						return;
					}
					var newFromAmount = balances.get(transferTx.fromMember).get(transferTx.fromCurrency).subtract(transferTx.amount);
					
					if (!balances.get(transferTx.toMember).containsKey(transferTx.toCurrency)) {
						balances.get(transferTx.toMember).put(transferTx.toCurrency, BigDecimal.ZERO);
					}
					var newToAmount = balances.get(transferTx.toMember).get(transferTx.toCurrency).add(transferTx.amount).multiply(new BigDecimal(conversionRate));
					
					balances.get(transferTx.fromMember).put(transferTx.fromCurrency, newFromAmount);
					balances.get(transferTx.toMember).put(transferTx.toCurrency, newToAmount);
				} else {
					System.err.println("Insufficient amount for transfer: " + initialAmount + " < " + transferTx.amount);
				}
			} else {
				System.err.println("Non-existant from currency: " + transferTx.fromCurrency);
			}
		} else {
			System.err.println("Non-existant from member: " + transferTx.fromMember);
		}
	}
	
	private double getConversionRate(final String fromCurrency, final String toCurrency) {
		if (fromCurrency.equals(toCurrency)) {
			return 1.0;
		} else {
			var ccyPair = new CcyPair(toCurrency, fromCurrency);
			if (quotes.containsKey(ccyPair)) {
				return quotes.get(ccyPair);
			} else {
				return Double.NaN;
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balances == null) ? 0 : balances.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		W2WState other = (W2WState) obj;
		if (balances == null) {
			if (other.balances != null)
				return false;
		} else if (!balances.equals(other.balances))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "W2WState [balances=" + balances + ", quotes=" + quotes + "]";
	}
}
