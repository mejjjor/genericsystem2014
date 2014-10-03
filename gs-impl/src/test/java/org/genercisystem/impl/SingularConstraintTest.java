package org.genercisystem.impl;

import org.genericsystem.api.exception.ConstraintViolationException;
import org.genericsystem.impl.Engine;
import org.genericsystem.impl.Generic;
import org.genericsystem.kernel.Statics;
import org.testng.annotations.Test;

@Test
public class SingularConstraintTest extends AbstractTest {

	public void test001_enableSingularConstraint_addInstance() {
		Engine engine = new Engine();
		Generic vehicle = engine.addInstance("Vehicle");
		Generic color = engine.addInstance("Color");
		Generic red = color.addInstance("red");
		Generic yellow = color.addInstance("yellow");
		Generic vehicleColor = vehicle.addAttribute("vehicleColor", color);
		vehicleColor.enableSingularConstraint(Statics.TARGET_POSITION);
		assert vehicleColor.isSingularConstraintEnabled(Statics.TARGET_POSITION);
		vehicle.addHolder(vehicleColor, "vehicleRed", red);
		new RollbackCatcher() {
			@Override
			public void intercept() {
				vehicle.addHolder(vehicleColor, "vehicleYellow", yellow);
			}
		}.assertIsCausedBy(ConstraintViolationException.class);
	}

	public void test002_enableSingularConstraint_trinaryRelation() {
		Engine engine = new Engine();
		Generic vehicle = engine.addInstance("Vehicle");
		Generic color = engine.addInstance("Color");
		Generic time = engine.addInstance("Time");
		Generic red = color.addInstance("red");
		Generic today = time.addInstance("today");
		Generic yesterday = time.addInstance("yesterday");
		Generic vehicleColor = vehicle.addAttribute("vehicleColor", color);
		vehicleColor.enableSingularConstraint(Statics.TARGET_POSITION);
		assert vehicleColor.isSingularConstraintEnabled(Statics.TARGET_POSITION);
		vehicle.addHolder(vehicleColor, "vehicleRedToday", red, today);
		vehicle.addHolder(vehicleColor, "vehicleRedYesterday", red, yesterday);
	}

	// public void test002_enablePropertyConstraint_addInstance() {
	// Engine engine = new Engine();
	// Generic vehicle = engine.addInstance("Vehicle");
	// Generic power = engine.addInstance("Power", vehicle);
	// Generic subPower = engine.addInstance(power, "SubPower", vehicle);
	// assert subPower.inheritsFrom(power);
	// power.enablePropertyConstraint();
	// assert subPower.isPropertyConstraintEnabled();
	// subPower.addInstance("123", vehicle);
	// new RollbackCatcher() {
	//
	// @Override
	// public void intercept() {
	// subPower.addInstance("126", vehicle);
	// }
	// }.assertIsCausedBy(ExistsException.class);
	// }
	//
	// public void test003_enablePropertyConstraint_addInstance() {
	// Engine engine = new Engine();
	// Generic vehicle = engine.addInstance("Vehicle");
	// Generic car = engine.addInstance(vehicle, "Car");
	// Generic power = engine.addInstance("Power", vehicle);
	// Generic subPower = engine.addInstance(power, "Power", car);
	// assert subPower.inheritsFrom(power);
	// power.enablePropertyConstraint();
	// assert subPower.isPropertyConstraintEnabled();
	// subPower.addInstance("123", car);
	// new RollbackCatcher() {
	//
	// @Override
	// public void intercept() {
	// subPower.addInstance("126", car);
	// }
	// }.assertIsCausedBy(ExistsException.class);
	// }
	//
	// public void test001_enablePropertyConstraint_setInstance() {
	// Engine engine = new Engine();
	// Generic vehicle = engine.addInstance("Vehicle");
	// Generic power = engine.addInstance("Power", vehicle);
	// power.enablePropertyConstraint();
	// assert power.isPropertyConstraintEnabled();
	// power.setInstance("123", vehicle);
	// power.setInstance("126", vehicle);
	// assert power.getInstances().size() == 1;
	// power.getInstances().forEach(x -> x.getValue().equals("126"));
	// }
	//
	// public void test001_disablePropertyConstraint_setInstance() {
	// Engine engine = new Engine();
	// Generic vehicle = engine.addInstance("Vehicle");
	// Generic power = engine.addInstance("Power", vehicle);
	// power.enablePropertyConstraint();
	// assert power.isPropertyConstraintEnabled();
	// power.setInstance("123", vehicle);
	// power.setInstance("126", vehicle);
	// assert power.getInstances().size() == 1;
	// power.getInstances().forEach(x -> x.getValue().equals("126"));
	// power.disablePropertyConstraint();
	// assert !power.isPropertyConstraintEnabled();
	// power.setInstance("123", vehicle);
	// assert power.getInstances().size() == 2;
	// }

}
