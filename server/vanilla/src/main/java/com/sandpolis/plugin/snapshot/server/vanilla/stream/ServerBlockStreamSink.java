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

import com.github.cilki.qcow4j.Qcow2;
import com.sandpolis.core.net.stream.StreamSink;
import com.sandpolis.plugin.snapshot.msg.MsgSnapshot.EV_SnapshotDataBlock;

public class ServerBlockStreamSink extends StreamSink<EV_SnapshotDataBlock> {

	private Qcow2 container;

	@Override
	public void onNext(EV_SnapshotDataBlock item) {
		container.write(item.getData().asReadOnlyByteBuffer());
	}

	@Override
	public void close() {
		super.close();
		// container.close();
	}
}
