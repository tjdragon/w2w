import java.io.IOException;
import java.time.Instant;

import com.swirlds.platform.Address;
import com.swirlds.platform.AddressBook;
import com.swirlds.platform.FCDataInputStream;
import com.swirlds.platform.FCDataOutputStream;
import com.swirlds.platform.FastCopyable;
import com.swirlds.platform.Platform;
import com.swirlds.platform.SwirldState;

public class W2WDemoState implements SwirldState {
	private AddressBook addressBook = null;
	private W2WState w2wState = null;
	
	@Override
	public void init(final Platform platform, final AddressBook addressBook) {
		System.out.println("W2WDemoState::init");
		this.w2wState = new W2WState();
		this.w2wState.init();
		this.addressBook = addressBook;
	}
	
	@Override
	public void handleTransaction(final long id, 
			                      final boolean isConsensus, 
			                      final Instant timestamp, 
			                      final byte[] transaction, 
			                      final Address address) {
		try {
			if (!isConsensus)
				return;
			
			var member = this.addressBook.getAddress(id).getSelfName();
			var tx = (W2WTransaction)W2WUtils.fromBytes(transaction);
			System.out.println("Received transaction from " + member + ": " + tx);
			
			switch (tx.type) {
			case SET_BALANCE_TX:
				var balanceTx = (W2WSetBalanceTxData)tx.data;
				if (balanceTx.amount.doubleValue() > 0.0) {
					this.w2wState.update(balanceTx);
				} else {
					System.err.println("Not updating a negative amount " + balanceTx + ": " + this.w2wState);
				}
				break;
			case TRANSFER_TX:
				var transferTx = (W2WTransferTxData)tx.data;
				System.out.println("[X] ID " + id);
				w2wState.transfer(transferTx);
				break;
			case SET_FX_RATE_TX:
				var fxTx = (W2WFXTxData)tx.data;
				w2wState.quotes.put(fxTx.ccyPair, fxTx.rate);
				break;
			case UNKNOWN:
				break;
			}
		} catch (Throwable t) {
			t.printStackTrace();
			System.out.println("handleTransaction error: " + t.getMessage());
		}
	}
	
	@Override
	public FastCopyable copy() {
		var stateCopy = new W2WDemoState();
		stateCopy.copyFrom(this);
		return stateCopy;
	}

	@Override
	public void copyFrom(final FCDataInputStream inputStream) throws IOException {
		try {
			this.addressBook.copyFrom(inputStream);
			var data = inputStream.readAllBytes();
			this.w2wState = (W2WState)W2WUtils.fromBytes(data);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	@Override
	public void copyTo(final FCDataOutputStream outputStream) throws IOException {
		try {
			this.addressBook.copyTo(outputStream);
			outputStream.write(W2WUtils.toBytes(this.w2wState));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	@Override
	public void copyFrom(final SwirldState state) {
		try {
			var oldState = (W2WDemoState)state;
			var copy = W2WUtils.toBytes(oldState.w2wState);
			this.w2wState = (W2WState)W2WUtils.fromBytes(copy);
			this.addressBook = oldState.addressBook.copy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public AddressBook getAddressBookCopy() {
		return this.addressBook.copy();
	}

	@Override
	public void noMoreTransactions() {
		// NOOP
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((w2wState == null) ? 0 : w2wState.hashCode());
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
		W2WDemoState other = (W2WDemoState) obj;
		if (w2wState == null) {
			if (other.w2wState != null)
				return false;
		} else if (!w2wState.equals(other.w2wState))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "W2WDemoState [w2wState=" + w2wState + "]";
	}
}
