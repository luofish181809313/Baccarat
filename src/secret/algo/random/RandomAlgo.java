package secret.algo.random;

import java.util.*;

public class RandomAlgo {

	public char[] getRandomBinary(int scopeMax) {
		ArrayList<Float> randomFloatList = new ArrayList<Float>();
		randomFloatList = (ArrayList<Float>) getRandomFloatList(scopeMax - 1);

		float seed = randomFloatList.get(0);

		Collections.sort(randomFloatList);

		int randomNumber = randomFloatList.indexOf(seed);
		char[] randomBinaryNumberArray = Integer.toBinaryString(randomNumber)
				.toCharArray();

		return randomBinaryNumberArray;
	}

	private List<Float> getRandomFloatList(int listScope) {
		Random random = new Random();
		List<Float> randomFloatList = new ArrayList<Float>();

		for (int i = 0; i < listScope;) {
			randomFloatList.add(random.nextFloat());
			i++;
		}
		return randomFloatList;
	}
}
