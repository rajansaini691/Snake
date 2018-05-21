package server;

import java.util.ArrayList;
import java.util.Random;

public class ServerFood {
	private Random random = new Random();
	private int i, j;

	public ServerFood() {
		i = random.nextInt(Server.fieldWidth);
		j = random.nextInt(Server.fieldHeight);
	}
	
	public void update() {
		
	}

	public void teleport(ClientThread[] threads) {
		boolean success = false;

		//Loops through all snakes & makes sure that the food doesn't intersect any of them
		successLoop: while (!success) {
			i = random.nextInt(Server.fieldWidth);
			j = random.nextInt(Server.fieldHeight);

			for (int k = 0; k < threads.length; k++) {
				if (threads[k] != null && threads[k].snake != null && !threads[k].snake.dead) {
					ArrayList<int[]> coords = threads[k].snake.getCoords();
					for (int l = 0; l < coords.size(); l++) {
						if (coords.get(l)[0] == i && coords.get(l)[1] == j) {
							continue successLoop;
						}
					}
				}
			}

			success = true;
		}
	}

	public String broadcast() {
		return i + "," + j + " ";
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}
}
