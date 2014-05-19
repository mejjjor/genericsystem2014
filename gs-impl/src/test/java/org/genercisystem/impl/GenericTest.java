package org.genercisystem.impl;

import java.util.Arrays;

import org.genericsystem.impl.Engine;
import org.genericsystem.impl.Generic;
import org.genericsystem.kernel.Statics;
import org.genericsystem.kernel.Vertex;
import org.testng.annotations.Test;

@Test
public class GenericTest extends AbstractTest {

	public void testEngine() {
		Engine engine = new Engine();
		assert engine.getAlive() != null;
		assert engine.getComponentsStream().count() == 0;
		assert engine.getInheritings().stream().count() == 0;
		assert engine.getInstances().stream().count() == 0;
		assert engine.getLevel() == 0;
		assert engine.getMeta().equals(engine);
		assert engine.getRoot().equals(engine);
		assert engine.getSupersStream().count() == 0;
		assert engine.getValue().equals(Statics.ENGINE_VALUE);
		assert engine.isAlive();
		assert engine.isMeta();
		assert engine.isRoot();
	}

	public void testGetInstances() {
		Engine engine = new Engine();
		assert engine.getInstances().isEmpty();
		Vertex vehicleVertex = engine.getAlive().addInstance("Vehicle");
		Vertex powerVehicleVertex = engine.getAlive().addInstance("Power", vehicleVertex);
		Generic vehicle = engine.getInstances().filter(g -> g.getValue().equals("Vehicle")).stream().findFirst().get();
		Generic powerVehicle = engine.getInstances().filter(g -> g.getValue().equals("Power")).stream().findFirst().get();
		assert vehicle.getAlive().equiv(vehicleVertex) : engine.getInstances();
		assert powerVehicle.getAlive().equiv(powerVehicleVertex) : engine.getInstances();
	}

	public void testAddInstance() {
		Engine engine = new Engine();
		Generic vehicle = engine.addInstance("Vehicle");
		engine.getInstances().iterator().next();
	}

