/*
 * Copyright 2016-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.primitives.value.impl;

import io.atomix.primitives.PrimitiveException;
import io.atomix.primitives.Synchronous;
import io.atomix.primitives.value.AsyncAtomicValue;
import io.atomix.primitives.value.AtomicValue;
import io.atomix.primitives.value.AtomicValueEventListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Default implementation for a {@code AtomicValue} backed by a {@link AsyncAtomicValue}.
 *
 * @param <V> value type
 */
public class DefaultAtomicValue<V> extends Synchronous<AsyncAtomicValue<V>> implements AtomicValue<V> {

  private final AsyncAtomicValue<V> asyncValue;
  private final long operationTimeoutMillis;

  public DefaultAtomicValue(AsyncAtomicValue<V> asyncValue, long operationTimeoutMillis) {
    super(asyncValue);
    this.asyncValue = asyncValue;
    this.operationTimeoutMillis = operationTimeoutMillis;
  }

  @Override
  public boolean compareAndSet(V expect, V update) {
    return complete(asyncValue.compareAndSet(expect, update));
  }

  @Override
  public V get() {
    return complete(asyncValue.get());
  }

  @Override
  public V getAndSet(V value) {
    return complete(asyncValue.getAndSet(value));
  }

  @Override
  public void set(V value) {
    complete(asyncValue.set(value));
  }

  @Override
  public void addListener(AtomicValueEventListener<V> listener) {
    complete(asyncValue.addListener(listener));
  }

  @Override
  public void removeListener(AtomicValueEventListener<V> listener) {
    complete(asyncValue.removeListener(listener));
  }

  private <T> T complete(CompletableFuture<T> future) {
    try {
      return future.get(operationTimeoutMillis, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new PrimitiveException.Interrupted();
    } catch (TimeoutException e) {
      throw new PrimitiveException.Timeout();
    } catch (ExecutionException e) {
      throw new PrimitiveException(e.getCause());
    }
  }
}