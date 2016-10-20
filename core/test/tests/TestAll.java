package tests;

import org.junit.Test;

import com.game.core.component.PathfindingComponent;
import com.guardian.game.components.StateComponent.Orientation;

public class TestAll {

	
	public void nor() {
		
		for(Orientation o : Orientation.values())
			System.out.println(o.vector);
	}
	
	@Test
	public void pf(){
		
		new PathfindingComponent();
	}
}
