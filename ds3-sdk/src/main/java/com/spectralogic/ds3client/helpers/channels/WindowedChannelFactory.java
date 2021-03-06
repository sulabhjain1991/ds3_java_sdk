/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.helpers.channels;

import java.nio.channels.SeekableByteChannel;

public class WindowedChannelFactory implements AutoCloseable {
    private final SeekableByteChannel channel;
    private final Object lock = new Object();

    public WindowedChannelFactory(final SeekableByteChannel channel) {
        this.channel = channel;
    }
    
    public SeekableByteChannel get(final long offset, final long length) {
        return new WindowedSeekableByteChannel(this.channel, this.lock, offset, length);
    }

    @Override
    public void close() throws Exception {
        this.channel.close();
    }
}
