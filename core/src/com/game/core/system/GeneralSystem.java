package com.game.core.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.game.core.component.ScriptComponent;
import com.guardian.game.components.StateComponent;
import com.guardian.game.tools.FamilyTools;
import com.guardian.game.tools.MapperTools;

public class GeneralSystem extends IteratingSystem {

	public GeneralSystem(int priority) {
		super(FamilyTools.generalF, priority);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
		if(scriptComponent != null)
			scriptComponent.script.update(deltaTime);
		
		StateComponent stateComponent = MapperTools.stateCM.get(entity);
		if(stateComponent != null)
			stateComponent.entityState.update();
	}
}
