package com.test.demo.htstrix;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import rx.Observable;
import rx.Subscription;
import rx.observables.BlockingObservable;

/**
 * @author sinbad on 2020/06/28
 */
public class HelloWorldHystrixCommandDemo extends HystrixCommand<String> {

	private final String helloName;

	public HelloWorldHystrixCommandDemo(String helloName) {
		super(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"));
		this.helloName = helloName;
	}

	@Override
	protected String run() throws Exception {
		return "Say hi " + helloName + "!";
	}


	public static void main(String[] args) throws ExecutionException, InterruptedException {
		HelloWorldHystrixCommandDemo worldHystrixDemo = new HelloWorldHystrixCommandDemo("MIUI");

		//1、execute
		// return this.queue().get();
		String execute = worldHystrixDemo.execute();
		System.out.println(execute);



		//2、queue
		//blockingObservable.toFuture()
		Future<String> helloFeature = worldHystrixDemo.queue();
		String helloFeatureResult = helloFeature.get();
		System.out.println(helloFeatureResult);


		//3、observe
		//
		Observable<String> helloObserve = worldHystrixDemo.observe();
		BlockingObservable<String> blockingObservable = helloObserve.toBlocking();
		Future<String> observeFeature = blockingObservable.toFuture();
		System.out.println(observeFeature.get());
		Iterator<String> observeResult = blockingObservable.getIterator();
		while (observeResult.hasNext()) {
			System.out.println(observeResult.next());
		}



		//4、toObservable
		Observable<String> helloToObserve = worldHystrixDemo.toObservable();
		BlockingObservable<String> blockingToObservable = helloToObserve.toBlocking();
		Future<String> toObserveToFeature = blockingToObservable.toFuture();
		System.out.println(toObserveToFeature);

		Subscription subscribe = helloToObserve.subscribe();
		System.out.println(subscribe);






	}
}
