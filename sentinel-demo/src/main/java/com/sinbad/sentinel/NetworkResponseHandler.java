package com.sinbad.sentinel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.xiaomi.vip.common.context.VipContext;

public class NetworkResponseHandler<T> {



	private String resourceName;


	private Class<T> fallbackMethod;
	private Impl<T> impl;

	public interface Impl<T> {
		/*
		 * 获取网络请求
		 */
		NetworkResponse getResponse() throws Throwable;

		/*
		 * 出错时创建一个Exception
		 */
		Exception onError(String errorMsg);

		/*
		 * 出错时创建一个Exception
		 */
		Exception onError(String errorMsg, Throwable ex);

		/*
		 * 是否正确的返回
		 */
		boolean isValidValue(T object);
	}





	public NetworkResponseHandler(String url, Class<T> classType, Impl<T> impl) {
		this.url = url;
		this.classType = classType;
		this.impl = impl;
	}

	@SuppressWarnings("unchecked")
	public T execute(VipContext vipContext, long userId) throws Exception {
		NetworkResponse response;
		try {
			response = impl.getResponse();
		} catch (Throwable ex) {
			LOGGER.error("Fail to send request to " + url, ex);
			throwExceptionIfNotNull(impl.onError("Fail to send request to " + url, ex));
			return null;
		}

		if (response.getStatusCode() != 200) {
			reportError(userId, response);
			return null;
		}

		Gson gson = new Gson();
		T responseObject = null;
		try {
			if (classType == String.class) {
				responseObject = (T) response.getContent();
			} else if (classType != null) {
				responseObject = gson.fromJson(response.getContent(), classType);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected response. statusCode={}, userId={}, content={}",
					response.getStatusCode(), userId, response.getContent(), ex);
			throwExceptionIfNotNull(impl.onError(String.format("Unexpected response. statusCode=%d, "
					+ "userId=%d, content=%s", response.getStatusCode(), userId, response.getContent()), ex));
			return null;
		}

		if (responseObject == null) {
			reportError(userId, response);
			return null;
		}

		if (!impl.isValidValue(responseObject)) {
			reportError(userId, response);
			return null;
		}

		return responseObject;
	}

	private void reportError(long userId, NetworkResponse response)
			throws Exception {
		LOGGER.error("Unexpected response. statusCode={}, userId={}, content={}",
				response.getStatusCode(), userId, response.getContent());
		Exception exception = impl.onError(String.format(
				"Unexpected response. statusCode=%d, userId=%d, content=%s",
				response.getStatusCode(), userId, response.getContent()));
		throwExceptionIfNotNull(exception);
	}

	private void throwExceptionIfNotNull(Exception exception) throws Exception {
		if (exception != null) {
			throw exception;
		}
	}
}
