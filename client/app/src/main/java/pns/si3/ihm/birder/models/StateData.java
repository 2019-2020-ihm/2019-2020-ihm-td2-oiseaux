package pns.si3.ihm.birder.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * State data.
 *
 * Allows the repositories to indicate a data status :
 * Loading, Success, Error.
 *
 * @param <T> The data class.
 */
public class StateData<T> {
	/**
	 * The data status.
	 */
	@NonNull
	private DataStatus status;

	/**
	 * The data.
	 */
	@Nullable
	private T data;

	/**
	 * The error.
	 */
	@Nullable
	private Throwable error;

	/**
	 * Constructs a state data.
	 */
	private StateData() {
		this.status = DataStatus.LOADING;
		this.data = null;
		this.error = null;
	}

	public boolean isSuccessful() {
		return this.status == DataStatus.SUCCESS;
	}

	@Nullable
	public T getData() {
		return data;
	}

	@Nullable
	public Throwable getError() {
		return error;
	}

	public static <T> StateData<T> success(T data) {
		StateData<T> stateData = new StateData<>();
		stateData.status = DataStatus.SUCCESS;
		stateData.data = data;
		stateData.error = null;
		return stateData;
	}

	public static <T> StateData<T> success() {
		return success(null);
	}

	public static <T> StateData<T> error(Throwable error) {
		StateData<T> stateData = new StateData<>();
		stateData.status = DataStatus.SUCCESS;
		stateData.data = null;
		stateData.error = error;
		return stateData;
	}
}
