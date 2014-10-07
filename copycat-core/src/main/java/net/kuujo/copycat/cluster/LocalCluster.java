/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.cluster;

import net.kuujo.copycat.protocol.LocalProtocol;

/**
 * Local protocol cluster.<p>
 *
 * This cluster type provides for the {@link net.kuujo.copycat.protocol.LocalProtocol}. Members of the {@code LocalCluster}
 * are identified simply by a string identifier with which threads identify one another.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class LocalCluster extends Cluster<Member> {

  public LocalCluster(ClusterConfig<Member> config) {
    super(new LocalProtocol(), config);
  }

  @Override
  public LocalCluster copy() {
    return new LocalCluster(config.copy());
  }

}