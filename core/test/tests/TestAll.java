package tests;

import org.junit.Test;

import com.guardian.game.components.StateComponent.Orientation;

public class TestAll {

	@Test
	public void nor() {
		
		for(Orientation o : Orientation.values())
			System.out.println(o.vector);
	}
}