	// public void test() {
	// Vertex engine = new Root();
	// Vertex vehicle = engine.addInstance("Vehicle");
	// Vertex vehicle2 = engine.addInstance("Vehicle2");
	// assert !vehicle2.inheritsFrom(vehicle);
	// assert vehicle == engine.setInstance("Vehicle");
	// // assert vehicle == engine.setInstance(new Vertex[] { vehicle2 }, "Vehicle");
	// Vertex car = engine.addInstance(new Vertex[] { vehicle }, "Car");
	// assert car.inheritsFrom(vehicle);
	// Vertex power = engine.addInstance("Power", car);
	// Vertex myBmw = car.addInstance("myBmw");
	// assert myBmw.isInstanceOf(car);
	// Vertex v233 = power.addInstance(233, myBmw);
	// Vertex color = engine.addInstance("Color");
	// Vertex red = color.addInstance("red");
	// Vertex green = color.addInstance("green");
	// Vertex yellow = color.addInstance("yellow");
	// assert !yellow.getSupersStream().anyMatch(red::equals);
	// Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
	// log.info("" + car.info() + power.info() + myBmw.info() + v233.info() + yellow.info());
	// assert engine.getInstances().containsAll(Arrays.asList(vehicle, car));
	// assert car.getInstances().contains(myBmw) : car.getInstances() + car.info();
	// assert power.getInstances().contains(v233);
	// assert car.getMetaComposites(power.getMeta()).contains(power);
	// assert car.getSupersStream().findFirst().get() == vehicle : car.getSupersStream().findFirst().get().info();
	// assert car.getSupersStream().anyMatch(vehicle::equals);
	// assert vehicle.getInheritings().contains(car);
	// assert myBmw.getMetaComposites(v233.getMeta()).contains(v233);
	// assert myBmw.isInstanceOf(car);
	// assert myBmw.isInstanceOf(vehicle);
	// assert !myBmw.isInstanceOf(engine);
	// assert vehicle.isInstanceOf(engine);
	// assert !vehicle.inheritsFrom(engine) : vehicle.getLevel() + " " + engine.getLevel() + " " + vehicle.equals(engine);
	// assert car.inheritsFrom(vehicle);
	// assert !car.isInstanceOf(vehicle);
	// assert !power.inheritsFrom(engine);
	// assert !v233.inheritsFrom(power);
	// assert v233.isInstanceOf(power);
	// assert engine.getInstance("Car") != null;
	// assert power.getInstance(233, myBmw) != null;
	// Vertex carRed = vehicleColor.addInstance("CarRed", car, red);
	// Vertex carGreen = vehicleColor.addInstance("CarGreen", car, green);
	// assert carRed.isSuperOf(vehicleColor, new Vertex[] { carRed }, "myBmwRed", myBmw, red);
	// assert !carRed.isSuperOf(vehicleColor, new Vertex[] {}, "myBmwRed", red, red);
	// assert carRed.isSuperOf(vehicleColor, new Vertex[] { carRed }, "CarRed", myBmw, red);
	// log.info("**************************************************************" + carGreen.info());
	// assert carGreen.isInstanceOf(vehicleColor);
	// assert vehicleColor.getInstances().contains(carGreen);
	//
	// Vertex myBmwYellow = vehicleColor.addInstance(new Vertex[] { carGreen }, "CarRed", myBmw, red);
	// assert carRed.isSuperOf(vehicleColor, new Vertex[] { carGreen }, "CarRed", myBmw, red);
	// assert myBmwYellow.inheritsFrom(carRed);
	// log.info(myBmwYellow.info());
	//
	// Vertex myBmwRed = vehicleColor.addInstance(new Vertex[] { carRed }, "myBmwRed", myBmw, red);
	// log.info(myBmwRed.info());
	// assert !yellow.inheritsFrom(red);
	// assert !yellow.isInstanceOf(red);
	// assert myBmwRed == vehicleColor.setInstance("myBmwRed", myBmw, red);
	// assert myBmwRed == vehicleColor.getInstance("myBmwRed", myBmw, red) : vehicleColor.getInstance("myBmwRed", myBmw, red).info();
	//
	// assert myBmwRed.inheritsFrom(carRed);
	// assert car.getAttributes(engine).contains(power) : car.getAttributes(engine);
	// assert car.getAttributes(engine).contains(vehicleColor) : car.getAttributes(engine);
	// assert car.getAttributes(engine).size() == 2 : car.getAttributes(engine);
	// assert !myBmwRed.inheritsFrom(power);
	// assert !v233.inheritsFrom(power);
	// assert v233.isInstanceOf(power);
	//
	// assert myBmw.getHolders(power).contains(v233) : myBmw.getHolders(power);
	// assert myBmw.getHolders(power).size() == 1 : myBmw.getHolders(engine);
	// assert myBmw.getValues(power).contains(233);
	// assert engine.isAttributeOf(myBmw);
	// log.info("----------------------------------");
	//
	// assert car.getAttributes(engine).equals(myBmw.getAttributes(engine)) : car.getAttributes(engine) + " " + myBmw.getAttributes(engine);
	// }

	// @Test(enabled = false)
	// public void test2() {
	// Vertex engine = new Root();
	// Vertex vehicle = engine.addInstance("Vehicle");
	// Vertex vehicle2 = engine.addInstance("Vehicle2");
	// assert vehicle == engine.setInstance("Vehicle");
	// assert vehicle != engine.setInstance(new Vertex[] { vehicle2 }, "Vehicle");
	// }
	//

	public void test3() {
		Engine engine = new Engine();

		assert engine.isRoot();
		Generic vehicle = engine.addInstance("Vehicle");
		assert vehicle.isAlive();
		Generic car = engine.addInstance(Arrays.asList(vehicle), "Car");
		Generic vehiclePower = engine.addInstance("VehiclePower", vehicle);
		Generic carPower = engine.addInstance("CarPower", car);
		assert car.getAttributes(engine).containsAll(Arrays.asList(vehiclePower, carPower)) : car.getAttributes(engine);
		assert car.getAttributes(engine).size() == 2;
	}

