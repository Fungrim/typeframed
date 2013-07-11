package net.larsan.protobuf.typeframe.netty;

import io.netty.channel.ChannelFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ChannelFutureWrapper implements Future<Boolean> {

	private final ChannelFuture wrapped;

	public ChannelFutureWrapper(ChannelFuture future) {
		this.wrapped = future;
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return wrapped.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return wrapped.isCancelled();
	}

	@Override
	public boolean isDone() {
		return wrapped.isDone();
	}

	@Override
	public Boolean get() throws InterruptedException, ExecutionException {
		wrapped.get();
		return wrapped.isSuccess();
	}

	@Override
	public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		wrapped.get(timeout, unit);
		return wrapped.isSuccess();
	}
}
