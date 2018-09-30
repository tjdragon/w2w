import java.math.BigDecimal;
import java.security.Security;
import java.util.List;

import com.swirlds.platform.Browser;
import com.swirlds.platform.Platform;
import com.swirlds.platform.SwirldMain;
import com.swirlds.platform.SwirldState;

public class W2WDemoMain implements SwirldMain {
	private Platform platform;
	private long selfId;
	
	@Override
	public void init(final Platform platform, final long id) {
		this.platform = platform;
		this.selfId = id;
		platform.setSleepAfterSync(250); // milliseconds
		System.out.println("init("+platform+","+selfId+")");
	}

	@Override
	public SwirldState newState() {
		return new W2WDemoState();
	}

	@Override
	public void preEvent() {
		// NOOP
	}

	@Override
	public void run() {
		var currencies = List.of("SGD", "EUR");
		currencies.stream().forEach(c -> {
			try {
				var amount = new BigDecimal((int)(Math.random() * 1000.0));
				var balanceTx = new W2WSetBalanceTxData(platform.getAddress().getSelfName(), amount, c);
				var w2wTx = new W2WTransaction(TxType.SET_BALANCE_TX, balanceTx);
				this.platform.createTransaction(W2WUtils.toBytes(w2wTx));
				System.out.println("Created and sent transaction " + w2wTx);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		W2WDemoState previousState = null;
		while(true) {
			try {
				final W2WDemoState state = (W2WDemoState)this.platform.getState();
				if (!state.equals(previousState)) {
					System.out.println();
					System.out.println("Latest state " + state);
				}
				previousState = state;
				
				Thread.sleep(5_000);
				// dave is the FX provider
				if ("dave".equals(platform.getAddress().getSelfName())) {
					var fxTx1 = new W2WFXTxData(new CcyPair("SGD", "EUR"), 0.64 + Math.random() / 100.0);
					final W2WTransaction w2wTx1 = new W2WTransaction(TxType.SET_FX_RATE_TX, fxTx1);
					this.platform.createTransaction(W2WUtils.toBytes(w2wTx1));
					System.out.println("Created and sent transaction " + w2wTx1);
					
					var fxTx2 = new W2WFXTxData(new CcyPair("EUR", "SGD"), 1.56 + Math.random() / 100.0);
					final W2WTransaction w2wTx2 = new W2WTransaction(TxType.SET_FX_RATE_TX, fxTx2);
					this.platform.createTransaction(W2WUtils.toBytes(w2wTx2));
					System.out.println("Created and sent transaction " + w2wTx2);
				}
				
				Thread.sleep(5_000);
				// alice randomly sends money to bob
				if ("alice".equals(platform.getAddress().getSelfName())) {
					var fromCcy = Math.random() > 0.5 ? "EUR" : "SGD";
					var toCcy = Math.random() > 0.5 ? "EUR" : "SGD";
					var transferTx = new W2WTransferTxData(
							"alice", 
							"bob", 
							new BigDecimal((int)(Math.random() * 10.0)), 
							fromCcy, 
							toCcy);
					final W2WTransaction w2wTx = new W2WTransaction(TxType.TRANSFER_TX, transferTx);
					this.platform.createTransaction(W2WUtils.toBytes(w2wTx));
					System.out.println("Created and sent transaction " + w2wTx);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Security.setProperty("crypto.policy", "unlimited");
		System.out.println("Game Demo: Set unlimited crypto policy");
		System.out.println("Game Demo: Running on " + System.getProperty("java.version"));
		Browser.main(args);
	}
}
