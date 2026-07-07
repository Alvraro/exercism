import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class React {
	public static boolean DEBUG = false; 

	public static abstract class Cell<T> {
		protected T value;
		protected ArrayList<ComputeCell<T>> outputs;

		public Cell() {
			outputs = new ArrayList<>();
		}
		
		public T getValue() {
			return value;
		}
		
		protected void updateOutputs() {
			if(DEBUG) System.err.println(String.format("%s updateOutputs %s", this, outputs));

			for (ComputeCell<T> output : outputs) {
				output.markUnstableRecursively();
			}
			
			for (ComputeCell<T> output : outputs) {
				output.update();
			}
		}

		public void addOutput(ComputeCell<T> other) {
			if(DEBUG) System.err.println(String.format("%s addOutput %s", this, other));

			outputs.add(other);
		}
		
		abstract protected boolean isStable();
	}

	public static class InputCell<T> extends Cell<T> {
		public void setValue(T newValue) {
			if (value != newValue) {
				value = newValue;
				updateOutputs();
			}
		}

		@Override
		protected boolean isStable() {
			return true;
		}
	}

	public static class ComputeCell<T> extends Cell<T> {
		private Function<List<T>, T> function;
		protected ArrayList<Cell<T>> inputs;
		private ArrayList<Consumer<T>> callbacks;
		private boolean stable;

		public ComputeCell(Function<List<T>, T> function, List<Cell<T>> inputs) {
			this.function = function;
			this.inputs = new ArrayList<Cell<T>>(inputs);
			for(Cell<T> cell : inputs) {
				cell.addOutput(this);
			}
			callbacks = new ArrayList<>();
			update();
		}

		public void markUnstableRecursively() {
			stable = false;
			for(ComputeCell<T> output : outputs) {
				output.markUnstableRecursively();
			}
		}
		
		public void markStable() {
			stable = true;
		}

		public void addCallback(Consumer<T> callback) {
			callbacks.add(callback);
		}

		public void removeCallback(Consumer<T> callback) {
			callbacks.remove(callback);
		}

		public void update() {
			// wait until every input is stable
			if(inputs.stream().anyMatch(input -> !input.isStable())) {
				if(DEBUG) System.err.println(String.format("%s update can't evaluate yet", this));
				return;
			}

			// evaluate
			List<T> values = inputs.stream().map(Cell::getValue).toList();
			T newValue = function.apply(values);
			markStable();

			if(DEBUG) System.err.println(String.format("%s update from %s to %s", this, value, newValue));

			if (value != newValue) {
				value = newValue;
				updateOutputs();
				invokeCallbacks();
			}
		}

		@Override
		protected boolean isStable() {
			if(!stable)
				return false;

			if(inputs.stream().anyMatch(input -> !input.isStable()))
				return false;

			return true;
		}

		private void invokeCallbacks() {
			for (Consumer<T> callback : callbacks) {
				if(DEBUG) System.err.println(String.format("%s invokeCallback for %s", this, value));
				callback.accept(value);
			}
		}
	}

	public static <T> InputCell<T> inputCell(T initialValue) {
		InputCell<T> cell = new InputCell<>();
		cell.setValue(initialValue);
		return cell;
	}

	public static <T> ComputeCell<T> computeCell(Function<List<T>, T> function, List<Cell<T>> cells) {
		ComputeCell<T> cell = new ComputeCell<T>(function, cells);
		return cell;
	}
}
