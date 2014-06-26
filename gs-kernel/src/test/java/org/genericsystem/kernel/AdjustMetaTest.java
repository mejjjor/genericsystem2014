package org.genericsystem.kernel;

import java.util.Arrays;
import java.util.Collections;

import org.testng.annotations.Test;

@Test
public class AdjustMetaTest extends AbstractTest {

	public void test001_AdjustMeta_MetaLevel_metaAttribut_NoComponent() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		assert engine == engine.adjustMeta(Collections.emptyList(), "Power", Collections.emptyList());
	}

	public void test002_AdjustMeta_MetaLevel_metaAttribut_OneComponent() {
		Root engine = new Root();
		Vertex metaAttribute = engine.addInstance(engine.getValue(), engine);
		Vertex car = engine.addInstance("Car");
		assert metaAttribute == engine.adjustMeta(Collections.emptyList(), "Power", Collections.singletonList(car));
	}

	public void test003_AdjustMeta_MetaLevel_metaAttribut_TwoComponents() {
		Root engine = new Root();
		Vertex metaAttribute = engine.addInstance(engine.getValue(), engine);
		Vertex car = engine.addInstance("Car");
		Vertex color = engine.addInstance("Color");
		assert metaAttribute == engine.adjustMeta(Collections.emptyList(), "CarColor", Arrays.asList(car, color));
	}

	public void test004_AdjustMeta_MetaLevel_metaAttribut() {
		Root engine = new Root();
		Vertex metaAttribute = engine.addInstance(engine.getValue(), engine);
		Vertex robot = engine.addInstance("Robot");
		Vertex car = engine.addInstance("Car");
		Vertex color = engine.addInstance("Color");
		assert metaAttribute == engine.adjustMeta(Collections.emptyList(), "CarColor", Arrays.asList(car, color));
		Vertex carColor = engine.addInstance("CarColor", car, color);
		assert metaAttribute == engine.adjustMeta(Collections.emptyList(), "Radio", Arrays.asList(car));
	}

	public void test005_AdjustMeta_MetaLevel_metaRelation_ThreeComponents() {
		Root engine = new Root();
		Vertex metaAttribute = engine.addInstance(engine.getValue(), engine);
		assert metaAttribute == engine.adjustMeta(Collections.emptyList(), engine.getValue(), Arrays.asList(engine, engine));
		Vertex metaRelation = engine.addInstance(engine.getValue(), engine, engine);
		Vertex car = engine.addInstance("Car");
		Vertex color = engine.addInstance("Color");
		Vertex carColor = engine.addInstance("CarColor", car, color);
		Vertex finition = engine.addInstance("Finition");
		assert metaRelation == engine.adjustMeta(Collections.emptyList(), "CarColorFinition", Arrays.asList(car, color, finition));
	}

	public void test006_AdjustMeta_TypeLevel_Relation_TwoComponents() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex color = engine.addInstance("Color");
		Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
		Vertex car = vehicle.addInstance("Car");
		Vertex red = color.addInstance("Red");
		assert vehicleColor == vehicleColor.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red)) : engine.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red));
	}

	public void test007_AdjustMeta_TypeLevel_Relation_TwoComponents_oneComponentSpecializedByInheritance() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex color = engine.addInstance("Color");
		Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
		Vertex color2 = engine.addInstance(color, "Color2");
		Vertex car = vehicle.addInstance("Car");
		Vertex red = color2.addInstance("Red");
		assert vehicleColor == vehicleColor.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red)) : engine.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red));
	}

	public void test008_AdjustMeta_TypeLevel_Relation_TwoComponents_oneComponentSpecializedByInstanciation() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex color = engine.addInstance("Color");
		Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
		Vertex color2 = color.addInstance("Color2");
		Vertex car = vehicle.addInstance("Car");
		Vertex red = color2.addInstance("Red");
		assert vehicleColor == vehicleColor.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red)) : engine.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red));
	}

	public void test009_AdjustMeta_TypeLevel_Relation_TwoComponents_TwoComponentSpecializedByInheritance() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex color = engine.addInstance("Color");
		Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
		Vertex vehicle2 = engine.addInstance(vehicle, "Vehicle2");
		Vertex color2 = engine.addInstance(color, "Color2");
		Vertex car = vehicle2.addInstance("Car");
		Vertex red = color2.addInstance("Red");
		assert vehicleColor == vehicleColor.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red)) : engine.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red));
	}

	public void test010_AdjustMeta_TypeLevel_Relation_TwoComponents_TwoComponentSpecializedByInstanciation() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex color = engine.addInstance("Color");
		Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
		Vertex vehicle2 = engine.addInstance("Vehicle2");
		Vertex color2 = color.addInstance("Color2");
		Vertex car = vehicle2.addInstance("Car");
		Vertex red = color2.addInstance("Red");
		assert vehicleColor == vehicleColor.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red)) : engine.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red));
	}

	public void test011_AdjustMeta_TypeLevel_Relation_TwoComponents_TwoComponentSpecialized() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex color = engine.addInstance("Color");
		Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
		Vertex vehicle2 = engine.addInstance(vehicle, "Vehicle2");
		Vertex color2 = color.addInstance("Color2");
		Vertex car = vehicle2.addInstance("Car");
		Vertex red = color2.addInstance("Red");
		assert vehicleColor == vehicleColor.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red)) : engine.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red));
	}

	public void test012_AdjustMeta_TypeLevel_Relation_ThreeComponents() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex color = engine.addInstance("Color");
		Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
		Vertex vehicle2 = engine.addInstance(vehicle, "Vehicle2");
		Vertex color2 = color.addInstance("Color2");
		Vertex finition = engine.addInstance("Finition");
		Vertex car = vehicle2.addInstance("Car");
		Vertex red = color2.addInstance("Red");

		assert vehicleColor == vehicleColor.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red, finition)) : engine.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red, finition));
	}

	public void test013_AdjustMeta_TypeLevel_Relation_ThreeComponents() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex color = engine.addInstance("Color");
		Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
		Vertex vehicle2 = engine.addInstance(vehicle, "Vehicle2");
		Vertex vehicleColor2 = engine.addInstance(vehicleColor, "VehicleColor2", vehicle2, color);
		Vertex color2 = color.addInstance("Color2");
		Vertex finition = engine.addInstance("Finition");
		Vertex car = vehicle2.addInstance("Car");
		Vertex red = color2.addInstance("Red");

		assert vehicleColor2 == vehicleColor.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red, finition)) : engine.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red, finition));
	}

	public void test014_AdjustMeta_TypeLevel_Relation_ThreeComponents() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex color = engine.addInstance("Color");
		Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
		Vertex vehicle2 = engine.addInstance(vehicle, "Vehicle2");
		Vertex color2 = color.addInstance("Color2");
		Vertex vehicleColor2 = engine.addInstance(vehicleColor, "VehicleColor2", vehicle2, color2);
		Vertex finition = engine.addInstance("Finition");
		Vertex car = vehicle2.addInstance("Car");
		Vertex red = color2.addInstance("Red");
		assert vehicleColor2 == vehicleColor.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red, finition)) : engine.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red, finition));
	}

	public void test015_AdjustMeta_TypeLevel_Relation_ThreeComponents() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex color = engine.addInstance("Color");
		Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
		Vertex vehicle2 = engine.addInstance(vehicle, "Vehicle2");
		Vertex color2 = color.addInstance("Color2");
		Vertex finition = engine.addInstance("Finition");
		Vertex car = vehicle2.addInstance("Car");
		Vertex vehicleColor2 = engine.addInstance(vehicleColor, "VehicleColor2", car, color2);
		Vertex red = color2.addInstance("Red");
		assert vehicleColor2 == vehicleColor.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red, finition)) : engine.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red, finition));
	}

	public void test016_AdjustMeta_TypeLevel_Relation_ThreeComponents() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex color = engine.addInstance("Color");
		Vertex vehicleColor = engine.addInstance("VehicleColor", vehicle, color);
		Vertex vehicle2 = engine.addInstance(vehicle, "Vehicle2");
		Vertex vehicleColor2 = engine.addInstance(vehicleColor, "VehicleColor2", vehicle2, color);
		Vertex color2 = color.addInstance("Color2");
		Vertex finition = engine.addInstance("Finition");
		Vertex car = vehicle2.addInstance("Car");
		Vertex vehicleColor3 = engine.addInstance(vehicleColor2, "VehicleColor3", car, color2);
		Vertex red = color2.addInstance("Red");
		assert vehicleColor3 == vehicleColor.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red, finition)) : engine.adjustMeta(Collections.emptyList(), "CarRed", Arrays.asList(car, red, finition));
	}

	public void test017_AdjustMeta_TypeLevel_Relation_ThreeComponents() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex vehicle2 = engine.addInstance(vehicle, "Vehicle2");
		Vertex vehicleVehicle2 = engine.addInstance("VehicleVehicle2", vehicle, vehicle2);
		Vertex power = engine.addInstance("Power", vehicle);
		Vertex intensity = engine.addInstance(power, "Intensity", vehicle2);
		Vertex unit = engine.addInstance("Unit", power);
		Vertex intensityUnit = engine.addInstance(unit, "Unit", intensity);
		Vertex car = vehicle.addInstance("Car");
		Vertex bus = vehicle2.addInstance("Bus");
		power.addInstance(100, car);
		intensity.addInstance(110, bus);
		unit.addInstance("Watt", power);
		intensityUnit.addInstance("KWatt", intensity);
		Vertex vehicleBus = engine.addInstance(vehicleVehicle2, "VehicleBus", vehicle, bus);
		assert vehicleBus == vehicleVehicle2.adjustMeta(Collections.emptyList(), "carBus", Arrays.asList(car, bus));
	}

	public void test018_AdjustMeta_TypeLevel_Relation() {
		Root engine = new Root();
		engine.addInstance(engine.getValue(), engine);
		Vertex vehicle = engine.addInstance("Vehicle");
		Vertex vehicle2 = engine.addInstance(vehicle, "Vehicle2");
		Vertex vehicleVehicle2 = engine.addInstance("VehicleVehicle2", vehicle, vehicle2);
		Vertex power = engine.addInstance("Power", vehicle);
		Vertex intensity = engine.addInstance(power, "Intensity", vehicle2);
		Vertex unit = engine.addInstance("Unit", power);
		Vertex intensityUnit = engine.addInstance(unit, "Unit", intensity);
		Vertex car = vehicle.addInstance("Car");
		Vertex bus = vehicle2.addInstance("Bus");
		power.addInstance(100, car);
		Vertex v110 = intensity.addInstance(110, bus);
		Vertex watt = unit.addInstance("Watt", power);
		Vertex kWatt = intensityUnit.addInstance(watt, "KWatt", intensity);

		Vertex vehicleVehicle2IntensityUnitWatt = engine.addInstance(vehicleVehicle2, "VehicleVehicle2IntensityUnitWatt", vehicle, vehicle2, intensity, unit, watt);
		assert vehicleVehicle2IntensityUnitWatt == vehicleVehicle2.adjustMeta(Collections.emptyList(), "carBus", Arrays.asList(car, bus, v110, intensityUnit, kWatt));
	}
}