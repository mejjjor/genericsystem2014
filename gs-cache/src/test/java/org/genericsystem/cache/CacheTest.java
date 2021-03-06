package org.genericsystem.cache;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

@Test
public class CacheTest extends AbstractTest {

	public void test001_getInheritings() {
		Engine engine = new Engine();
		Generic vehicle = engine.addInstance("Vehicle");
		assert vehicle.isAlive();
		assert vehicle.unwrap() == null;
		Generic car = engine.addInstance(vehicle, "Car");

		assert vehicle.getInheritings().stream().anyMatch(car::equals);
	}

	public void test001_getInstances() {
		Engine engine = new Engine();
		Generic vehicle = engine.addInstance("Vehicle");
		assert vehicle.isAlive();
		assert vehicle.unwrap() == null;
		assert engine.getInstances().stream().anyMatch(g -> g.equals(vehicle));
	}

	public void test001_getMetaComposites() {
		Engine engine = new Engine();
		Generic vehicle = engine.addInstance("Vehicle");
		Generic powerVehicle = engine.addInstance("power", vehicle);
		assert vehicle.getMetaComponents(engine.getMetaAttribute()).contains(powerVehicle);
		Generic myVehicle = vehicle.addInstance("myVehicle");
		Generic myVehicle123 = powerVehicle.addInstance("123", myVehicle);
		assert myVehicle.getMetaComponents(powerVehicle).contains(myVehicle123);
	}

	public void test001_getSuperComposites() {
		Engine engine = new Engine();
		Generic vehicle = engine.addInstance("Vehicle");
		Generic powerVehicle = engine.addInstance("power", vehicle);
		Generic myVehicle = vehicle.addInstance("myVehicle");
		Generic vehicle256 = powerVehicle.addInstance("256", vehicle);
		Generic myVehicle123 = powerVehicle.addInstance(vehicle256, "123", myVehicle);
		assert myVehicle123.inheritsFrom(vehicle256);
		assert myVehicle.getSuperComponents(vehicle256).contains(myVehicle123);
	}

	public void test002_getSuperComposites() {
		Engine engine = new Engine();
		Generic vehicle = engine.addInstance("Vehicle");
		Generic powerVehicle = engine.addInstance("power", vehicle);
		powerVehicle.enablePropertyConstraint();
		assert powerVehicle.isPropertyConstraintEnabled();
		Generic myVehicle = vehicle.addInstance("myVehicle");
		Generic vehicle256 = powerVehicle.addInstance("256", vehicle);
		Generic myVehicle123 = powerVehicle.addInstance("123", myVehicle);
		assert !vehicle256.equals(myVehicle123);
		assert myVehicle123.inheritsFrom(vehicle256);
		assert myVehicle.getSuperComponents(vehicle256).contains(myVehicle123);
	}

	public void test002_flush() {
		Engine engine = new Engine();
		Generic vehicle = engine.addInstance("Vehicle");
		Generic car = engine.addInstance(vehicle, "Car");
		assert vehicle.isAlive();
		assert vehicle.unwrap() == null;
		engine.getCurrentCache().flush();
		assert vehicle.isAlive();
		assert vehicle.getMeta().isAlive();
		assert vehicle.getMeta().unwrap() != null;
		assert vehicle.unwrap() != null;
		assert vehicle.unwrap().getInheritings().stream().anyMatch(car.unwrap()::equals);
	}

	public void test002_clear() {
		Engine engine = new Engine();
		Generic vehicle = engine.addInstance("Vehicle");
		engine.getCurrentCache().clear();
		assert !engine.getInstances().stream().anyMatch(g -> g.equals(vehicle));
	}

	public void test003_mountNewCache() {
		Engine engine = new Engine();
		Cache<Generic, Engine, Vertex, Root> currentCache = engine.getCurrentCache();
		Cache<Generic, Engine, Vertex, Root> mountNewCache = currentCache.mountNewCache();
		assert mountNewCache.getSubContext() == currentCache;
		Generic vehicle = engine.addInstance("Vehicle");
		assert currentCache == mountNewCache.flushAndUnmount();
		assert ((AbstractGeneric<Generic, Engine, Vertex, Root>) vehicle).unwrap() == null;
		currentCache.flush();
		assert vehicle.unwrap() != null;
	}

	public void test004_TwoCompositesWithSameMetaInDifferentCaches() {
		Engine engine = new Engine();
		Cache<Generic, Engine, Vertex, Root> currentCache = engine.getCurrentCache();
		Generic vehicle = engine.addInstance("Vehicle");
		Generic color = engine.addInstance("Color");
		Generic vehicleColor = color.addInstance("vehicleColor", vehicle);
		Cache<Generic, Engine, Vertex, Root> mountNewCache = currentCache.mountNewCache();
		Generic vehicleColor2 = color.addInstance("vehicleColor2", vehicle);
		assert vehicle.getMetaComponents(color).containsAll(Arrays.asList(vehicleColor, vehicleColor2)) : vehicle.getMetaComponents(color);
		mountNewCache.flush();
		assert vehicle.isAlive();
		assert color.isAlive();
		assert vehicleColor.isAlive();
		assert vehicleColor2.isAlive();
		assert vehicle.getMetaComponents(color).containsAll(Arrays.asList(vehicleColor, vehicleColor2)) : vehicle.getMetaComponents(color);
	}

	public void test005_TwoCompositesWithSameMetaInDifferentCaches_remove() {
		Engine engine = new Engine();
		Cache<Generic, Engine, Vertex, Root> currentCache = engine.getCurrentCache();
		Generic vehicle = engine.addInstance("Vehicle");
		Generic vehiclePower = engine.addInstance("vehiclePower", vehicle);
		Cache<Generic, Engine, Vertex, Root> mountNewCache = currentCache.mountNewCache();
		assert vehiclePower.isAlive();
		assert !vehicle.getMetaComponents(engine.getMetaAttribute()).isEmpty() : vehicle.getMetaComponents(engine.getMetaAttribute()).stream().collect(Collectors.toList());
		vehiclePower.remove();
		assert vehicle.getMetaComponents(engine.getMetaAttribute()).isEmpty() : vehicle.getMetaComponents(engine.getMetaAttribute()).stream().collect(Collectors.toList());
		mountNewCache.flush();
		assert vehicle.isAlive();
		assert !vehiclePower.isAlive();
		assert vehicle.getMetaComponents(engine.getMetaAttribute()).isEmpty() : vehicle.getMetaComponents(engine.getMetaAttribute()).stream().collect(Collectors.toList());
	}
}
