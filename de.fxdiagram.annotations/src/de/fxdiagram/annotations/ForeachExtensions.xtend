package de.fxdiagram.annotations

class ForeachExtensions {
		
	/**
	 * Applies {@code procedure} for each element of the given iterable.
	 * 
	 * @param iterable
	 *            the iterable. May not be <code>null</code>.
	 * @param procedure
	 *            the procedure. May not be <code>null</code>.
	 */
	def static <T> void forEachExt(Iterable<T> iterable, (T)=>void procedure) {
		IterableExtensions.forEach(iterable, procedure)
	}
	
	/**
	 * Applies {@code procedure} for each element of the given iterable.
	 * The procedure takes the element and a loop counter. If the counter would overflow, {@link Integer#MAX_VALUE}
	 * is returned for all subsequent elements. The first element is at index zero.
	 * 
	 * @param iterable
	 *            the iterable. May not be <code>null</code>.
	 * @param procedure
	 *            the procedure. May not be <code>null</code>.
	 * @since 2.3
	 */
	def static <T> void forEachExt(Iterable<T> iterable, (T, Integer)=>void procedure) {
		IterableExtensions.forEach(iterable, procedure)
	}
	
}