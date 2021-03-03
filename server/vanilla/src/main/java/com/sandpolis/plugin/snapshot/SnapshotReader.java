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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SnapshotReader {

	private static final int HDR_METADATA_SIZE = 28;

	private static final int HASH_BLOCK_SIZE = 25;

	private final long dataSize;

	private final int blockCount;

	private final short blockSize;

	private final short reductionFactor;

	private int rootReductionFactor;

	private final List<RandomAccessFile> layers;

	public SnapshotReader(Path file) throws IOException {

		var root = new RandomAccessFile(file.toFile(), "r");

		this.layers = new ArrayList<>();
		this.layers.add(root);

		// Read metadata
		dataSize = root.readLong();
		blockCount = root.readInt();
		blockSize = root.readShort();
		reductionFactor = root.readShort();

		byte[] parent = new byte[8];
		root.read(parent);
		while (!Arrays.equals(parent, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 })) {
			root = new RandomAccessFile(file.resolveSibling(new String(parent)).toFile(), "r");
			root.read(parent);
			layers.add(root);
		}

		// Compute root reduction factor
		rootReductionFactor = blockCount;
		while (rootReductionFactor >= reductionFactor) {
			rootReductionFactor /= reductionFactor;
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
		byte[] block = new byte[blockSize];
		synchronized (layers) {
			var root = layers.get(0);
			root.seek(computeHashOffset(hashLevel, hashIndex));

			long offset = root.readLong();
			int layer = root.readUnsignedByte();

			var target = layers.get(layer);
			target.seek(offset);
			target.read(block);
		}
		return block;
	}

	/**
	 * Index into the hash tree. This implementation uses the formula for the closed
	 * form solution of a geometric series.
	 * 
	 * @param level The 0-based level
	 * @param index The 0-based index within the given level
	 * @return The file offset of the hash structure defined by the given level and
	 *         index
	 */
	private long computeHashOffset(int level, int index) {
		return (long) (HASH_BLOCK_SIZE * rootReductionFactor
				* ((1 - Math.pow(reductionFactor, level)) / (1 - reductionFactor) + index));
	}
}
