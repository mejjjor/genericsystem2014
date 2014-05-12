package org.genericsystem.kernel;

import org.testng.annotations.Test;

@Test
public class AttributesTest extends AbstractTest {

	public void test1Attribut() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		assert power.getComponentsStream().count() == 1;
		assert vehicle.equals(power.getComponents()[0]);
		assert power.isAlive();
	}

	public void test1AttributWith2LevelsInheritance1AttributOnParent() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		assert vehicle.getAttributes(root).size() == 1;
		assert vehicle.getAttributes(root).contains(power);
		assert car.getAttributes(root).size() == 1;
		assert car.getAttributes(root).contains(power);
	}

	public void test1AttributWith2LevelsInheritance1AttributOnFistChild() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex power = root.addInstance("Power", car);
		assert vehicle.getAttributes(root).size() == 0;
		assert car.getAttributes(root).size() == 1;
		assert car.getAttributes(root).contains(power);
	}

	public void test1AttributWith3LevelsInheritance1AttributOnParent() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex microcar = root.addInstance(new Vertex[] { car }, "Microcar");
		assert vehicle.getAttributes(root).size() == 1;
		assert vehicle.getAttributes(root).contains(power);
		assert car.getAttributes(root).size() == 1;
		assert car.getAttributes(root).contains(power);
		assert microcar.getAttributes(root).size() == 1;
		assert microcar.getAttributes(root).contains(power);
	}

	public void test1AttributWith3LevelsInheritance1AttributOnFirstChild() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex power = root.addInstance("Power", car);
		Vertex microcar = root.addInstance(new Vertex[] { car }, "Microcar");
		assert vehicle.getAttributes(root).size() == 0;
		assert car.getAttributes(root).size() == 1;
		assert car.getAttributes(root).contains(power);
		assert microcar.getAttributes(root).size() == 1;
		assert microcar.getAttributes(root).contains(power);
	}

	public void test1AttributWith3LevelsInheritance1AttributOnSecondChild() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex microcar = root.addInstance(new Vertex[] { car }, "Microcar");
		Vertex power = root.addInstance("Power", microcar);
		assert vehicle.getAttributes(root).size() == 0;
		assert car.getAttributes(root).size() == 0;
		assert microcar.getAttributes(root).size() == 1;
		assert microcar.getAttributes(root).contains(power);
	}

	/*
	 * public void testSimple1MetaAttribut() { Root root = new Root(); Vertex car = root.addInstance("Car"); Vertex power = root.addInstance("Power", car); assert power.getComponentsStream().count() == 1; assert car.equals(power.getComponents()[0]); assert
	 * power.isAlive(); }
	 */
	public void test2Attributs() {

		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex airconditioner = root.addInstance("AirConditioner", vehicle);
		assert vehicle.getAttributes(root).size() == 2;
		assert vehicle.getAttributes(root).contains(power);
		assert vehicle.getAttributes(root).contains(airconditioner);
		assert power.isAlive();
		assert airconditioner.isAlive();
	}

	public void test2AttributsWith2LevelsInheritance2AttributsOnParent() {

		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex airconditioner = root.addInstance("AirConditioner", vehicle);
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		assert vehicle.getAttributes(root).size() == 2;
		assert vehicle.getAttributes(root).contains(power);
		assert vehicle.getAttributes(root).contains(airconditioner);
		assert car.getAttributes(root).size() == 2;
		assert car.getAttributes(root).contains(power);
		assert car.getAttributes(root).contains(airconditioner);
	}

	public void test2AttributsWith2LevelsInheritance2AttributsOnFistChild() {

		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex power = root.addInstance("Power", car);
		Vertex airconditioner = root.addInstance("AirConditioner", car);
		assert vehicle.getAttributes(root).size() == 0;
		assert car.getAttributes(root).size() == 2;
		assert car.getAttributes(root).contains(power);
		assert car.getAttributes(root).contains(airconditioner);
	}

	public void test2AttributsWith2LevelsInheritance1AttributOnParentAnd1AttributOnFistChild() {

		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex airconditioner = root.addInstance("AirConditioner", car);
		assert vehicle.getAttributes(root).size() == 1;
		assert vehicle.getAttributes(root).contains(power);
		assert car.getAttributes(root).size() == 2;
		assert car.getAttributes(root).contains(power);
		assert car.getAttributes(root).contains(airconditioner);
	}

	public void test1AttributWith3LevelsInheritance2AttributOnParent() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex airconditioner = root.addInstance("AirConditioner", vehicle);
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex microcar = root.addInstance(new Vertex[] { car }, "Microcar");
		assert vehicle.getAttributes(root).size() == 2;
		assert vehicle.getAttributes(root).contains(power);
		assert vehicle.getAttributes(root).contains(airconditioner);
		assert car.getAttributes(root).size() == 2;
		assert car.getAttributes(root).contains(power);
		assert car.getAttributes(root).contains(airconditioner);
		assert microcar.getAttributes(root).size() == 2;
		assert microcar.getAttributes(root).contains(power);
		assert microcar.getAttributes(root).contains(airconditioner);
	}

	public void test1AttributWith3LevelsInheritance2AttributFirstChild() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex power = root.addInstance("Power", car);
		Vertex airconditioner = root.addInstance("AirConditioner", car);
		Vertex microcar = root.addInstance(new Vertex[] { car }, "Microcar");
		assert vehicle.getAttributes(root).size() == 0;
		assert car.getAttributes(root).size() == 2;
		assert car.getAttributes(root).contains(power);
		assert car.getAttributes(root).contains(airconditioner);
		assert microcar.getAttributes(root).size() == 2;
		assert microcar.getAttributes(root).contains(power);
		assert microcar.getAttributes(root).contains(airconditioner);
	}

	public void test1AttributWith3LevelsInheritance2AttributOnSecondChild() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex microcar = root.addInstance(new Vertex[] { car }, "Microcar");
		Vertex power = root.addInstance("Power", microcar);
		Vertex airconditioner = root.addInstance("AirConditioner", microcar);
		assert vehicle.getAttributes(root).size() == 0;
		assert car.getAttributes(root).size() == 0;
		assert microcar.getAttributes(root).size() == 2;
		assert microcar.getAttributes(root).contains(power);
		assert microcar.getAttributes(root).contains(airconditioner);
	}

	public void test1AttributWith3LevelsInheritance1AttributOnParent1AttributOnFirstChild() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex airconditioner = root.addInstance("AirConditioner", car);
		Vertex microcar = root.addInstance(new Vertex[] { car }, "Microcar");
		assert vehicle.getAttributes(root).size() == 1;
		assert vehicle.getAttributes(root).contains(power);
		assert car.getAttributes(root).size() == 2;
		assert car.getAttributes(root).contains(power);
		assert car.getAttributes(root).contains(airconditioner);
		assert microcar.getAttributes(root).size() == 2;
		assert microcar.getAttributes(root).contains(power);
		assert microcar.getAttributes(root).contains(airconditioner);
	}

	public void test1AttributWith3LevelsInheritance1AttributOnParent1AttributOnSecondChild() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex microcar = root.addInstance(new Vertex[] { car }, "Microcar");
		Vertex airconditioner = root.addInstance("AirConditioner", microcar);
		assert vehicle.getAttributes(root).size() == 1;
		assert vehicle.getAttributes(root).contains(power);
		assert car.getAttributes(root).size() == 1;
		assert car.getAttributes(root).contains(power);
		assert microcar.getAttributes(root).size() == 2;
		assert microcar.getAttributes(root).contains(power);
		assert microcar.getAttributes(root).contains(airconditioner);
	}

	public void test1AttributWith3LevelsInheritance1AttributFirstChild1AttributOnSecondChild() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex power = root.addInstance("Power", car);
		Vertex microcar = root.addInstance(new Vertex[] { car }, "Microcar");
		Vertex airconditioner = root.addInstance("AirConditioner", microcar);
		assert vehicle.getAttributes(root).size() == 0;
		assert car.getAttributes(root).size() == 1;
		assert car.getAttributes(root).contains(power);
		assert microcar.getAttributes(root).size() == 2;
		assert microcar.getAttributes(root).contains(power);
		assert microcar.getAttributes(root).contains(airconditioner);
	}

	public void test1AttributWith2LevelsInheritance2ChildrenAt2ndLevel1AttributOnParent() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex caravan = root.addInstance(new Vertex[] { vehicle }, "Caravan");
		assert vehicle.getAttributes(root).size() == 1;
		assert vehicle.getAttributes(root).contains(power);
		assert car.getAttributes(root).size() == 1;
		assert car.getAttributes(root).contains(power);
		assert caravan.getAttributes(root).size() == 1;
		assert caravan.getAttributes(root).contains(power);
	}

	public void test1AttributWith2LevelsInheritance2ChildrenAt2ndLevel1AttributOnLevel1FirstChild() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex power = root.addInstance("Power", car);
		Vertex caravan = root.addInstance(new Vertex[] { vehicle }, "Caravan");
		assert vehicle.getAttributes(root).size() == 0;
		assert car.getAttributes(root).size() == 1;
		assert car.getAttributes(root).contains(power);
		assert caravan.getAttributes(root).size() == 0;
	}

	public void test1AttributWith2LevelsInheritance2ChildrenAt2ndLevel1AttributOnLevel1SecondChild() {
		Root root = new Root();
		Vertex vehicle = root.addInstance("Vehicle");
		Vertex car = root.addInstance(new Vertex[] { vehicle }, "Car");
		Vertex caravan = root.addInstance(new Vertex[] { vehicle }, "Caravan");
		Vertex power = root.addInstance("Power", caravan);
		assert vehicle.getAttributes(root).size() == 0;
		assert car.getAttributes(root).size() == 0;
		assert caravan.getAttributes(root).size() == 1;
		assert caravan.getAttributes(root).contains(power);
	}

	public void test1AttributWith3LevelsInheritance2ChildrenAt2ndLevel1ChildAtThirdLevel1AttributOnParent() {
		Vertex root = new Root();
		Vertex object = root.addInstance("Object");
		Vertex power = root.addInstance("Power", object);
		Vertex vehicle = root.addInstance(new Vertex[] { object }, "Vehicle");
		Vertex robot = root.addInstance(new Vertex[] { object }, "Robot");
		Vertex transformer = root.addInstance(new Vertex[] { vehicle, robot }, "Transformer");

		assert object.getAttributes(root).size() == 1;
		assert object.getAttributes(root).contains(power);
		assert vehicle.getAttributes(root).size() == 1;
		assert vehicle.getAttributes(root).contains(power);
		assert robot.getAttributes(root).size() == 1;
		assert robot.getAttributes(root).contains(power);
		assert transformer.getAttributes(root).size() == 1;
		assert transformer.getAttributes(root).contains(power);
	}

	public void test1AttributWith3LevelsInheritance2ChildrenAt2ndLevel1ChildAtThirdLevel1AttributLevel1FistChild() {
		Vertex root = new Root();
		Vertex object = root.addInstance("Object");
		Vertex vehicle = root.addInstance(new Vertex[] { object }, "Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex robot = root.addInstance(new Vertex[] { object }, "Robot");
		Vertex transformer = root.addInstance(new Vertex[] { vehicle, robot }, "Transformer");

		assert object.getAttributes(root).size() == 0;
		assert vehicle.getAttributes(root).size() == 1;
		assert vehicle.getAttributes(root).contains(power);
		assert robot.getAttributes(root).size() == 0;
		assert transformer.getAttributes(root).size() == 1;
		assert transformer.getAttributes(root).contains(power);
	}

	public void test1AttributWith3LevelsInheritance2ChildrenAt2ndLevel1ChildAtThirdLevel1AttributLevel1SecondChild() {
		Vertex root = new Root();
		Vertex object = root.addInstance("Object");
		Vertex vehicle = root.addInstance(new Vertex[] { object }, "Vehicle");
		Vertex robot = root.addInstance(new Vertex[] { object }, "Robot");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex transformer = root.addInstance(new Vertex[] { vehicle, robot }, "Transformer");

		assert object.getAttributes(root).size() == 0;
		assert vehicle.getAttributes(root).size() == 1;
		assert vehicle.getAttributes(root).contains(power);
		assert robot.getAttributes(root).size() == 0;
		assert transformer.getAttributes(root).size() == 1;
		assert transformer.getAttributes(root).contains(power);
	}

	public void test1AttributWith3LevelsInheritance2ChildrenAt2ndLevel1ChildAtThirdLevel1AttributLevel2Child1() {
		Vertex root = new Root();
		Vertex object = root.addInstance("Object");
		Vertex vehicle = root.addInstance(new Vertex[] { object }, "Vehicle");
		Vertex robot = root.addInstance(new Vertex[] { object }, "Robot");
		Vertex transformer = root.addInstance(new Vertex[] { vehicle, robot }, "Transformer");
		Vertex power = root.addInstance("Power", transformer);
		assert object.getAttributes(root).size() == 0;
		assert vehicle.getAttributes(root).size() == 0;
		assert robot.getAttributes(root).size() == 0;
		assert transformer.getAttributes(root).size() == 1;
		assert transformer.getAttributes(root).contains(power);
	}

	public void test2AttributsWith3LevelsInheritance2ChildrenAt2ndLevel1ChildAtThirdLevel2AttributsOnParent() {
		Vertex root = new Root();
		Vertex object = root.addInstance("Object");
		Vertex power = root.addInstance("Power", object);
		Vertex airconditioner = root.addInstance("AirConditioner", object);

		Vertex vehicle = root.addInstance(new Vertex[] { object }, "Vehicle");
		Vertex robot = root.addInstance(new Vertex[] { object }, "Robot");
		Vertex transformer = root.addInstance(new Vertex[] { vehicle, robot }, "Transformer");

		assert object.getAttributes(root).size() == 2;
		assert object.getAttributes(root).contains(power);
		assert object.getAttributes(root).contains(airconditioner);
		assert vehicle.getAttributes(root).size() == 2;
		assert vehicle.getAttributes(root).contains(power);
		assert vehicle.getAttributes(root).contains(airconditioner);
		assert robot.getAttributes(root).size() == 2;
		assert robot.getAttributes(root).contains(power);
		assert robot.getAttributes(root).contains(airconditioner);
		assert transformer.getAttributes(root).size() == 2;
		assert transformer.getAttributes(root).contains(power);
		assert transformer.getAttributes(root).contains(airconditioner);
	}

	public void test2AttributsWith3LevelsInheritance2ChildrenAt2ndLevel1ChildAtThirdLevel2AttributsLevel1FirstChild() {
		Vertex root = new Root();
		Vertex object = root.addInstance("Object");
		Vertex vehicle = root.addInstance(new Vertex[] { object }, "Vehicle");
		Vertex power = root.addInstance("Power", vehicle);
		Vertex airconditioner = root.addInstance("AirConditioner", vehicle);
		Vertex robot = root.addInstance(new Vertex[] { object }, "Robot");
		Vertex transformer = root.addInstance(new Vertex[] { vehicle, robot }, "Transformer");

		assert object.getAttributes(root).size() == 0;
		assert vehicle.getAttributes(root).size() == 2;
		assert vehicle.getAttributes(root).contains(power);
		assert vehicle.getAttributes(root).contains(airconditioner);
		assert robot.getAttributes(root).size() == 0;
		assert transformer.getAttributes(root).size() == 2;
		assert transformer.getAttributes(root).contains(power);
		assert transformer.getAttributes(root).contains(airconditioner);
	}

	public void test2AttributsWith3LevelsInheritance2ChildrenAt2ndLevel1ChildAtThirdLevel2AttributsLevel1SecondChild() {
		Vertex root = new Root();
		Vertex object = root.addInstance("Object");
		Vertex vehicle = root.addInstance(new Vertex[] { object }, "Vehicle");
		Vertex robot = root.addInstance(new Vertex[] { object }, "Robot");
		Vertex power = root.addInstance("Power", robot);
		Vertex airconditioner = root.addInstance("AirConditioner", robot);
		Vertex transformer = root.addInstance(new Vertex[] { vehicle, robot }, "Transformer");

		assert object.getAttributes(root).size() == 0;
		assert vehicle.getAttributes(root).size() == 0;
		assert robot.getAttributes(root).size() == 2;
		assert robot.getAttributes(root).contains(power);
		assert robot.getAttributes(root).contains(airconditioner);
		assert transformer.getAttributes(root).size() == 2;
		assert transformer.getAttributes(root).contains(power);
		assert transformer.getAttributes(root).contains(airconditioner);
	}

	public void test2AttributsWith3LevelsInheritance2ChildrenAt2ndLevel1ChildAtThirdLevel2AttributsLevel2() {
		Vertex root = new Root();
		Vertex object = root.addInstance("Object");
		Vertex vehicle = root.addInstance(new Vertex[] { object }, "Vehicle");
		Vertex robot = root.addInstance(new Vertex[] { object }, "Robot");
		Vertex transformer = root.addInstance(new Vertex[] { vehicle, robot }, "Transformer");
		Vertex power = root.addInstance("Power", transformer);
		Vertex airconditioner = root.addInstance("AirConditioner", transformer);
		assert object.getAttributes(root).size() == 0;
		assert vehicle.getAttributes(root).size() == 0;
		assert robot.getAttributes(root).size() == 0;
		assert transformer.getAttributes(root).size() == 2;
		assert transformer.getAttributes(root).contains(power);
		assert transformer.getAttributes(root).contains(airconditioner);
	}

}
