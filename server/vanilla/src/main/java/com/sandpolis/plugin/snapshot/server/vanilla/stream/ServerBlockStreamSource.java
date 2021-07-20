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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import com.github.cilki.qcow4j.Qcow2;
import com.google.common.hash.Hashing;
import com.google.protobuf.ByteString;
import com.sandpolis.core.net.stream.StreamSource;
import com.sandpolis.plugin.snapshot.msg.MsgSnapshot.EV_SnapshotBlock;
import com.sandpolis.plugin.snapshot.msg.MsgSnapshot.EV_SnapshotHashList;

public class ServerBlockStreamSource extends StreamSource<EV_SnapshotBlock> {

	private ServerHashStreamSink hashStream;

	private Qcow2 container;

	private ExecutorService executor;

	private void startTask() {
		executor.execute(() -> {
			var buffer = ByteBuffer.allocateDirect(65536);

			while (!Thread.interrupted()) {
				EV_SnapshotHashList ev = null;
				try {
					ev = hashStream.queue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				var offset = ev.getOffset();

				for (ByteString hash : ev.getHashList()) {
					try {
						container.read(buffer, offset);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// Hash the block and submit if different
					if (!Arrays.equals(Hashing.sha512().hashBytes(buffer).asBytes(), hash.toByteArray())) {
						submit(EV_SnapshotBlock.newBuilder().setOffset(offset).setData(ByteString.copyFrom(buffer))
								.build());
					}
					offset += buffer.capacity();
				}
			}
		});
	}

	public ServerBlockStreamSource(ServerHashStreamSink hashStream) {
		this.hashStream = hashStream;
	}

	@Override
	public void start() {
		// TODO configure concurrency
		startTask();
	}

	@Override
	public void stop() {
		executor.shutdown();
	}
}
