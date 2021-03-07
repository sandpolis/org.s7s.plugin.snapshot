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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SnapshotReader extends SnapshotStream {

	private final List<RandomAccessFile> layers;

	public SnapshotReader(Path file) throws IOException {

		var root = new RandomAccessFile(file.toFile(), "r");

		this.layers = new ArrayList<>();
		this.layers.add(root);

		// Read metadata
		root.seek(0);
		var version = root.readByte();
		if (version != VERSION) {
			// TODO
		}
		dataSize = root.readLong();
		blockSize = root.readShort();
		reductionFactor = root.readShort();
		blockCount = computeBlockCount(dataSize, blockSize);
		rootReductionFactor = computeRootReductionFactor(blockCount, reductionFactor);

		byte[] parent = new byte[8];
		root.read(parent);
		while (!Arrays.equals(parent, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 })) {
			root = new RandomAccessFile(file.resolveSibling(new String(parent)).toFile(), "r");
			root.read(parent);
			layers.add(root);
		}
	}

	public byte[] readHash(int hashLevel, int hashIndex) throws IOException {
		byte[] hash = new byte[16];
		synchronized (layers) {
			var root = layers.get(0);
			root.seek(computeHashOffset(hashLevel, hashIndex));
			root.read(hash);
		}
		return hash;
	}

	public byte[] readData(int hashLevel, int hashIndex) throws IOException {
		synchronized (layers) {
			var root = layers.get(0);
			root.seek(computeHashOffset(hashLevel, hashIndex) + 16);

			long offset = root.readLong();
			short length = root.readShort();
			int layer = root.readUnsignedByte();

			byte[] block = new byte[length];

			var target = layers.get(layer);
			target.seek(offset);
			target.read(block);
			return block;
		}
	}

}
