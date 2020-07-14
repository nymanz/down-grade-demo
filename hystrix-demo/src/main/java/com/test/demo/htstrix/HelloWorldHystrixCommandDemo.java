package com.test.demo.htstrix;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import rx.Observable;
import rx.Subscription;
import rx.observables.BlockingObservable;

/**
 * @author sinbad on 2020/06/28
 */
public class HelloWorldHystrixCommandDemo extends HystrixCommand<String> {

	private final String helloName;

//	public HelloWorldHystrixCommandDemo(String helloName) {
//		super(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"));
//		this.helloName = helloName;
//	}

	/**
	 * 设置 groupKey 、 CommandKey 和 threadPoolName
	 * @param helloName
	 */
	public HelloWorldHystrixCommandDemo(String helloName) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorldCommand"))
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HelloWorldThreadPool"))
		);
		this.helloName = helloName;
	}

	@Override
	protected String run() throws Exception {

		return "Say hi " + helloName + "!";
//		throw new RuntimeException("throw RuntimeException");
	}

	/**
	 * 降级。Hystrix会在run()执行过程中出现错误、超时、线程池拒绝、断路器熔断等情况时，
	 * 执行getFallBack()方法内的逻辑
	 */
	@Override
	protected String getFallback() {
		return "Hello fail, " + helloName;
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		HelloWorldHystrixCommandDemo worldHystrixDemo = new HelloWorldHystrixCommandDemo("MIUI");

		//1、execute
		// 异步执行
		// return this.queue().get();
		// 同步执行
//		String execute = worldHystrixDemo.execute();
//		System.out.println(execute);



		//2、queue
		//blockingObservable.toFuture()
		Future<String> helloFeature = worldHystrixDemo.queue();
		String helloFeatureResult = helloFeature.get();
		System.out.println(helloFeatureResult);


	}
}
