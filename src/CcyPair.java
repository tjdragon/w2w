import java.io.Serializable;

public final class CcyPair implements Serializable {
	private static final long serialVersionUID = -7734773311930345135L;

	public final String buyCcy;
	public final String sellCcy;
	public final String tenor = "SP";
	
	public CcyPair(final String buyCcy, final String sellCcy) {
		this.buyCcy = buyCcy;
		this.sellCcy = sellCcy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buyCcy == null) ? 0 : buyCcy.hashCode());
		result = prime * result + ((sellCcy == null) ? 0 : sellCcy.hashCode());
		result = prime * result + ((tenor == null) ? 0 : tenor.hashCode());
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
		CcyPair other = (CcyPair) obj;
		if (buyCcy == null) {
			if (other.buyCcy != null)
				return false;
		} else if (!buyCcy.equals(other.buyCcy))
			return false;
		if (sellCcy == null) {
			if (other.sellCcy != null)
				return false;
		} else if (!sellCcy.equals(other.sellCcy))
			return false;
		if (tenor == null) {
			if (other.tenor != null)
				return false;
		} else if (!tenor.equals(other.tenor))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "CcyPair [buyCcy=" + buyCcy + ", sellCcy=" + sellCcy + ", tenor=" + tenor + "]";
	}
}
