package com.test.demo.htstrix;import com.netflix.hystrix.HystrixCommandGroupKey;import com.netflix.hystrix.HystrixObservableCommand;import com.netflix.hystrix.exception.HystrixRuntimeException;import rx.Observable;import rx.Subscriber;import rx.schedulers.Schedulers;import java.util.Collections;import java.util.Iterator;/** * @author 赵乾泽 * @version 1.0 * @title * @description * @created 2020/7/14 10:55 下午 * @changeRecord */public class HelloWorldObservableCommandFailsFastDemo extends HystrixObservableCommand<String> {    private boolean throwException;    public HelloWorldObservableCommandFailsFastDemo(Boolean throwException){        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));        this.throwException = throwException;    }    private static void call(Boolean v) {        System.out.println("onNext: " + v);    }    @Override    protected Observable<String> construct() {        return Observable.create((Observable.OnSubscribe<String>) observer -> {            try {                if (!observer.isUnsubscribed()) {                    // a real example would do work like a network call here                    observer.onError(new RuntimeException("抛出异常"));                    observer.onNext(throwException + "!");                    observer.onCompleted();                }            } catch (Exception e) {                observer.onError(e);            }        }).subscribeOn(Schedulers.io());    }    /**     * 回调方法     * @return     *///    @Override//    protected Observable<String> resumeWithFallback() {//        // fail fast 通过 throwException参数判断是抛出异常还是返回默认值//        if (throwException) {//            return Observable.error(new Throwable("failure from CommandThatFailsFast"));//        } else {//            return Observable.just("success");//        }//    }    @Override    protected Observable<String> resumeWithFallback() {        // fail silent 返回默认值        return Observable.empty();    }    public static void main(String[] args) {        // fail        // fallback 抛出的异常此处不会捕获，此处捕获到的是正常执行时的异常        HelloWorldObservableCommandFailsFastDemo worldHystrixDemo = new HelloWorldObservableCommandFailsFastDemo(true);        try{            Observable<String> fworld = worldHystrixDemo.observe();            Iterator<String> iterator = fworld.toBlocking().getIterator();            while (iterator.hasNext()){                System.out.println(iterator.next());            }        }catch (HystrixRuntimeException e){            System.out.println(e.getCause().getMessage());        }        // success//        HelloWorldObservableCommandFailsFastDemo bobHystrixDemo = new HelloWorldObservableCommandFailsFastDemo(false);//        Observable<String> fbob = bobHystrixDemo.observe();//        Iterator<String> iterator = fbob.toBlocking().getIterator();//        while (iterator.hasNext()){//            System.out.println(iterator.next());//        }    }}