	//
	// public void test4() {
	// Vertex engine = new Root();
	// Vertex vehicle = engine.addInstance("Vehicle");
	// Vertex car = engine.addInstance(new Vertex[] { vehicle }, "Car");
	// Vertex vehiclePower = engine.addInstance("Power", vehicle);
	// Vertex carPower = engine.addInstance("Power", car);
	// assert car.getAttributes(engine).contains(carPower);
	// assert car.getAttributes(engine).size() == 1 : car.getAttributes(engine);
	// }
	//
	// public void test5() {
	// Vertex engine = new Root();
	// Vertex vehicle = engine.addInstance("Vehicle");
	// Vertex car = engine.addInstance(new Vertex[] { vehicle }, "Car");
	// Vertex vehiclePower = engine.addInstance("VehiclePower", vehicle);
	// Vertex carPower = engine.addInstance(new Vertex[] { vehiclePower }, "CarPower", car);
	// assert car.getAttributes(engine).contains(carPower);
	// assert car.getAttributes(engine).size() == 1 : car.getAttributes(engine);
	// }
	//
	// public void test6() {
	// Vertex engine = new Root();
	// Vertex vehicle = engine.addInstance("Vehicle");
	// Vertex car = engine.addInstance(new Vertex[] { vehicle }, "Car");
	// Vertex sportCar = engine.addInstance(new Vertex[] { car }, "SportCar");
	// Vertex vehiclePower = engine.addInstance("VehiclePower", vehicle);
	// Vertex carPower = engine.addInstance(new Vertex[] { vehiclePower }, "CarPower", car);
	// Vertex sportCarPower = engine.addInstance(new Vertex[] { vehiclePower }, "SportCarPower", sportCar);
	// assert sportCar.getAttributes(engine).containsAll(Arrays.asList(carPower, sportCarPower)) : car.getAttributes(engine) + " " + sportCarPower.info();
	// assert sportCar.getAttributes(engine).size() == 2;
	// }
	//
	// public void test7() {
	// Vertex engine = new Root();
	// Vertex vehicle = engine.addInstance("Vehicle");
	// Vertex robot = engine.addInstance("robot");
	// Vertex transformer = engine.addInstance(new Vertex[] { vehicle, robot }, "Transformer");
	// Vertex vehiclePower = engine.addInstance("Power", vehicle);
	// Vertex robotPower = engine.addInstance("Power", robot);
	// assert transformer.getAttributes(engine).containsAll(Arrays.asList(robotPower, vehiclePower)) : transformer.getAttributes(engine);
	// assert transformer.getAttributes(engine).size() == 2;
	// }
	//
	// public void test8() {
	// Vertex engine = new Root();
	// Vertex vehicle = engine.addInstance("Vehicle");
	// Vertex robot = engine.addInstance("robot");
	// Vertex transformer = engine.addInstance(new Vertex[] { vehicle, robot }, "Transformer");
	// Vertex vehiclePower = engine.addInstance("VehiclePower", vehicle);
	// Vertex robotPower = engine.addInstance("RobotPower", robot);
	// Vertex transformerPower = engine.addInstance(new Vertex[] { vehiclePower, robotPower }, "TransformerPower", transformer);
	// assert transformer.getAttributes(engine).contains(transformerPower) : transformer.getAttributes(engine);
	// assert transformer.getAttributes(engine).size() == 1;
	// }
	//
	// public void test9() {
	// Vertex engine = new Root();
	// Vertex vehicle = engine.addInstance("Vehicle");
	// Vertex robot = engine.addInstance("robot");
	// Vertex transformer = engine.addInstance(new Vertex[] { vehicle, robot }, "Transformer");
	// Vertex vehiclePower = engine.addInstance("Power", vehicle);
	// Vertex robotPower = engine.addInstance("Power", robot);
	// Vertex transformerPower = engine.addInstance("Power", transformer);
	// assert transformer.getAttributes(engine).contains(transformerPower) : transformer.getAttributes(engine);
	// assert transformer.getAttributes(engine).size() == 1;
	// assert transformer.getAttributes(robot).size() == 0 : transformer.getAttributes(robot);
	// }
	//
	// public void test10() {
	// Vertex engine = new Root();
	// Vertex vehicle = engine.addInstance("Vehicle");
	// engine.removeInstance("Vehicle");
	// new RollbackCatcher() {
	// @Override
	// public void intercept() {
	// engine.addInstance(new Vertex[] { vehicle }, "Car");
	// }
	// }.assertIsCausedBy(NotAliveException.class);
	// }
	//
	// public void test11() {
	// Vertex engine = new Root();
	// Vertex vehicle = engine.addInstance("Vehicle");
	// engine.removeInstance("Vehicle");
	// new RollbackCatcher() {
	// @Override
	// public void intercept() {
	// vehicle.addInstance("myVehicle");
	// }
	// }.assertIsCausedBy(NotAliveException.class);
	// }
}
