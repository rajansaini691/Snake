package server;

public class ServerLaser {
	int i, j;
	int direction;
	
	public ServerLaser(int direction, int i, int j) {
		this.direction = direction;
		
		if(direction < 2) {
			this.i = i;
			this.j = -100;
		} else {
			this.i = -100;
			this.j = j;
		}
	}
	
	public String broadcast() {
		return ((i > 0)? i : j) + "";
	}
	
}
