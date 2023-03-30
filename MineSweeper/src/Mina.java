public class Mina {
	private int mine;
	private static boolean exploded;
	
	public Mina() {
		mine = 0;
		exploded = false;
	}
	
	public void setMina() {
		mine = 1;
	}
	
	public void premi() {
		if(!exploded) {
			if(mine == 1) {
				mine = 2;
				exploded = true;
			}
		}
	}
	
	public int getStato() {
		return mine;
	}
	
	public static boolean getStatoPartita() {
		if(exploded)
			return true;
		else
			return false;
	}
}
