package com.test.demo.htstrix;import com.netflix.hystrix.HystrixCommand;import com.netflix.hystrix.HystrixCommandGroupKey;/** * @author 赵乾泽 * @version 1.0 * @title * @description * @created 2020/7/14 11:45 下午 * @changeRecord */public class HelloWorldCommandWithStubbedFallback extends HystrixCommand<HelloWorldCommandWithStubbedFallback.UserAccount> {    private final int customerId;    private final String countryCodeFromGeoLookup;    /**     * @param customerId     *            The customerID to retrieve UserAccount for     * @param countryCodeFromGeoLookup     *            The default country code from the HTTP request geo code lookup used for fallback.     */    protected HelloWorldCommandWithStubbedFallback(int customerId, String countryCodeFromGeoLookup) {        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));        this.customerId = customerId;        this.countryCodeFromGeoLookup = countryCodeFromGeoLookup;    }    @Override    protected UserAccount run() {        // fetch UserAccount from remote service        //        return UserAccountClient.getAccount(customerId);        throw new RuntimeException("forcing failure for example");    }    @Override    protected UserAccount getFallback() {        /**         * Return stubbed fallback with some static defaults, placeholders,         * and an injected value 'countryCodeFromGeoLookup' that we'll use         * instead of what we would have retrieved from the remote service.         */        return new UserAccount(customerId, "Unknown Name",                countryCodeFromGeoLookup, true, true, false);    }    public static class UserAccount {        private final int customerId;        private final String name;        private final String countryCode;        private final boolean isFeatureXPermitted;        private final boolean isFeatureYPermitted;        private final boolean isFeatureZPermitted;        UserAccount(int customerId, String name, String countryCode,                    boolean isFeatureXPermitted,                    boolean isFeatureYPermitted,                    boolean isFeatureZPermitted) {            this.customerId = customerId;            this.name = name;            this.countryCode = countryCode;            this.isFeatureXPermitted = isFeatureXPermitted;            this.isFeatureYPermitted = isFeatureYPermitted;            this.isFeatureZPermitted = isFeatureZPermitted;        }    }    public static void main(String[] args) {        HelloWorldCommandWithStubbedFallback command = new HelloWorldCommandWithStubbedFallback(1234, "ca");        UserAccount account = command.execute();        // true        System.out.println(command.isFailedExecution());        // true        System.out.println(command.isResponseFromFallback());        // 1234        System.out.println(account.customerId);        // "ca"        System.out.println( account.countryCode);        // true        System.out.println(account.isFeatureXPermitted);        // true        System.out.println( account.isFeatureYPermitted);        // false        System.out.println( account.isFeatureZPermitted);    }}