package org.genericsystem.kernel.systemproperty.constraints;

import org.genericsystem.api.exception.ConstraintViolationException;
import org.genericsystem.kernel.AbstractVertex;
import org.genericsystem.kernel.DefaultRoot;
import org.genericsystem.kernel.DefaultVertex;

public class PropertyConstraint implements Constraint {

	@Override
	public <T extends AbstractVertex<T, U>, U extends DefaultRoot<T, U>> void check(DefaultVertex<T, U> base, DefaultVertex<T, U> attribute) throws ConstraintViolationException {
		// if (base.getHolders((T) attribute).size() > 1)
		// throw new PropertyConstraintViolationException(base + " has more than one " + attribute);

		// if (modified.isAttribute()) {
		// for (final Generic inheriting : ((GenericImpl) ((Holder) modified).getBaseComponent()).getAllInheritings()) {
		// FunctionalSnapshot<Holder> snapshot = ((GenericImpl) inheriting).getHolders((Attribute) constraintBase).filter(next -> {
		// for (int componentPos = 1; componentPos < next.getComponents().size(); componentPos++)
		// if (!Objects.equals(next.getComponent(componentPos), ((Holder) constraintBase).getComponent(componentPos)))
		// return false;
		// return true;
		// });
		// if (snapshot.size() > 1)
		// throw new PropertyConstraintViolationException(snapshot.get(0).info() + snapshot.get(1).info());
		// }
		// return;
		// }
		// if (!(((GenericImpl) constraintBase).getAllInstances().filter(next -> !next.equals(modified) && Objects.equals(next.getValue(), modified.getValue())).isEmpty()))
		// throw new PropertyConstraintViolationException("");

		// if (base.isAttribute()) {
		// for (final T inheriting : base.getComponents().iterator().next().getAllInheritings()) {
		// List<T> snapshot = inheriting.getHolders((T) attribute).stream().filter(next -> {
		// for (int componentPos = 1; componentPos < next.getComponents().size(); componentPos++)
		// if (!Objects.equals(next.getComponent(componentPos), attribute.getComponent(componentPos)))
		// return false;
		// return true;
		// }).collect(Collectors.toList());
		// if (snapshot.size() > 1)
		// throw new PropertyConstraintViolationException(snapshot.get(0).info() + snapshot.get(1).info());
		// }
		// return;
		// }
		// if (!(attribute.getAllInstances().stream().filter(next -> !next.equals(base) && Objects.equals(next.getValue(), base.getValue())).count() == 0))
		// throw new PropertyConstraintViolationException("");

	}
}