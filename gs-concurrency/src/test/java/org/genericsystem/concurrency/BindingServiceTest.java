package org.genericsystem.concurrency;

import org.genericsystem.api.exception.ExistsException;
import org.testng.annotations.Test;

@Test
public class BindingServiceTest extends AbstractTest {

	public void test001_addInstance() {
		// given
		Generic engine = new Engine();

		// when
		Generic vehicle = engine.addInstance("Vehicle");

		// then
		assert "Vehicle".equals(vehicle.getValue());
		assert vehicle.isAlive();
	}

	public void test002_addSameValueKO() {
		// given
		Generic engine = new Engine();
		engine.addInstance("Vehicle");

		new RollbackCatcher() {
			@Override
			public void intercept() {
				// when
				engine.addInstance("Vehicle");
			}
			// then
		}.assertIsCausedBy(ExistsException.class);
	}

	// public void test003_addSameValueSingular() {
	// // given
	// Generic engine = new Engine();
	// Generic vehicle = engine.addInstance("Vehicle");
	// vehicle.setSingularConstraint(0);
	//
	// // when
	// Generic vehicle2 = engine.addInstance("Vehicle");
	//
	// assert vehicle.isAlive();
	// assert vehicle2.isAlive();
	// assert "Vehicle".equals(vehicle.getValue());
	// assert "Vehicle".equals(vehicle2.getValue());
	// }

}
