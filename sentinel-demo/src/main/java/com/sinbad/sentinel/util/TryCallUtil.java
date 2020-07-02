package com.sinbad.sentinel.util;

import java.util.concurrent.Callable;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.sinbad.sentinel.ExecutorApi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sinbad on 2020/07/02.
 */
@Slf4j
public class TryCallUtil<T> implements Callable<T> {


	private String resourceName;

	private ExecutorApi<T> executorApi;
	private ExecutorApi<T> failBackApi;

	private ExecutorApi<T> onErrorApi;


	public interface OnErrorApi {
		void onError(int retryIndex, Throwable th) throws Throwable;
	}


	@Override
	public T call() throws Exception {







		return runTask(resourceName, executorApi, failBackApi, onErrorApi);
	}


	private T runTask(String resourceName, ExecutorApi<T> executorApi, ExecutorApi<T> failBackApi, ExecutorApi<T> onErrorApi) throws Exception {

		try (Entry entry = SphU.entry(resourceName)) {
			return executorApi.execute();
		} catch (BlockException blockE) {
			log.warn("On call {} been down", resourceName);
			return failBackApi.execute();
		} catch (Exception throwable) {
			log.error("On call {} failed, throwable=", resourceName, throwable);
			if (onErrorApi != null) {
				return onErrorApi.execute();
			} else {
				throw throwable;
			}
		}
	}


}
