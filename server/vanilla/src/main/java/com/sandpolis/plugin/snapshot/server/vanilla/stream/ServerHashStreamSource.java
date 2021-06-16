package com.sandpolis.plugin.snapshot.server.vanilla.stream;

import com.github.cilki.qcow4j.Qcow2;
import com.sandpolis.core.net.stream.StreamSource;
import com.sandpolis.plugin.snapshot.msg.MsgSnapshot.EV_SnapshotHashList;

public class ServerHashStreamSource extends StreamSource<EV_SnapshotHashList> {

	private Qcow2 container;

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
