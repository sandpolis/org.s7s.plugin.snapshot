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

import com.sandpolis.core.net.stream.StreamSink;
import com.sandpolis.plugin.snapshot.msg.MsgSnapshot.EV_SnapshotHashList;

public class ServerHashStreamSink extends StreamSink<EV_SnapshotHashList> {

	BlockingQueue<EV_SnapshotHashList> queue;

	@Override
	public void onNext(EV_SnapshotHashList item) {
		queue.add(item);
	}

}
