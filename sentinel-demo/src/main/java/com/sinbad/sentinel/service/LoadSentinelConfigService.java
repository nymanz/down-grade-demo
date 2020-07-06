package com.sinbad.sentinel.service;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;

public class LoadSentinelConfigService<S, T> extends AbstractDataSource<S, T> {


	public LoadSentinelConfigService(Converter<S, T> parser) {
		super(parser);
	}

	@Override
	public S readSource() throws Exception {
		return null;
	}

	@Override
	public void close() throws Exception {

	}
}
