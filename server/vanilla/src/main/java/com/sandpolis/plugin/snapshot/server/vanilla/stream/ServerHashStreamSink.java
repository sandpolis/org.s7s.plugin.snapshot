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
