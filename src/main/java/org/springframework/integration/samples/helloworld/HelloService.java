/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.samples.helloworld;


import com.google.common.util.concurrent.*;
import org.apache.commons.io.IOUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Simple POJO to be referenced from a Service Activator.
 *
 * @author Mark Fisher
 */
@EnableAsync
public class HelloService {

	public String sayHello(String name) {
		return "Hello " + name;
	}

	public String downloadContents(URL url) throws IOException {
		try(InputStream input = url.openStream()) {
			return IOUtils.toString(input, StandardCharsets.UTF_8);
		}
	}

	private final ExecutorService poop = Executors.newFixedThreadPool(10);

	public Future<String> downloadContentsWithFuture(final URL url) throws IOException {

		return poop.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				try(InputStream input = url.openStream()) {
					return IOUtils.toString(input, StandardCharsets.UTF_8);
				}
			}
		});

	}

	@Async
	public Future<String> downloadContentsWithFutureAsync(final URL url) throws IOException {

		try(InputStream input = url.openStream()) {
			return new AsyncResult<>(IOUtils.toString(input, StandardCharsets.UTF_8));
		}

	}
	
	public void doGuavaDownloadWithChainedThreads(final URL url) throws IOException {
		ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
		List<ListenableFuture<String>> calls = new ArrayList<ListenableFuture<String>>();
		while(calls.size()< 5) {
			com.google.common.util.concurrent.ListenableFuture<String> call = service.submit(
					new Callable<String>() {
						@Override
						public String call() throws Exception {
							try(InputStream input = url.openStream()) {
								return IOUtils.toString(input, StandardCharsets.UTF_8);
							}
						}
					}
			);
			calls.add(call);
		}
		
		ListenableFuture<List<String>> goodCalls = Futures.successfulAsList(calls);
		Futures.addCallback(goodCalls, new FutureCallback<List<String>>() {
			@Override
			public void onSuccess(List<String> strings) {
				System.out.print("Successful call");
			}

			@Override
			public void onFailure(Throwable throwable) {
				System.err.println("Problems");
			}
		});
		
	}
	
}
