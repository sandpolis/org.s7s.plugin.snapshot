package com.sandpolis.plugin.snapshot.server.vanilla.stream;

import com.github.cilki.qcow4j.Qcow2;
import com.sandpolis.core.net.stream.StreamSink;
import com.sandpolis.plugin.snapshot.msg.MsgSnapshot.EV_SnapshotBlock;

public class ServerBlockStreamSink extends StreamSink<EV_SnapshotBlock> {

	private Qcow2 container;

	@Override
	public void onNext(EV_SnapshotBlock item) {
		container.write(item.getData().asReadOnlyByteBuffer());
	}

	@Override
	public void close() {
		super.close();
		// container.close();
	}
}
