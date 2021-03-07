//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.plugin.snapshot.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import com.google.common.hash.Hashing;

public class SnapshotWriter extends SnapshotStream {

	private final RandomAccessFile layer;

	public SnapshotWriter(Path file, long dataSize, short blockSize, short reductionFactor) throws IOException {

		this.dataSize = dataSize;
		this.blockSize = blockSize;
		this.reductionFactor = reductionFactor;
		this.blockCount = computeBlockCount(dataSize, blockSize);

		layer = new RandomAccessFile(file.toFile(), "rw");

		// Write metadata header
		layer.seek(0);
		layer.writeByte(VERSION);
		layer.writeLong(dataSize);
		layer.writeShort(blockSize);
		layer.writeShort(reductionFactor);

		// Write hash header
		byte[] empty = new byte[HASH_BLOCK_SIZE];
		for (int i = 0; i < blockCount; i++) {
			layer.write(empty);
		}
	}

	public void writeData(int hashLevel, int hashIndex, byte[] data) throws IOException {

		synchronized (layer) {

			// Append data block
			layer.seek(layer.length());
			long offset = layer.getFilePointer();
			layer.write(data);

			// Overwrite hash
			layer.seek(computeHashOffset(hashLevel, hashIndex));
			layer.write(Hashing.murmur3_128().hashBytes(data).asBytes());
			layer.writeLong(offset);
			layer.writeShort(data.length);
			layer.writeByte(0);
		}
	}
}
