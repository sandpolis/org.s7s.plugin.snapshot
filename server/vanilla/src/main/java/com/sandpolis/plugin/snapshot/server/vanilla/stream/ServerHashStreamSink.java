//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.plugin.snapshot.server.vanilla.stream;

import java.util.concurrent.BlockingQueue;

import com.sandpolis.core.instance.stream.StreamSink;
import com.sandpolis.plugin.snapshot.Messages.EV_SnapshotHashBlock;

public class ServerHashStreamSink extends StreamSink<EV_SnapshotHashBlock> {

	BlockingQueue<EV_SnapshotHashBlock> queue;

	@Override
	public void onNext(EV_SnapshotHashBlock item) {
		queue.add(item);
	}

}
