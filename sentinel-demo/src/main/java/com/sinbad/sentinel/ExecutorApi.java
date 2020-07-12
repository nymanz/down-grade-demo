package com.sinbad.sentinel;

/**
 * deprecated
 */
@FunctionalInterface
public interface ExecutorApi<T> {

	T execute() throws Exception;
}
