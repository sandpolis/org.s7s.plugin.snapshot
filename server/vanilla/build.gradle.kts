//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//

plugins {
	id("java-library")
	id("com.sandpolis.build.module")
}

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")

	compileOnly(project.getParent()?.getParent()!!)

	if (project.getParent() == null) {
		compileOnly("com.sandpolis.core.integration.qcow2:+")
	} else {
		compileOnly(project(":core:integration:com.sandpolis.core.integration.qcow2"))
	}
}

eclipse {
	project {
		name = "com.sandpolis.plugin.snapshot:server:vanilla"
		comment = "com.sandpolis.plugin.snapshot:server:vanilla"
	}
}
