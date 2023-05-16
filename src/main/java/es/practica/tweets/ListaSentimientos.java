package es.practica.tweets;

public class ListaSentimientos {
	private float score;
	private float magnitude;
	
	public ListaSentimientos() {
		this.score = 0;
		this.magnitude = 0;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(float magnitude) {
		this.magnitude = magnitude;
	}
}