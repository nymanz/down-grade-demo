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


	/**
	 * 尝试执行
	 * @param resourceName 资源名称
	 * @param executorApi 任务API
	 * @param blockApi 限流降级逻辑
	 * @param failBackApi 异常失败逻辑
	 */
	private T runTask(String resourceName, ExecutorApi<T> executorApi, ExecutorApi<T> blockApi, ExecutorApi<T> failBackApi) throws Exception {



		try (Entry entry = SphU.entry(resourceName)) {
			return executorApi.execute();
		} catch (BlockException blockE) {
			log.warn("On call {} been down", resourceName);
			return blockApi.execute();
		} catch (Exception throwable) {
			log.error("On call {} failed, throwable=", resourceName, throwable);
			if (onErrorApi != null) {
				return failBackApi.execute();
			} else {
				throw throwable;
			}
		}
	}


}
