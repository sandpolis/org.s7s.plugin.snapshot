//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.plugin.snapshot;

public class SnapshotReader {

	private static final HDR_METADATA_SIZE = 20;

	private final long dataSize;

	private final int blockCount;

	private final short blockSize;

	private final short reductionFactor;

	private final MappedByteBuffer buffer;

	public SnapshotHandle(Path file) {
		var channel = Files.newByteChannel(file, EnumSet.of(StandardOpenOption.READ));
		buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

		// Read metadata
		dataSize = buffer.getLong(0);
		blockCount = buffer.getInt(8);
		blockSize = buffer.getShort(12);
		reductionFactor = buffer.getShort(16);
	}

	public byte[] readHash(int hashLevel, int hashIndex) {

	}

	public byte[] readBlock(long blockIndex) {
		byte[] block = new byte[blockSize];
		synchronized (buffer) {
			buffer.position(HDR_METADATA_SIZE + (0) + (blockIndex * blockSize));
			buffer.get(block);
		}
		return block;
	}

}
