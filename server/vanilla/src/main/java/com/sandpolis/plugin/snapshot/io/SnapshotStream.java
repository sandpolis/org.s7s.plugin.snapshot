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

public abstract class SnapshotStream {

	protected static final byte VERSION = 0x01;

	protected static final int HDR_METADATA_SIZE = 28;

	protected static final int HASH_BLOCK_SIZE = 25;

	protected long dataSize;

	protected int blockCount;

	protected short blockSize;

	protected short reductionFactor;

	protected int rootReductionFactor;

	protected int computeBlockCount(long dataSize, short blockSize) {
		return (int) (dataSize / blockSize);
	}

	protected int computeRootReductionFactor(int blockCount, short reductionFactor) {

		int rootReductionFactor = blockCount;
		while (rootReductionFactor >= reductionFactor) {
			rootReductionFactor /= reductionFactor;
		}
		return rootReductionFactor;
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
	protected long computeHashOffset(int level, int index) {
		return (long) (HASH_BLOCK_SIZE * rootReductionFactor
				* ((1 - Math.pow(reductionFactor, level)) / (1 - reductionFactor) + index));
	}
}
