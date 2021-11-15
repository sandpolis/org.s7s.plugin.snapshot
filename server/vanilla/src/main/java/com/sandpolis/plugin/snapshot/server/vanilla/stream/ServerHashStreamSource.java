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
import com.sandpolis.core.net.stream.StreamSource;
import com.sandpolis.plugin.snapshot.Messages.EV_SnapshotHashBlock;

public class ServerHashStreamSource extends StreamSource<EV_SnapshotHashBlock> {

	private Qcow2 container;

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
